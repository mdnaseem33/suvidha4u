package com.martvalley.suvidha_u.dashboard.qr_code

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.home.retailer.DialogFragment

class BarcodeReaderPopup : DialogFragment(){

    private var scanListener: BarcodeScanListener? = null
    private lateinit var barcodeView: DecoratedBarcodeView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.popup_barcode_reader, container, false)
        barcodeView = view.findViewById(R.id.barcode_scanner)

        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    scanListener?.onBarcodeScanResult(result.text)
                    dismiss()
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                // Not used in this implementation
            }
        })

        return view
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    fun setBarcodeScanListener(listener: BarcodeScanListener) {
        this.scanListener = listener
    }
}

interface BarcodeScanListener {
    fun onBarcodeScanResult(barcodeResult: String)
}