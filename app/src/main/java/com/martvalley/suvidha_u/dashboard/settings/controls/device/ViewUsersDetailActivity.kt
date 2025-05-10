package com.martvalley.suvidha_u.dashboard.settings.controls.device

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityViewUsersDetailBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewUsersDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityViewUsersDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.text.text = "User's Data"
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = false
            withNetwork { callApi() }
        }

        withNetwork { callApi() }

        binding.viewOnMap.setOnClickListener {
            val latlong = binding.locValue.text.split(",")
            viewInGoogleMaps(latlong[0].split(":")[1], latlong[1].split(":")[1], this)
        }

    }

    private fun callApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getCustomerDeviceLastDataApi(
            intent.getStringExtra("id").toString()
        )
        call.enqueue(object : Callback<DeviceBasics.GetDeviceInfo> {
            override fun onResponse(
                call: Call<DeviceBasics.GetDeviceInfo>,
                response: Response<DeviceBasics.GetDeviceInfo>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        if (response.body()?.data == null) {
                            binding.noData.show()
                            binding.locTv.hide()
                            binding.mobileTv.hide()
                            binding.callTv.hide()
                            binding.locValue.hide()
                            binding.mobileValue.hide()
                            binding.callValue.hide()
                            binding.time.hide()
                            binding.viewOnMap.hide()
                        } else {
                            response.body()?.let {
                                it.data.location?.let {
                                    //binding.locValue.text = it.replace("{", "").replace("}", "")
                                }
                                it.data.mobile?.let {
                                    binding.mobileValue.text =
                                        it.replace("null", "---").removeSuffix(",")
                                }
                                it.data.call_list?.let {
                                    binding.callValue.text = it
                                }
                                it.data.updated_at?.let {
                                    binding.time.text =
                                        "Last updated at : ${it.convertISOTimeToDateTime()}"
                                }

                                if (it.data.location.toString().contains("lat")) {
                                    binding.viewOnMap.show()
                                } else binding.viewOnMap.hide()

                            }
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<DeviceBasics.GetDeviceInfo>, t: Throwable
            ) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })
    }

}