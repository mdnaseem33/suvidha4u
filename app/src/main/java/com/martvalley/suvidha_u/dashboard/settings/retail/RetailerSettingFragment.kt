package com.martvalley.suvidha_u.dashboard.settings.retail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.guardanis.applock.AppLock
import com.guardanis.applock.activities.LockCreationActivity
import com.guardanis.applock.activities.UnlockActivity
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.retailerModule.ChatBotActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.DashBoardNewActivity
import com.martvalley.suvidha_u.dashboard.settings.*
import com.martvalley.suvidha_u.databinding.FragmentRetailerSettingBinding
import com.martvalley.suvidha_u.login.Auth
import com.martvalley.suvidha_u.login.LoginActivity
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.SharedPref
import com.martvalley.suvidha_u.utils.loadImage
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetailerSettingFragment : Fragment() {

    private val binding by lazy { FragmentRetailerSettingBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.msg.img.setImageResource(R.drawable.img)
        binding.wallpaper.img.setImageResource(R.drawable.wallpaper_customize)
        binding.password.img.setImageResource(R.drawable.change_password)
        binding.profile.img.setImageResource(R.drawable.edit_profile)
        binding.logout.img.setImageResource(R.drawable.logout)
        binding.support.img.setImageResource(R.drawable.live_support_img)
        binding.loanPrefix.img.setImageResource(R.drawable.baseline_assured_workload_24)
        binding.frp.img.setImageResource(R.drawable.baseline_alternate_email_24)
        binding.switcher.img.setImageResource(R.drawable.baseline_swap_horiz_24)
        binding.changeLock.img.setImageResource(R.drawable.baseline_fingerprint_24)
        binding.switcher.root.visibility = View.GONE
        binding.msg.tv.text = "Change Profile Image"
        binding.wallpaper.tv.text = "Wallpaper Customize"
        binding.password.tv.text = "Change Password"
        binding.profile.tv.text = "Edit Profile"
        binding.logout.tv.text = "Logout"
        binding.support.tv.text = "Live Support!"
        binding.frp.tv.text = "Custom FRP Email"
        binding.qrCode.tv.text = "Payment QR"
        binding.loanPrefix.tv.text = "Loan Prefix"
        binding.changeLock.tv.text = "Change Lock"
        //binding.audio.tv.text = "Audio Customize"

        binding.support.root.setOnClickListener {
            startActivity(Intent(requireContext(), ChatBotActivity::class.java))
        }

        binding.msg.root.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileImageActivity::class.java))
        }
        binding.report.imageViewProfile.setOnClickListener{
            startActivity(Intent(requireContext(), ProfileImageActivity::class.java))
        }
        binding.wallpaper.root.setOnClickListener {
            startActivity(Intent(requireContext(), WallpaperCustomiseActivity::class.java))
        }

        binding.logout.root.setOnClickListener {
            SharedPref(requireContext()).clearSharedPreference()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }

        binding.password.root.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }
        binding.profile.root.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.changeLock.root.setOnClickListener {
            // User is enrolled, proceed to unlock
            val intent = Intent(requireContext(), UnlockActivity::class.java)
            startActivityForResult(intent, AppLock.REQUEST_CODE_UNLOCK)
        }

        typeUI()
        callAuthApi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            showToast( "Lock Changed Successfully")
        }else if (requestCode == AppLock.REQUEST_CODE_UNLOCK && resultCode == Activity.RESULT_OK) {
            // User is not enrolled, you can prompt to create a lock
            val intent = Intent(requireContext(), LockCreationActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }

    private fun typeUI(){
        if(SharedPref(requireContext()).getValueInt(Constants.SUB_ROLE) == 2 || SharedPref(requireContext()).getValueInt(Constants.IS_RETAILER) == 2){
            binding.frp.root.visibility = View.GONE
            binding.loanPrefix.root.visibility = View.GONE
            binding.wallpaper.root.visibility = View.GONE
            binding.wallpaper.root.visibility = View.GONE
            binding.report.plusMember.visibility = View.GONE

        }else{
            binding.frp.root.setOnClickListener {
                startActivity(Intent(requireContext(), FrpEmailActivity::class.java))
            }
            binding.loanPrefix.root.setOnClickListener {
                startActivity(Intent(requireContext(), LoanPrefixActivity::class.java))
            }

            binding.qrCode.root.setOnClickListener {
                startActivity(Intent(requireContext(), PaymentQr::class.java))
            }
        }

        if(SharedPref(requireContext()).getValueInt(Constants.SUB_ROLE) == 3){
            if(SharedPref(requireContext()).getValueInt(Constants.IS_RETAILER) == 2){
                binding.switcher.tv.text = "Switch To Retailer"
                binding.switcher.root.setOnClickListener {
                    SharedPref(requireContext()).save(Constants.IS_RETAILER, 1)
                    startActivity(Intent(requireContext(), DashBoardNewActivity::class.java))

                }
            }else if(SharedPref(requireContext()).getValueInt(Constants.IS_RETAILER) == 1){
                binding.switcher.tv.text = "Switch To Distributor"
                binding.switcher.root.setOnClickListener {
                    SharedPref(requireContext()).save(Constants.IS_RETAILER, 2)
                    startActivity(Intent(requireContext(), DashBoardNewActivity::class.java))

                }
            }else{
                SharedPref(requireContext()).save(Constants.IS_RETAILER, 2)
                binding.switcher.tv.text = "Switch To Distributor"
                binding.switcher.root.setOnClickListener {
                    SharedPref(requireContext()).save(Constants.IS_RETAILER, 2)
                    startActivity(Intent(requireContext(), DashBoardNewActivity::class.java))

                }
            }

            binding.switcher.root.visibility = View.VISIBLE
        }
    }

    private fun callAuthApi() {
        val call = RetrofitInstance.apiService.getAuthApi()
        call.enqueue(object : Callback<Auth.AuthResponse> {
            override fun onResponse(
                call: Call<Auth.AuthResponse>, response: Response<Auth.AuthResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            binding.report.userNameTextView.text = it.name
                            binding.report.userPhoneTextView.text = it.phone
                            binding.report.plusMember.text = it.member
                            if (it.image != null) {
                                val imageUrl = Constants.BASEURL + "storage/public/" +it.image
                                Glide.with(requireContext())
                                    .load(imageUrl)
                                    .into(binding.report.imageViewProfile);
                                binding.msg.img.loadImage(imageUrl)
                            }

                        }
                    }
                    else -> {
                        requireContext().showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Auth.AuthResponse>, t: Throwable) {
                requireContext().showApiErrorToast()
            }

        })

    }

    override fun onResume() {
        super.onResume()
        callAuthApi()
    }
}