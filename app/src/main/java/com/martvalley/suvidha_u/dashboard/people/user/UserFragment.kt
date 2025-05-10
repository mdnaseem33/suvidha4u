package com.martvalley.suvidha_u.dashboard.people.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.dashboard.retailerModule.user.adapter.UserListAdapter
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.databinding.FragmentPeopleBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {
    private val binding by lazy { FragmentPeopleBinding.inflate(layoutInflater) }
    private lateinit var adapter: UserAdapter
    private lateinit var userListAdapter: UserListAdapter
    val list = ArrayList<User.Customer>()

    val vieww =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.getBooleanExtra("statuschanged", false) == true) {
                    withNetwork { callApi() }
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding. root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.clearSearch.setOnClickListener {
//            binding.searchEt.text.clear()
//            adapter.mList = list
//            adapter.notifyDataSetChanged()
//        }

        adapter = UserAdapter(list, requireContext()) { data, action, pos ->
            when (action) {

                "more" -> {
                    MoreOptionFragment().apply {
                        innterface = object : MoreOption {
                            override fun view() {
                                vieww.launch(
                                    Intent(
                                        requireContext(),
                                        UserViewActivity::class.java
                                    ).putExtra("id", data.id)
                                )
                                dismiss()
                            }

                            override fun control() {
                                if (data.key_type==5 && data.payment_term != 1){
                                    showToast("No Emi for this anti theft")
                                }else{
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            ControlsActivity::class.java
                                        ).putExtra("id", data.id)
                                    )
                                    dismiss()
                                }

                            }

                            override fun delete() {
                                withNetwork { callDeleteApi(data.id.toString(), pos) }
                                dismiss()
                            }

                        }
                    }.show(parentFragmentManager, "")
                }
                "detail" -> {
//                    startActivity(
//                        Intent(
//                            requireContext(), UserViewActivity::class.java
//                        ).putExtra("id", data.id)
//                    )
                }
                "control" -> {
                    if (data.key_type==5 && data.payment_term != 1){
                        showToast("No Emi for this anti theft")
                    }else {
                        startActivity(
                            Intent(
                                requireContext(),
                                ControlsActivity::class.java
                            ).putExtra("id", data.id)
                        )
                    }
                }
            }
        }
        binding.rv.adapter = adapter

//        binding.searchEt.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                filterList(p0.toString().lowercase())
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//
//        })

        withNetwork { callApi() }
    }

    private fun filterList(key: String) {
        val filter_list = ArrayList<User.Customer>()
        list.forEach {
            if (it.name.lowercase().contains(key)) {
                filter_list.add(it)
            }
        }
        adapter.mList = filter_list
        adapter.notifyDataSetChanged()
    }

    private fun callApi() {
       // binding.pb.show()
        val call = RetrofitInstance.apiService.getCustomerListApi()
        call.enqueue(object : Callback<User.UserListResponse> {
            override fun onResponse(
                call: Call<User.UserListResponse>, response: Response<User.UserListResponse>
            ) {
               // binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.clear()
                            list.addAll(it.customer)
                            adapter.notifyDataSetChanged()
                        }
                    }

                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<User.UserListResponse>, t: Throwable) {
                //binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }

    private fun callChangeStatusApi(id: String, status: String, pos: Int) {
        //binding.pb.show()
        val status_value = if (status == "0") 1 else 0
        val request = Retailer.StatusChangeRequest(id, status_value.toString())
        val call = RetrofitInstance.apiService.userStatusChangeApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
               // binding.pb.hide()
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
               // binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }

    private fun callDeleteApi(id: String, pos: Int) {
        //binding.pb.show()
        val request = Retailer.DeleteRequest(id)
        val call = RetrofitInstance.apiService.userDeleteApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                //binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                        }
                    }
                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.StatusChangeResponse>, t: Throwable) {
               // binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }


}