package com.martvalley.suvidha_u.dashboard.people.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityUserQrBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserQrActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUserQrBinding.inflate(layoutInflater) }
    var toggle = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.text.text = "Customer Qr Code"
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.calender.hide()
        binding.toolbar.filter.hide()
        binding.toggleButton.setOnClickListener {
            toggle = !toggle
            Toggle()
        }
        if(intent.getIntExtra("type",0) == 3){
            //binding.toggleButton.visibility = View.VISIBLE
            binding.appQr.visibility = android.view.View.VISIBLE
            binding.qr.visibility = android.view.View.INVISIBLE
        }else{
            binding.appQr.visibility = android.view.View.INVISIBLE
            binding.qr.visibility = android.view.View.VISIBLE
        }
        withNetwork { callApi() }

    }

    private fun Toggle(){
        if(toggle){
            binding.appQr.visibility = android.view.View.INVISIBLE
            binding.qr.visibility = android.view.View.VISIBLE
            binding.toggleButton.text = "Show App QR"
        }else{
            binding.appQr.visibility = android.view.View.VISIBLE
            binding.qr.visibility = android.view.View.INVISIBLE
            binding.toggleButton.text = "Show Customer QR"
        }
    }


    private fun callApi() {
        binding.pb.show()
        val call =
            RetrofitInstance.apiService.getCustomerQRApi(intent.getStringExtra("id").toString())
        call.enqueue(object : Callback<User.QRResponse> {
            override fun onResponse(
                call: Call<User.QRResponse>, response: Response<User.QRResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            binding.qr.setImageBitmap(it.qr.base64ToBitmap())
                            binding.appQr.loadImage(Constants.BASEURL+"storage/"+it.app)
                            binding.id.text = intent.getStringExtra("id").toString()
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<User.QRResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

}