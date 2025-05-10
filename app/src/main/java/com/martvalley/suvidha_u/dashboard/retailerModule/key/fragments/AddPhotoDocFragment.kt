package com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.retailerModule.RetailerViewModel
import com.martvalley.suvidha_u.databinding.FragmentAddPhotoDocBinding
import com.martvalley.suvidha_u.utils.getBase64String
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPhotoDocFragment : Fragment() {
    private var _binding: FragmentAddPhotoDocBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RetailerViewModel by activityViewModels()
    var photo = false
    var photid = false
    var photoIdback = false
    var refid = false
    var refidback = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddPhotoDocBinding.inflate(inflater, container, false)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                uri.getBase64String(requireActivity().contentResolver)?.let { media ->
                    if (photo) {
                        viewModel.customerPhoto = ("data:image/png;base64,$media")
                        binding.photoChecked.setImageResource(R.drawable.checked_green)
                        binding.photoText.text = resources.getText(R.string.update)
                        photo = false
                    } else if (photid) {
                        viewModel.customerIDFront = ("data:image/png;base64,$media")
                        binding.idChecked.setImageResource(R.drawable.checked_green)
                        binding.idFrontText.text = resources.getText(R.string.update)
                        photid = false
                    }  else if (photoIdback) {
                        viewModel.customerIDBack = ("data:image/png;base64,$media")
                        binding.idBackChecked.setImageResource(R.drawable.checked_green)
                        binding.idbackText.text = resources.getText(R.string.update)
                        photoIdback = false
                    }  else if (refid) {
                        viewModel.referPhotoFront = ("data:image/png;base64,$media")
                        binding.referChecked.setImageResource(R.drawable.checked_green)
                        binding.refFrontText.text = resources.getText(R.string.update)
                        refid = false
                    } else if (refidback) {
                        viewModel.referPhotoBack = ("data:image/png;base64,$media")
                        binding.referBackChecked.setImageResource(R.drawable.checked_green)
                        binding.refIdBackText.text = resources.getText(R.string.update)
                        refidback = false
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Something went wrong please try again!!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_addPhotoDocFragment_to_registeredFragment)
        }

        binding.photCardView.setOnClickListener {
            photo = true
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.idCardView.setOnClickListener {
            photid = true
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.idBackCardView.setOnClickListener {
            photoIdback = true
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.referCardView.setOnClickListener {
            refid = true
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.referBackCardView.setOnClickListener {
            refidback = true
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_addPhotoDocFragment_to_registeredFragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}