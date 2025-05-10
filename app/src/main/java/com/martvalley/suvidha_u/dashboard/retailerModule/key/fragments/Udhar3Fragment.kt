package com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.martvalley.suvidha_u.databinding.FragmentUdhar3Binding


class Udhar3Fragment : Fragment() {
    private var _binding: FragmentUdhar3Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUdhar3Binding.inflate(inflater, container, false)

        binding.backTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.nextButton.setOnClickListener {
            activity?.finish()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}