package com.martvalley.suvidha_u.dashboard.people.retailer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityCreditRetailerBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreditRetailerActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCreditRetailerBinding.inflate(layoutInflater) }
    private val id by lazy { intent.getIntExtra("id", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backImage.setOnClickListener {
            onBackPressed()
        }

        binding.btn.setOnClickListener {
            val amt = binding.emailEt.text.trim().toString()
            val remarks = binding.remarksEt.text.trim().toString()
            if (amt.isEmpty()) {
                showToast("Please enter amount.")
            }
            else if (remarks.isEmpty()){
                showToast("Please enter remarks.")
            } else {
                withNetwork { callCreditApi(amt, remarks) }
            }
        }
    }

    private fun callCreditApi(amt: String, remarks: String) {
        binding.pb.show()
        val request = Retailer.CreditRetailerRequest(amt, id.toString(), remarks)
        val call = RetrofitInstance.apiService.creditRetailerApi(request)
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
                            binding.remarksEt.text.clear()
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