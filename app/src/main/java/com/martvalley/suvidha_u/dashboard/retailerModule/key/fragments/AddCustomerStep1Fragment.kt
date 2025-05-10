package com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.retailerModule.RetailerViewModel
import com.martvalley.suvidha_u.dashboard.retailerModule.extra.ScannerActivity
import com.martvalley.suvidha_u.databinding.FragmentAddCustomerStep1Binding
import com.martvalley.suvidha_u.utils.bitmapToBase64
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCustomerStep1Fragment : Fragment(), ImageAnalysis.Analyzer {
    private var _binding: FragmentAddCustomerStep1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RetailerViewModel by activityViewModels()


    private val cameraPermission = Manifest.permission.CAMERA
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startScanner()
            }
        }

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .enableAllPotentialBarcodes()
        .build()
    val scanner = BarcodeScanning.getClient(options)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddCustomerStep1Binding.inflate(inflater, container, false)

        val type = activity?.intent?.getStringExtra("Value_Key")
        binding.keyName.text = type
        when(type){
            "Smart Key" -> {
                binding.keyDes.text = "Mobile FRP Protection"
            }
            requireContext().getString(R.string.superkey) -> {
                binding.keyDes.text = "Zero Touch Enrollment"
            }
            requireContext().getString(R.string.home_appliance) -> {
                binding.keyDes.text = "Install without reset device"
            }
            requireContext().getString(R.string.udhar) ->{
                binding.keyDes.text = ""
            }
        }

        binding.backTextView.setOnClickListener {
            activity?.finish()
        }

        binding.nextButton.setOnClickListener {
            if (validate()) {
                viewModel.name = binding.custNameEditText.text.toString()
                viewModel.imeiNum = "243242342342"
                viewModel.signedImg = binding.signaturePad.signatureBitmap.bitmapToBase64()
                findNavController().navigate(R.id.action_addCustomerStep1Fragment_to_addCustomer2Fragment)
            }
        }


        binding.scanButton.setOnClickListener {
            //startActivity(Intent(requireContext(),ScannerActivity::class.java))
        }

        binding.clearSign.setOnClickListener {
            binding.signaturePad.clear()
        }
        return binding.root
    }

    private fun requestCameraAndStartScanner() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startScanner()
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                requestPermissionLauncher.launch(cameraPermission)
            }
        }
    }

    private val cameraPermissionRequestLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted: proceed with opening the camera
                startScanner()
            } else {
                // Permission denied: inform the user to enable it through settings
                // Permission denied
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Permission required")
                    .setMessage("This application needs to access the camera to process barcodes if not enabled goto the app settings and enable the camera permission ")
                    .setPositiveButton("Ok") { _, _ ->
                        // Keep asking for permission until granted

                    }
                    .setCancelable(false)
                    .create()
                    .apply {
                        setCanceledOnTouchOutside(false)
                        show()
                    }
            }
        }

    private fun startScanner() {
        ScannerActivity.startScanner(requireContext()) { barcodes ->
            barcodes.forEach { barcode ->
                when (barcode.valueType) {
                    Barcode.TYPE_URL -> {
                        binding.imeiEditText.setText(barcode.url.toString())
                    }

                    Barcode.TYPE_CONTACT_INFO -> {
                        binding.imeiEditText.setText(barcode.contactInfo.toString())
                    }

                    else -> {
                        binding.imeiEditText.setText(barcode.rawValue.toString())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() {

        binding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched

            }

            override fun onSigned() {

            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })
    }

    private fun validate():Boolean {
        if (binding.custNameEditText.text.isEmpty()){
            Toast.makeText(requireContext(), "Enter Customer Name", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.imeiEditText.text.isEmpty()) {
            Toast.makeText(requireContext(), "Enter IEMI NUMBER", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.signaturePad.isEmpty){
            Toast.makeText(requireContext(), "Sign to proceed", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }



    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
        }
    }
}