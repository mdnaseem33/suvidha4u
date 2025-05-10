package com.martvalley.suvidha_u.dashboard.people.retailer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityAntiCreditBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AntiCreditActivity : AppCompatActivity() {

    val binding by lazy { ActivityAntiCreditBinding.inflate(layoutInflater) }
    private val id by lazy { intent.getIntExtra("id", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backImage.setOnClickListener {
            onBackPressed()
        }

        binding.btn.setOnClickListener {
            val amt = binding.emailEt.text.trim().toString()
            if (amt.isEmpty()) {
                showToast("Please enter amount.")
            } else {
                withNetwork { callCreditApi(amt, binding.description.text.trim().toString()) }
            }

        }
    }

    private fun callCreditApi(amt: String, description: String?=null) {
        binding.pb.show()
        val request = Retailer.AntiCreditRetailerRequest(amt, id.toString(), description)
        val call = RetrofitInstance.apiService.antiCreditRetailerApi(request)
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
                            binding.emailEt.text.clear()
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