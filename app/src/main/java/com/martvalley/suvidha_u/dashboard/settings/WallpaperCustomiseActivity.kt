package com.martvalley.suvidha_u.dashboard.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityWallpaperCustomiseBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WallpaperCustomiseActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWallpaperCustomiseBinding.inflate(layoutInflater) }

    private var request: Settings.SaveWallpaperRequest? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { uri ->
            val path = FileUtils.getRealPath(this, uri)
            val path_list = path?.split("/")

            if (path != null) {
//                binding.chooseButton.text = path_list?.get(path_list.size - 1)
                binding.img.loadImage(path)
            }
            uri.getBase64String(contentResolver)?.let { media ->
                request = Settings.SaveWallpaperRequest(media)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        MainApplication.authData?.wallpaper?.let {
            it.logd()
            if (it != "") {
//                binding.chooseButton.text = it
                binding.img.loadImage(Constants.BASEURL + "storage/$it")
            }
        }

        binding.chooseButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }

        binding.saveButton.setOnClickListener {
            request?.let { it1 -> callUploadMediaApi(it1) }
        }

        binding.backImage.setOnClickListener {
            onBackPressed()
        }

    }

    private fun callUploadMediaApi(request: Settings.SaveWallpaperRequest) {
        request.logd()
        binding.pb.show()
        val call = RetrofitInstance.apiService.saveWallpaperApi(request)
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