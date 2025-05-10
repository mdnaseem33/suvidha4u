package com.martvalley.suvidha_u.dashboard.people.retailer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.FragmentRetailerListBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetailerListFragment : Fragment() {
    private val binding by lazy { FragmentRetailerListBinding.inflate(layoutInflater) }
    private lateinit var adapter: RetailerAdapter
    val list = ArrayList<Retailer.User>()
    private var retailerType: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    val edit =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                withNetwork { callApi(retailerType) }
            }
        }


    val vieww =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.getBooleanExtra("statuschanged", false) == true) {
                    withNetwork { callApi(retailerType) }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retailerType = arguments?.getString("type")
        adapter = RetailerAdapter(list, requireContext()) { data, action, pos ->
            when (action) {
                "action" -> {
                    callChangeStatusApi(data.id.toString(), data.status.toString(), pos)
                }
                "more" -> {
                    MoreOptionFragment().apply {
                        innterface = object : MoreOption {
                            override fun edit() {
                                edit.launch(
                                    Intent(
                                        requireContext(),
                                        EditRetailerActivity::class.java
                                    ).putExtra("id", data.id)
                                )
                                dismiss()
                            }

                            override fun view() {
                                vieww.launch(
                                    Intent(
                                        requireContext(),
                                        RetailerViewActivity::class.java
                                    ).putExtra("id", data.id)
                                )
                                dismiss()
                            }

                            override fun delete() {
                                withNetwork { callDeleteApi(data.id.toString(), pos) }
                                dismiss()
                            }

                            override fun credit() {
                                startActivity(
                                    Intent(
                                        requireContext(), CreditRetailerActivity::class.java
                                    ).putExtra("id", data.id)
                                )
                                dismiss()
                            }

                            override fun debit() {
                                startActivity(
                                    Intent(
                                        requireContext(), DebitRetailerActivity::class.java
                                    ).putExtra("id", data.id)
                                )
                                dismiss()
                            }

                        }
                    }.show(parentFragmentManager, "")
                }
                "detail" -> {
                    startActivity(
                        Intent(
                            requireContext(), RetailerViewActivity::class.java
                        ).putExtra("id", data.id)
                    )
                }
            }
        }
        binding.rv.adapter = adapter


        val res =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    withNetwork { callApi(retailerType) }
                }
            }

        binding.createRetailer.setOnClickListener {
            res.launch(Intent(requireContext(), CreateRetailerActivity::class.java))
        }

        binding.clearSearch.setOnClickListener {
            binding.searchEt.text.clear()
            adapter.mList = list
            adapter.notifyDataSetChanged()
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0.toString().lowercase())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        withNetwork { callApi(retailerType) }

    }


    private fun filterList(key: String) {
        val filter_list = ArrayList<Retailer.User>()
        list.forEach {
            if (it.name.lowercase().contains(key) || it.id.toString().contains(key) || it.email.contains(key) || it.phone.contains(key) ) {
                filter_list.add(it)
            }
        }
        adapter.mList = filter_list
        adapter.notifyDataSetChanged()
    }

    private fun callApi(retailerType: String?) {
        retailerType.logd("fragment data")
        binding.pb.show()
        val call = RetrofitInstance.apiService.getRetailerListApi(type = retailerType)
        call.enqueue(object : Callback<Retailer.RetailerListResponse> {
            override fun onResponse(
                call: Call<Retailer.RetailerListResponse>,
                response: Response<Retailer.RetailerListResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.clear()
                            list.addAll(it.users)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.RetailerListResponse>, t: Throwable) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }

    private fun callDeleteApi(id: String, pos: Int) {
        binding.pb.show()
        val request = Retailer.DeleteRequest(id)
        val call = RetrofitInstance.apiService.retailerDeleteApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            withNetwork { callApi(retailerType) }
//                            adapter.notifyDataSetChanged()
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

    private fun callChangeStatusApi(id: String, status: String, pos: Int) {
        binding.pb.show()
        val status_value = if (status == "0") 1 else 0
        val request = Retailer.StatusChangeRequest(id, status_value.toString())
        val call = RetrofitInstance.apiService.retailerStatusChangeApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list[pos].status = status_value
                            adapter.notifyDataSetChanged()
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