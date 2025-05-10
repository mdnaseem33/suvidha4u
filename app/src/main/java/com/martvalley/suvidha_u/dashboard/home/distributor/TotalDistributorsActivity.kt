package com.martvalley.suvidha_u.dashboard.home.distributor

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.dashboard.people.retailer.RetailerAdapter
import com.martvalley.suvidha_u.dashboard.people.retailer.RetailerViewActivity
import com.martvalley.suvidha_u.databinding.ActivityTotalDistributorsBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TotalDistributorsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTotalDistributorsBinding.inflate(layoutInflater) }
    private lateinit var adapter: RetailerAdapter
    val list = ArrayList<Retailer.User>()

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


        adapter = RetailerAdapter(list, this) { data, action, pos ->
            when (action) {
                "action" -> {
                    callChangeStatusApi(
                        data.id.toString(), data.status.toString(), pos
                    )
                }
                "detail" -> {
                    vieww.launch(
                        Intent(
                            this, RetailerViewActivity::class.java
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
        val filter_list = ArrayList<Retailer.User>()
        list.forEach {
            if (it.name != null && it.name.lowercase().contains(key)) {
                filter_list.add(it)
            }else if (it.id != null && it.id.toString().lowercase().contains(key)) {
                filter_list.add(it)
            }else if (it.phone != null && it.phone.lowercase().contains(key)) {
                filter_list.add(it)
            }
        }
        adapter.mList = filter_list
        adapter.notifyDataSetChanged()
    }

    private fun callApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getRetailerListApi()
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
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<Retailer.RetailerListResponse>, t: Throwable
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


}
