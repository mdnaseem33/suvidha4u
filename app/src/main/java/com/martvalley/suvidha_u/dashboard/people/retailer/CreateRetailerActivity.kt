package com.martvalley.suvidha_u.dashboard.people.retailer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityCreateRetailerBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateRetailerActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCreateRetailerBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.text.text = "Add Retailer"
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

        binding.saveButton.setOnClickListener {
            val address = binding.retailerAddressEt.text.trim().toString()
            val email = binding.retailerEmailEt.text.trim().toString()
            val gst = binding.retailerGstEt.text.trim().toString()
            val name = binding.retailerNameEt.text.trim().toString()
            val ownername = binding.retailerOwnernameEt.text.trim().toString()
            val state = binding.retailerStateEt.text.trim().toString()
            val phone = binding.retailerPhoneEt.text.trim().toString()
            val pass = binding.retailerPassword.text.trim().toString()
            val memeber = binding.retailerMember.text.trim().toString()
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
            } else if (pass.isEmpty() && pass.length <= 7) {
                showToast("Enter password.")
            } else {
                val request = Retailer.AddRetailerRequest(
                    address = address,
                    email = email,
                    gst = gst,
                    name = name,
                    owner_name = ownername,
                    pass = pass,
                    phone = phone,
                    state = state,
                    member = memeber
                )
                callAddApi(request)
            }
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

    }

    private fun callAddApi(request: Retailer.AddRetailerRequest) {
        binding.pb.show()
        val call = RetrofitInstance.apiService.addRetailerApi(request)
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

                    422 -> {
                        val gson = Gson()
                        val type = object : TypeToken<Retailer.AddRetailerErrorResponse>() {}.type
                        val errorResponse: Retailer.AddRetailerErrorResponse? =
                            gson.fromJson(response.errorBody()!!.charStream(), type)

                        if (errorResponse?.errors?.email == null && errorResponse?.errors?.phone != null) {
                            showToast(errorResponse.errors.phone[0])
                        } else if (errorResponse?.errors?.phone == null && errorResponse?.errors?.email != null) {
                            showToast(errorResponse.errors.email[0])
                        } else if (errorResponse?.errors?.email != null && errorResponse?.errors?.phone != null) {
                            showToast("Phone and email have already been taken.")
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