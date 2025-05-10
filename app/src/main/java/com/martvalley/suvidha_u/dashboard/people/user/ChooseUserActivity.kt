package com.martvalley.suvidha_u.dashboard.people.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityChooseRetailerBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseUserActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChooseRetailerBinding.inflate(layoutInflater) }
    private lateinit var adapter: ChooseUserAdapter
    val list = ArrayList<User.Customer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.text.text = "Choose user"
        binding.toolbar.calender.hide()
        binding.toolbar.filter.hide()
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }

        adapter = ChooseUserAdapter(list, this) { data, pos ->
            setResult(RESULT_OK, Intent().putExtra("id", data.id))
            finish()
        }
        binding.rv.adapter = adapter

        withNetwork { callApi() }

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
                            adapter.notifyDataSetChanged()
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<User.UserListResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }


}