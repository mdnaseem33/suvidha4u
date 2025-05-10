package com.martvalley.suvidha_u.dashboard.settings.distributor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.settings.ChangePasswordActivity
import com.martvalley.suvidha_u.dashboard.settings.EditProfileActivity
import com.martvalley.suvidha_u.dashboard.settings.report.ReportActivity
import com.martvalley.suvidha_u.databinding.FragmentDistributerSettingBinding
import com.martvalley.suvidha_u.login.LoginActivity
import com.martvalley.suvidha_u.utils.SharedPref
import com.martvalley.suvidha_u.utils.openWhatsAppConversationUsingUri


class DistributerSettingFragment : Fragment() {
    private val binding by lazy { FragmentDistributerSettingBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.report.img.setImageResource(R.drawable.report)
        binding.password.img.setImageResource(R.drawable.change_password)
        binding.profile.img.setImageResource(R.drawable.edit_profile)
        binding.logout.img.setImageResource(R.drawable.logout)
        binding.support.img.setImageResource(R.drawable.live_support_img)

        binding.report.tv.text = "Report"
        binding.password.tv.text = "Change Password"
        binding.profile.tv.text = "Edit Profile"
        binding.logout.tv.text = "Logout"
        binding.support.tv.text = "Live Support!"

        binding.report.root.setOnClickListener {
            startActivity(Intent(requireContext(), ReportActivity::class.java))
        }

        binding.logout.root.setOnClickListener {
            SharedPref(requireContext()).clearSharedPreference()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }

        binding.support.root.setOnClickListener {
            context?.let { it1 -> openWhatsAppConversationUsingUri(it1, "+912269646511 ", "") }
        }

        binding.password.root.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }

        binding.profile.root.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }
}