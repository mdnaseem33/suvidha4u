package com.martvalley.suvidha_u.forgot_pass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityForgotPasswordBinding
import com.martvalley.suvidha_u.login.LoginActivity
import com.martvalley.suvidha_u.utils.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    private val binding by lazy { ActivityForgotPasswordBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backImage.setOnClickListener {
            finish()
        }
        binding.backTv.setOnClickListener {
            finish()
        }

        binding.btn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()

            if (email.isEmpty()) {
                showToast("Please enter email .")
            } else if (email.isEmpty()) {
                showToast("Please enter email address.")
            } else if (email.contains("@") && !email.isEmailValid()) {
                showToast("Please enter a valid email")
            } else if (!email.contains("@") && !email.isPhoneValid()) {
                showToast("Please enter a valid phone number")
            } else {
                withNetwork { callForgotPasswordApi(email) }
            }
        }

    }

    private fun callForgotPasswordApi(email: String) {
        binding.pb.show()

        val req = ForgotPass.Request(email)

        val call = RetrofitInstance.apiService.forgotPasswordApi(req)
        call.enqueue(object : Callback<ForgotPass.Response> {
            override fun onResponse(
                call: Call<ForgotPass.Response>, response: Response<ForgotPass.Response>
            ) {
                binding.pb.hide()

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            showToast(it.message)
                            val intent =
                                Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    422 -> {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string()).getJSONObject("errors")
                            showToast(jObjError.getJSONArray("email").get(0).toString())
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                e.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<ForgotPass.Response>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

}