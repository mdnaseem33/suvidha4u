package com.martvalley.suvidha_u.dashboard.settings.retail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.settings.Settings
import com.martvalley.suvidha_u.databinding.ActivityFrpEmailBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.logd
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FrpEmailActivity : AppCompatActivity() {
    val binding by lazy { ActivityFrpEmailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MainApplication.authData?.frp_email?.let {
            it.logd()
            if (it != "") {
                binding.msgEt.setText(it)
            }
        }

        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }

        binding.backImage.setOnClickListener {
            this.onBackPressed()
        }

        binding.saveButton.setOnClickListener {
            val msg = binding.msgEt.text.trim().toString()
            if (msg.isEmpty()) {
                showToast("Enter Frp Email")
            } else {
                withNetwork { callUploadMediaApi( Settings.SaveFrpEmailRequest(msg) ) }
            }
        }
    }
    private fun callUploadMediaApi(request: Settings.SaveFrpEmailRequest) {
        request.logd()
        binding.pb.show()
        val call = RetrofitInstance.apiService.savefrpEmailApi(request)
        call.enqueue(object : Callback<Settings.SaveWallpaperResponse> {
            override fun onResponse(
                call: Call<Settings.SaveWallpaperResponse>,
                response: Response<Settings.SaveWallpaperResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            showToast(it.message)
                            MainApplication.authData = it.retailer
                            MainApplication.authData.logd()
                        }
                    }

                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Settings.SaveWallpaperResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }
}