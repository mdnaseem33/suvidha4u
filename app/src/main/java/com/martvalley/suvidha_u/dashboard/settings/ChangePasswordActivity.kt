package com.martvalley.suvidha_u.dashboard.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.databinding.ActivityChangePasswordBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChangePasswordBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backImage.setOnClickListener {
            onBackPressed()
        }

        binding.saveButton.setOnClickListener {
            binding.currentPass.clearFocus()
            binding.newPass.clearFocus()
            binding.confirmNewPass.clearFocus()

            val oldpass = binding.currentPass.text.toString().trim()
            val newpass = binding.newPass.text.toString().trim()
            val confirmpass = binding.confirmNewPass.text.toString().trim()

            if (oldpass.isEmpty()) {
                showToast("Please enter your current password.")
            } else if (newpass.isEmpty()) {
                showToast("Please enter new password.")
            } else if (confirmpass.isEmpty()) {
                showToast("Please enter confirm password.")
            } else if (newpass != confirmpass) {
                showToast("Confirm password does not match with new password.")
            } else {
                withNetwork {
                    val request = Settings.PasswordChangeRequest(
                        confirm_password = confirmpass,
                        new_password = newpass,
                        old_password = oldpass
                    )
                    callPassChangeApi(request)
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun callPassChangeApi(request: Settings.PasswordChangeRequest) {
        binding.pb.show()

        val call = RetrofitInstance.apiService.passwordChangeApi(request)
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
                                binding.currentPass.text.clear()
                                binding.newPass.text.clear()
                                binding.confirmNewPass.text.clear()
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