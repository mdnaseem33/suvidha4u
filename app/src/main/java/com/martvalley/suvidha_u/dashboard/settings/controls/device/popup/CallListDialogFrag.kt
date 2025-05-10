package com.martvalley.suvidha_u.dashboard.settings.controls.device.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.settings.controls.device.DeviceBasics
import com.martvalley.suvidha_u.databinding.CallListPopupBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallListDialogFrag: DialogFragment() {
    private var _binding: CallListPopupBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = CallListPopupBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

    }
     fun callApi(id: String) {
       // binding.pb.show()
        val call = RetrofitInstance.apiService.getCustomerDeviceLastDataApi(id)
        call.enqueue(object : Callback<DeviceBasics.GetDeviceInfo> {
            override fun onResponse(
                call: Call<DeviceBasics.GetDeviceInfo>,
                response: Response<DeviceBasics.GetDeviceInfo>
            ) {
               // binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        if (response.body()?.data == null) {
                            binding.callValue.show()
//                            binding.locTv.hide()
//                            binding.mobileTv.hide()
//                            binding.callTv.hide()
//                            binding.locValue.hide()
//                            binding.mobileValue.hide()
                            binding.callValue.hide()
//                            binding.time.hide()
//                            binding.viewOnMap.hide()
                        } else {
                            response.body()?.let {
//                                it.data.location?.let {
//                                    binding.locValue.text = it.replace("{", "").replace("}", "")
//                                }
//                                it.data.mobile?.let {
//                                    binding.mobileValue.text =
//                                        it.replace("null", "---").removeSuffix(",")
//                                }
                                it.data.call_list?.let {
                                    binding.callValue.text = it
                                }
//                                it.data.updated_at?.let {
//                                    binding.time.text =
//                                        "Last updated at : ${it.convertISOTimeToDateTime()}"
//                                }
//
//                                if (it.data.location.toString().contains("lat")) {
//                                    binding.viewOnMap.show()
//                                } else binding.viewOnMap.hide()

                            }
                        }
                    }
                    else -> {
                        //requireContext().showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<DeviceBasics.GetDeviceInfo>, t: Throwable
            ) {
               // binding.pb.hide()
               // requireContext().showApiErrorToast()
            }

        })
    }
}