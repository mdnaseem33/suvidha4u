package com.martvalley.suvidha_u.dashboard.people.user

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.databinding.ActivityUserViewBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUserViewBinding.inflate(layoutInflater) }
    private val id by lazy { intent.getIntExtra("id", 0) }
    lateinit var user: User.Customer
    var statuschanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        withNetwork { callViewApi() }

        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.text.text = "User Detail"
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

        binding.controlBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ControlsActivity::class.java
                ).putExtra("id", id)
            )
        }

        binding.deleteBtn.setOnClickListener {
            withNetwork { callDeleteApi(id.toString()) }
        }

    }

    private fun callDeleteApi(id: String) {
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
                            statuschanged = true
                            onBackPressed()
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

    private fun callViewApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.viewUserApi(id.toString())
        call.enqueue(object : Callback<User.ViewUserDetailsResponse> {
            override fun onResponse(
                call: Call<User.ViewUserDetailsResponse>,
                response: Response<User.ViewUserDetailsResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            user = it.customer
                            bind(it.customer)
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<User.ViewUserDetailsResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

    fun bind(data: User.Customer) {

        binding.name.text = data.name ?: ""
        binding.imei1Value.text = data.imei1 ?: ""
        binding.imei2Value.text = data.imei2 ?: ""
        binding.createdValue.text = data.created_at.convertISOTimeToDate()
        binding.syncValue.text = data.last_sync?.split(" ")?.get(0) ?: ""

        binding.statusBtn.text = when (data.status) {
            0 -> "Summit"
            1 -> "active"
            else -> ""
        }

        if (data.status == 1) {
            binding.statusBtn.backgroundTintList =
                ColorStateList.valueOf(getColor(R.color.green))
        } else {
            binding.statusBtn.backgroundTintList =
                ColorStateList.valueOf(getColor(R.color.red))
        }

//        binding.statusBtn.setOnClickListener {
//            if (binding.statusBtn.text != "Removed") {
//                callChangeStatusApi(
//                    data.id.toString(),
//                    data.status.toString()
//                )
//            }
//        }

    }

    private fun callChangeStatusApi(id: String, status: String) {
        binding.pb.show()
        val status_value = if (status == "0") 1 else 0
        val request = Retailer.StatusChangeRequest(id, status_value.toString())
        val call = RetrofitInstance.apiService.userStatusChangeApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            user.status = status_value
                            statuschanged = true
                            binding.statusBtn.text = when (user.status) {
                                0 -> "Removed"
                                1 -> "active"
                                else -> ""
                            }

                            if (user.status == 1) {
                                binding.statusBtn.backgroundTintList =
                                    ColorStateList.valueOf(getColor(R.color.green))
                            } else {
                                binding.statusBtn.backgroundTintList =
                                    ColorStateList.valueOf(getColor(R.color.red))
                            }
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

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK, Intent().putExtra("statuschanged", statuschanged))
        finish()
    }

}