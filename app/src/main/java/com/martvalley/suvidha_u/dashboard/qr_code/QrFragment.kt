package com.martvalley.suvidha_u.dashboard.qr_code

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.martvalley.suvidha_u.databinding.FragmentQrBinding


class QrFragment : Fragment() {
    val binding by lazy { FragmentQrBinding.inflate(layoutInflater) }
//    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_DENIED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.CAMERA),
//                123
//            )
//        } else {
//            startScanning()
//        }


        binding.qr.setImageBitmap(getQrCodeBitmap())

//        binding.name.text = MainApplication.authData?.name.toString()

    }

    private fun getQrCodeBitmap(
    ): Bitmap {
//        val qrCodeContent = "emi,${SharedPref(requireContext()).getValueInt(Constants.USERID)}"
        val qrCodeContent = "https://emitrackon.in/"
        val hints = hashMapOf<EncodeHintType, Int>().also {
            it[EncodeHintType.MARGIN] = 1
        } // Make the QR code buffer border narrower
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, 1024, 1024, hints)
        val size = 1024 //pixels
        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }

//    private fun startScanning() {
//        codeScanner = CodeScanner(requireContext(), binding.scannerView)
//        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
//        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
//        // ex. listOf(BarcodeFormat.QR_CODE)
//        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
//        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
//        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
//        codeScanner.isFlashEnabled = false // Whether to enable flash or not
//
//        // Callbacks
//        codeScanner.decodeCallback = DecodeCallback {
//            requireActivity().runOnUiThread {
//                if (it.text.contains("emi")) {
//                    showToast("Scan result : ${it.text}")
//                } else {
//                    showToast("Invalid QR code.")
//                }
//            }
//        }
//        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
//            requireActivity().runOnUiThread {
//                showToast("Camera initialization error: ${it.message}")
//            }
//        }
//
//        binding.scannerView.setOnClickListener {
//            codeScanner.startPreview()
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 123) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(requireContext(), "Camera permission granted", Toast.LENGTH_LONG)
//                    .show()
//                startScanning()
//            } else {
//                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_LONG)
//                    .show()
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (::codeScanner.isInitialized) {
//            codeScanner?.startPreview()
//        }
//    }
//
//    override fun onPause() {
//        if (::codeScanner.isInitialized) {
//            codeScanner?.releaseResources()
//        }
//        super.onPause()
//    }

}