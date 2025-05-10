package com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.home.Dashboard
import com.martvalley.suvidha_u.dashboard.home.retailer.CreateUserActivity.Companion.bitmap
import com.martvalley.suvidha_u.dashboard.people.user.User
import com.martvalley.suvidha_u.dashboard.retailerModule.RetailerViewModel
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.SmartKey
import com.martvalley.suvidha_u.databinding.FragmentRegisteredBinding
import com.martvalley.suvidha_u.utils.base64ToBitmap
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.logd
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.withNetwork
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class RegisteredFragment : Fragment() {
    private var _binding: FragmentRegisteredBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RetailerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisteredBinding.inflate(inflater,container,false)

        binding.backTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.exitButton.setOnClickListener {
            activity?.finish()
        }

        val request = SmartKey(
            bank_id = viewModel.bankid,
            brand_id = 1,
            customer_id_back = viewModel.customerIDBack,
            customer_id_front = viewModel.customerIDFront,
            down_payment = viewModel.downPayment,
            image = viewModel.customerPhoto,
            imei1 = viewModel.imeiNum,
            imei2 = viewModel.imeiNum,
            loan_amount = viewModel.loanAmount,
            loan_date = viewModel.loanDate,
            loan_number = viewModel.loanNumber,
            mobile = viewModel.mobileNumber,
            model_number = viewModel.modelNo,
            name = viewModel.name,
            payment_type = viewModel.emiType,
            payment_term = viewModel.emiFrequency,
            product_price = viewModel.productPrice,
            reference_name = viewModel.referenceName,
            reference_number = "RS12344",
            reference_id_back = viewModel.referPhotoBack,
            reference_id_front = viewModel.referPhotoFront,
            sign = viewModel.signedImg,
            tenure = 12


        )
        withNetwork { callApi(request) }

        return binding.root
    }

    private fun callApi(request: SmartKey) {
        request.logd()
        binding.pb.show()
        val call = RetrofitInstance.apiService.createSmartKey(request)
        call.enqueue(object : Callback<Dashboard.CreateUserResponse> {
            override fun onResponse(
                call: Call<Dashboard.CreateUserResponse>,
                response: Response<Dashboard.CreateUserResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            withNetwork { callApiImage(it.customer_id.toString()) }
                        }
                    }
                    else -> {
                        requireContext().showApiErrorToast()
                        bitmap = null
                    }
                }
            }

            override fun onFailure(call: Call<Dashboard.CreateUserResponse>, t: Throwable) {
                binding.pb.hide()
                requireContext().showApiErrorToast()
            }

        })

    }

    private fun callApiImage(id:String) {
        binding.pb.show()
        val call =
            RetrofitInstance.apiService.getCustomerQRApi(id)
        call.enqueue(object : Callback<User.QRResponse> {
            override fun onResponse(
                call: Call<User.QRResponse>, response: Response<User.QRResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            try {
                                binding.qrCodeImage.setImageBitmap(it.qr.base64ToBitmap())
                                binding.qrCodeNumber.text = id
                            }catch (e:Exception){
                                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else -> {
                        requireContext().showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<User.QRResponse>, t: Throwable) {
                binding.pb.hide()
                requireContext().showApiErrorToast()
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}