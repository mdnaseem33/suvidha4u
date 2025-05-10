package com.martvalley.suvidha_u.dashboard.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.dashboard.settings.report.ReportActivity
import com.martvalley.suvidha_u.databinding.FragmentSettingBinding
import com.martvalley.suvidha_u.login.LoginActivity
import com.martvalley.suvidha_u.utils.SharedPref


class SettingFragment : Fragment() {

    private val binding by lazy { FragmentSettingBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.report.img.setImageResource(R.drawable.report)
        binding.msg.img.setImageResource(R.drawable.lock_messages)
        binding.wallpaper.img.setImageResource(R.drawable.wallpaper_customize)
//        binding.hub.img.setImageResource(R.drawable.marketing_hub)
        binding.password.img.setImageResource(R.drawable.change_password)
        binding.profile.img.setImageResource(R.drawable.edit_profile)
        binding.logout.img.setImageResource(R.drawable.logout)
        binding.support.img.setImageResource(R.drawable.live_support_img)
        binding.control.img.setImageResource(R.drawable.device)

        binding.report.tv.text = "Report"
        binding.msg.tv.text = "Lock Message"
        binding.wallpaper.tv.text = "Wallpaper Customize"
        //binding.hub.tv.text = "Marketing Hub"
        binding.password.tv.text = "Change Password"
        binding.profile.tv.text = "Edit Profile"
        binding.logout.tv.text = "Logout"
        binding.support.tv.text = "Live Support!"
        binding.control.tv.text = "Control"

        binding.report.root.setOnClickListener {
            startActivity(Intent(requireContext(), ReportActivity::class.java))
        }

        binding.logout.root.setOnClickListener {
            SharedPref(requireContext()).clearSharedPreference()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.control.root.setOnClickListener {
            startActivity(Intent(requireContext(), ControlsActivity::class.java))
        }
    }

}