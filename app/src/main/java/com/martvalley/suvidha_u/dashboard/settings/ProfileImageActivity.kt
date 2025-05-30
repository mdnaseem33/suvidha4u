package com.martvalley.suvidha_u.dashboard.settings

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments.CameraFragment
import com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments.onImageCaptureListener
import com.martvalley.suvidha_u.databinding.ActivityProfileImageBinding
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.FileUtils
import com.martvalley.suvidha_u.utils.compressImageFromUriAndGetBase64
import com.martvalley.suvidha_u.utils.getBase64String
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.loadImage
import com.martvalley.suvidha_u.utils.logd
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileImageActivity : AppCompatActivity(), onImageCaptureListener {
    private val binding: ActivityProfileImageBinding by lazy {
        ActivityProfileImageBinding.inflate(layoutInflater)
    }
    private var request: Settings.SaveProfileRequest? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { uri ->
            val path = FileUtils.getRealPath(this, uri)
            val path_list = path?.split("/")
            try {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment != null) {
                    supportFragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            if (path != null) {
                binding.img.loadImage(path)
            }

            compressImageFromUriAndGetBase64(uri){ media->
                request = Settings.SaveProfileRequest(media!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        MainApplication.authData?.image?.let {
            it.logd()
            if (it != "") {
//                binding.chooseButton.text = it
                binding.img.loadImage(Constants.BASEURL + "storage/public/$it")
            }
        }

        binding.chooseButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CameraFragment())
                .addToBackStack(null) // Optional: allows user to go back
                .commit()
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

    private fun callUploadMediaApi(request: Settings.SaveProfileRequest) {
        request.logd()
        binding.pb.show()
        val call = RetrofitInstance.apiService.saveProfileImage(request)
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
    override fun onImageCapture(uri: Uri) {
        binding.img.setImageURI(uri)
        uri.getBase64String(contentResolver)?.let { media ->
            request = Settings.SaveProfileRequest(media)

        }
    }

}