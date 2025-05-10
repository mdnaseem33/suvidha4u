package com.martvalley.suvidha_u.dashboard.home.retailer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.dashboard.people.user.*
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.databinding.ActivityTotalRetailersBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TotalRetailersActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTotalRetailersBinding.inflate(layoutInflater) }
    private lateinit var adapter: UserAdapter
    val list = ArrayList<User.Customer>()

    val vieww =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.getBooleanExtra("statuschanged", false) == true) {
                    withNetwork { callApi() }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.text.text = "Total Users"
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()


        adapter = UserAdapter(list, this) { data, action, pos ->
            when (action) {
//                "action" -> callChangeStatusApi(data.id.toString(), data.status.toString(), pos)
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
                            }

                            override fun delete() {
                                withNetwork { callDeleteApi(data.id.toString(), pos) }
                                dismiss()
                            }

                        }
                    }.show(supportFragmentManager, "")
                }
                "control" -> {
                    startActivity(
                        Intent(
                            this,
                            ControlsActivity::class.java
                        ).putExtra("id", data.id)
                    )
                }
            }

        }

        binding.rv.adapter = adapter

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

        withNetwork { callApi() }

    }


    private fun filterList(key: String) {
        val filter_list = ArrayList<User.Customer>()
        list.forEach {
            if (it.name != null && it.name.lowercase().contains(key)) {
                filter_list.add(it)
            }else if (it.id != null && it.id.toString().lowercase().contains(key)) {
                filter_list.add(it)
            }else if (it.phone != null && it.phone.lowercase().contains(key)) {
                filter_list.add(it)
            }else if ( it.imei1 != null && it.imei1.lowercase().contains(key)) {
                filter_list.add(it)
            }
        }
        adapter.mList = filter_list
        adapter.notifyDataSetChanged()
    }

    private fun callApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getCustomerListApi()
        call.enqueue(object : Callback<User.UserListResponse> {
            override fun onResponse(
                call: Call<User.UserListResponse>,
                response: Response<User.UserListResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.clear()
                            list.addAll(it.customer)
                            binding.toolbar.text.text = "Total Users (${it.customer.size})"
                            adapter.notifyDataSetChanged()
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<User.UserListResponse>, t: Throwable
            ) {
                binding.pb.hide()
                showApiErrorToast()
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
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.StatusChangeResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

    private fun callDeleteApi(id: String, pos: Int) {
        binding.pb.show()
        val request = Retailer.DeleteRequest(id)
        val call = RetrofitInstance.apiService.userDeleteApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.StatusChangeResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

}
