package com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.databinding.FragmentSplashKeyBinding


class SplashKeyFragment : Fragment() {
    private var _binding: FragmentSplashKeyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashKeyBinding.inflate(inflater,container,false)

        val type = activity?.intent?.getStringExtra("Value_Key")
        binding.keyName.text = type

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (type != null) {
                if (type.contains("Udhar")){
                    findNavController().navigate(R.id.action_splashKeyFragment_to_udharFragment)
                } else {
                    findNavController().navigate(R.id.action_splashKeyFragment_to_addCustomerStep1Fragment)
                }
            }
        }, 1000)
       // action_splashKeyFragment_to_udharFragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}