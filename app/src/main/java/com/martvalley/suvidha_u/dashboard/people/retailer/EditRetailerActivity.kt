package com.martvalley.suvidha_u.dashboard.people.retailer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityEditRetailerBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditRetailerActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditRetailerBinding.inflate(layoutInflater) }
    private val id by lazy { intent.getIntExtra("id", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.text.text = "Edit Retailer"
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

        withNetwork { callViewApi() }

        binding.saveButton.setOnClickListener {
            val address = binding.retailerAddressEt.text.trim().toString()
            val email = binding.retailerEmailEt.text.trim().toString()
            val gst = binding.retailerGstEt.text.trim().toString()
            val name = binding.retailerNameEt.text.trim().toString()
            val ownername = binding.retailerOwnernameEt.text.trim().toString()
            val state = binding.retailerStateEt.text.trim().toString()
            val phone = binding.retailerPhoneEt.text.trim().toString()

            if (name.isEmpty()) {
                showToast("Enter retailer name.")
            } else if (ownername.isEmpty()) {
                showToast("Enter owner name.")
            } else if (phone.isEmpty()) {
                showToast("Enter phone name.")
            } else if (email.isEmpty()) {
                showToast("Enter email address.")
            } else if (address.isEmpty()) {
                showToast("Enter full address.")
            } else if (state.isEmpty()) {
                showToast("Enter state.")
            } else if (gst.isEmpty()) {
                showToast("Enter gst.")
            } else {
                val request = Retailer.UpdateRetailerRequest(
                    address = address,
                    gst = gst,
                    name = name,
                    owner_name = ownername,
                    pass = "",
                    state = state,
                    id = id.toString()
                )
                callEditApi(request)
            }
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

    }

    private fun callViewApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.viewRetilerApi(id.toString())
        call.enqueue(object : Callback<Retailer.ViewRetailerResponse> {
            override fun onResponse(
                call: Call<Retailer.ViewRetailerResponse>,
                response: Response<Retailer.ViewRetailerResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            it.retailer.let { retailer ->
                                binding.retailerNameEt.setText(retailer.name)
                                binding.retailerOwnernameEt.setText(retailer.owner_name)
                                binding.retailerPhoneEt.setText(retailer.phone)
                                binding.retailerEmailEt.setText(retailer.email)
                                binding.retailerAddressEt.setText(retailer.address)
                                binding.retailerStateEt.setText(retailer.state)
                                binding.retailerGstEt.setText(retailer.gst)
                            }
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.ViewRetailerResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

    private fun callEditApi(request: Retailer.UpdateRetailerRequest) {
        binding.pb.show()
        val call = RetrofitInstance.apiService.editRetailerApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            showToast(it.message)
                            setResult(RESULT_OK, Intent())
                            finish()
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