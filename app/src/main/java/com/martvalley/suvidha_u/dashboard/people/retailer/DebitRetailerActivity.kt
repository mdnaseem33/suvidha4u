package com.martvalley.suvidha_u.dashboard.people.retailer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityDebitRetailerBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebitRetailerActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDebitRetailerBinding.inflate(layoutInflater) }
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
                withNetwork { callApi(amt) }
            }
        }
    }

    private fun callApi(amt: String) {
        binding.pb.show()
        val request = Retailer.CreditRetailerRequest(amt, id.toString())
        val call = RetrofitInstance.apiService.debitRetailerApi(request)
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