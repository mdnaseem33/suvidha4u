package com.martvalley.suvidha_u.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.retailerModule.DashBoardNewActivity
import com.martvalley.suvidha_u.databinding.ActivityLoginBinding
import com.martvalley.suvidha_u.forgot_pass.ForgotPasswordActivity
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.SharedPref
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.invisible
import com.martvalley.suvidha_u.utils.isEmailValid
import com.martvalley.suvidha_u.utils.isPhoneValid
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import com.google.gson.Gson
import com.martvalley.suvidha_u.R
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private var isPasswordVisible = false
    var selected = Constants.RETAILER
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backImage.setOnClickListener {
            onBackPressed()
        }

        binding.btn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()

            if (email.isEmpty() && pass.isEmpty()) {
                showToast("Please enter email and password.")
            } else if (email.isEmpty()) {
                showToast("Please enter email address.")
            } else if (email.contains("@") && !email.isEmailValid()) {
                showToast("Please enter a valid email")
            } else if (!email.contains("@") && !email.isPhoneValid()) {
                showToast("Please enter a valid phone number")
            } else if (pass.isEmpty()) {
                showToast("Please enter password.")
            } else {
                withNetwork { callLoginApi(email, pass) }
            }
        }

        binding.retailerTv.setOnClickListener {
            binding.retailerDivider.show()
            binding.distDivider.invisible()
            selected = Constants.RETAILER

        }

        binding.distribbutorTv.setOnClickListener {
            binding.retailerDivider.invisible()
            binding.distDivider.show()
            selected = Constants.DISTRIBUTOR
        }

        binding.forgotpassTv.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.passEt.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // drawableEnd is at index 2
                if (event.rawX >= (binding.passEt.right - binding.passEt.compoundDrawables[drawableEnd].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.passEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.passEt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24_close, 0)
        } else {
            binding.passEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passEt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
        }
        isPasswordVisible = !isPasswordVisible
        binding.passEt.setSelection(binding.passEt.text.length) // Move cursor to the end
    }


    private fun callLoginApi(email: String, pass: String) {
        binding.pb.show()
        val role = if (selected == Constants.RETAILER) 3 else 2

        val req = Login.LoginRequest(email, pass, role)
        Log.d("RequestBody", Gson().toJson(req))
        val call = RetrofitInstance.apiService.loginApi(req)
        call.enqueue(object : Callback<Login.LoginResponse> {
            override fun onResponse(
                call: Call<Login.LoginResponse>, response: Response<Login.LoginResponse>
            ) {
                binding.pb.hide()

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            SharedPref(this@LoginActivity).save(Constants.AUTH_KEY, it.access_token)
                            SharedPref(this@LoginActivity).save(Constants.IS_LOGGED_IN, true)
                            SharedPref(this@LoginActivity).save(Constants.ROLE, role)
                            SharedPref(this@LoginActivity).save(Constants.EMAIL, email)



                            if (selected == Constants.RETAILER) {
                                val intent = Intent(this@LoginActivity, DashBoardNewActivity::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@LoginActivity, DashBoardNewActivity::class.java)
                                startActivity(intent)
                            }
                            finish()
                        }
                    }
                    401 -> {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            showToast(jObjError.getString("message"))
                        } catch (e: Exception) {
                            Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Login.LoginResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }
}