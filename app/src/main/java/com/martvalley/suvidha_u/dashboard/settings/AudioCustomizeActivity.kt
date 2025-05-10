package com.martvalley.suvidha_u.dashboard.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.MimeTypes
import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityAudioCustomizeBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AudioCustomizeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAudioCustomizeBinding.inflate(layoutInflater) }

    private var request: Settings.SetAudioRequest? = null

    private lateinit var exoPlayer: ExoPlayer

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { uri ->
            val path = FileUtils.getRealPath(this, uri)
            val path_list = path?.split("/")

            if (path != null) {
                binding.chooseButton.text = path_list?.get(path_list.size - 1)
                val mediaItem =
                    MediaItem.Builder().setUri(path).setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                    .build()
                exoPlayer.setMediaItem(mediaItem)
            }

            uri.getBase64String(contentResolver)?.let { media ->
                request = Settings.SetAudioRequest("$media")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        MainApplication.authData?.audio?.let {
            it.logd()
            if (it != "") {
                binding.chooseButton.text = it
            }
        }

        val url = Constants.BASEURL + "storage/${MainApplication.authData?.audio}"
        exoPlayer = ExoPlayer.Builder(this).build().apply {
            binding.player.player = this
            val mediaItem =
                MediaItem.Builder().setUri(url).setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                    .build()
            setMediaItem(mediaItem)
            prepare()
        }


        binding.chooseButton.setOnClickListener {
            pickImage.launch("audio/*")
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

    private fun callUploadMediaApi(request: Settings.SetAudioRequest) {
        request.logd()
        binding.pb.show()
        val call = RetrofitInstance.apiService.setAudioApi(request)
        call.enqueue(object : Callback<Settings.SetAudioResponse> {
            override fun onResponse(
                call: Call<Settings.SetAudioResponse>,
                response: Response<Settings.SetAudioResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            showToast(it.message)
                            MainApplication.authData = it.retailer
                            binding.chooseButton.text = it.retailer.audio
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Settings.SetAudioResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.stop()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

}