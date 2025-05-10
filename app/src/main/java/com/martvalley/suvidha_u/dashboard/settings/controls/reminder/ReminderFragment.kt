package com.martvalley.suvidha_u.dashboard.settings.controls.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.dashboard.settings.controls.Control
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.databinding.FragmentReminderBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReminderFragment : Fragment() {
    private val binding by lazy { FragmentReminderBinding.inflate(layoutInflater) }
    private lateinit var adapter: ReminderAdapter
    val list = ArrayList<Control.Record>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReminderAdapter(list, requireContext()) { data, pos ->
            UpdateActivity() {
                withNetwork { callApi() }
            }.apply {
                user_id = data.id
            }.show(parentFragmentManager, "")
        }

        binding.rv.adapter = adapter

        val customer = (requireActivity() as ControlsActivity).cust_data!!.customer
        binding.textViewCharges.text = "Bounce charges: ₹${customer.charges}"
        binding.editTextCharges.setText(customer.charges.toString())
        binding.buttonEdit.setOnClickListener {
            binding.editTextCharges.show()
            binding.buttonUpdate.show()
        }
        val id = (requireActivity() as ControlsActivity).id.toString()
        binding.buttonUpdate.setOnClickListener {
            view ->
            if (binding.editTextCharges.text.toString().isNotEmpty()) {
                val chargesRequest = ChargesRequest( binding.editTextCharges.text.toString(), id)
                updateCharge(chargesRequest)
            } else {
                showToast("Please enter charges")
            }
        }
        withNetwork { callApi() }
    }

    private fun callApi() {
        binding.pb.show()
        val call =
            RetrofitInstance.apiService.getEmiTransactionListApi((requireActivity() as ControlsActivity).id.toString())
        call.enqueue(object : Callback<Control.EmiTransactionListResponse> {
            override fun onResponse(
                call: Call<Control.EmiTransactionListResponse>,
                response: Response<Control.EmiTransactionListResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.clear()
                            list.addAll(it.record)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Control.EmiTransactionListResponse>, t: Throwable) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }

    private fun updateCharge(chargesRequest: ChargesRequest) {
        binding.pb.show()
        val call =
            RetrofitInstance.apiService.updateChargesCustomer(chargesRequest)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                p0: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            if (it.status == 200) {
                                binding.editTextCharges.hide()
                                binding.buttonUpdate.hide()
                                binding.textViewCharges.text = "Bounce charges: ₹${chargesRequest.charges}"
                            }
                            showToast("${it.message}")
                        }
                    }
                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.StatusChangeResponse>, t: Throwable) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }
}