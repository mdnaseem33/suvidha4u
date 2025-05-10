package com.martvalley.suvidha_u.dashboard.people.retailer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.settings.Settings
import com.martvalley.suvidha_u.databinding.ActivityChangeRetailerPasswordBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.logd
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeRetailerPassword : AppCompatActivity() {
    val binding by lazy { ActivityChangeRetailerPasswordBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val id = intent.getIntExtra("id", 0)
        id.logd("extra id")
        if(id == 0){
            showToast("Invalid Retailer")
            finish()
        }
        binding.backImage.setOnClickListener {
            onBackPressed()
        }

        binding.saveButton.setOnClickListener {
            binding.newPass.clearFocus()
            binding.confirmNewPass.clearFocus()

            val newpass = binding.newPass.text.toString().trim()
            val confirmpass = binding.confirmNewPass.text.toString().trim()

            if (newpass.isEmpty()) {
                showToast("Please enter new password.")
            } else if (confirmpass.isEmpty()) {
                showToast("Please enter confirm password.")
            } else if (newpass != confirmpass) {
                showToast("Confirm password does not match with new password.")
            } else {
                withNetwork {
                    val request = Settings.PasswordChangeRetailerRequest(
                        confirm_password = confirmpass,
                        password = newpass,
                        id = id.toString()
                    )
                    callPassChangeApi(request)
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun callPassChangeApi(request: Settings.PasswordChangeRetailerRequest) {
        binding.pb.show()

        val call = RetrofitInstance.apiService.passwordChangeReatiler(request)
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

                            if (it.status == 200) {
                                binding.newPass.text.clear()
                                binding.confirmNewPass.text.clear()
                                finish()
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
}