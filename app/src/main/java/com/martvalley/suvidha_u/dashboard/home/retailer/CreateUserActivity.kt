package com.martvalley.suvidha_u.dashboard.home.retailer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.home.Dashboard
import com.martvalley.suvidha_u.dashboard.qr_code.BarcodeReaderPopup
import com.martvalley.suvidha_u.dashboard.qr_code.BarcodeScanListener
import com.martvalley.suvidha_u.dashboard.settings.SigningActivity
import com.martvalley.suvidha_u.databinding.ActivityCreateUserBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateUserActivity : AppCompatActivity(), BarcodeScanListener {
    private val binding by lazy { ActivityCreateUserBinding.inflate(layoutInflater) }

    companion object {
        var bitmap: Bitmap? = null
    }

    private val PICK_IMAGE_FROM_GALLERY = 1
    private val TAKE_PICTURE = 2
    private var photoFile: File? = null
    private var photoFileUri: Uri? = null

    var selectedEmi = -1

    var customerImage = ""
    var aadharFrontImage = ""
    var aadharBackImage = ""
    var adharFront = false
    var adharBack = false
    private lateinit var currentPhotoPath: String



    private val aadhar_front_image =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        }

    private val aadhar_back_image =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { uri ->
                uri.getBase64String(contentResolver)?.let { media ->
                    aadharBackImage = ("data:image/png;base64,$media")
                    val real_path = FileUtils.getRealPath(this, uri)
                    binding.aadharBackImage.text = real_path?.split("/")?.last()
                }
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.text.text = "Register User"
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

        binding.downArrow.setOnClickListener {
            if (binding.linear.visibility == View.GONE) {
                binding.arrowImg.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                binding.linear.visibility = View.VISIBLE
            } else {
                binding.linear.visibility = View.GONE
                binding.arrowImg.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            }
        }

        binding.checkbox.setSpan(this) {
            startActivityForResult(
                Intent(this, SigningActivity::class.java).putExtra(
                    "create", "user"
                ), 10
            )
        }

        binding.scannerBtn.setOnClickListener{
            val barcodeReaderPopup = BarcodeReaderPopup()
            barcodeReaderPopup.setBarcodeScanListener(this)
            barcodeReaderPopup.show(supportFragmentManager, "BarcodeReaderPopup")
        }

        binding.firstInstallmentDate.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    this, R.style.MyDatePickerSpinnerStyle, { view, year, monthOfYear, dayOfMonth ->
                        binding.firstInstallmentDate.setText(
                            String.format(
                                "%02d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth,
                            )
                        )
                    }, year, month, day
                )
                dpd.datePicker.minDate = Date().time
                dpd.show()
                return@setOnTouchListener true
            }
            false
        }

//        binding.retailerId.setOnTouchListener { v, event ->
//            val DRAWABLE_RIGHT = 2
//            if (event.action == MotionEvent.ACTION_UP) {
//                if (event.rawX >= binding.retailerId.right - binding.retailerId.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
//                    startActivityForResult(Intent(this, ScanQRActivity::class.java), 100)
//                    return@setOnTouchListener true
//                }
//            }
//            false
//        }

        binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                if (bitmap == null) {
                    showToast("Please sign.")
                    compoundButton.isChecked = false
                }
            }
        }

        binding.customerImage.setOnClickListener {
            adharBack = false
            adharFront = false
            showChooseImageSourceDialog()
        }
        binding.aadharFrontImage.setOnClickListener {
           // aadhar_front_image.launch("image/*")
            adharFront = true
            showChooseImageSourceDialog()
        }
        binding.aadharBackImage.setOnClickListener {
            adharBack = true
            showChooseImageSourceDialog()
        }

        val dataAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, emis)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_item)
        binding.spinner.adapter = dataAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedEmi = p2
                binding.spinner.hideKeyboard()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedEmi = -1
            }
        }

        binding.regBtn.setOnClickListener {
            val name = binding.custNameEt.text.toString().trim()
            val phone = binding.custNoEt.text.toString().trim()
            val amt = binding.emiAmt.text.toString().trim()
            val date = binding.firstInstallmentDate.text.toString().trim()
            val loanid = binding.loanId.text.toString().trim()
//            val retailerId = binding.retailerId.text.toString().trim()

            if (name.isEmpty()) {
                showToast("Enter name")
            } else if (phone.isEmpty()) {
                showToast("Enter phone number")
            } else if (amt.isEmpty()) {
                showToast("Enter amount")
            } else if (date.isEmpty()) {
                showToast("Choose date")
            } else if (loanid.isEmpty()) {
                showToast("Enter loan id")
            } /*else if (retailerId.isEmpty()) {
                showToast("Enter retailer id")
            }*/
            else if (selectedEmi == 0 || selectedEmi == -1) {
                showToast("Select emi months")
            } else if (!binding.checkbox.isChecked && bitmap == null) {
                showToast("Please sign here.")
            } else {
                val request = Dashboard.CreateUserRequest(
                    aadhar_back_image = aadharBackImage,
                    aadhar_front_image = aadharFrontImage,
                    bill_no = loanid,
                    emi_amount = amt,
                    emi_months = selectedEmi,
                    first_intallment_date = date,
                    image = customerImage,
                    name = name,
                    phone = phone,
                    sign = bitmap?.bitmapToBase64().toString(),
                    qr_id = MainApplication.authData?.qr_id.toString()
                )
                withNetwork { callApi(request) }
            }
        }


    }

    var emis = arrayOf(
        "Months of EMI", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
    )

    private fun showChooseImageSourceDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_choose_image_source, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Choose Image Source")

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        dialogView.findViewById<Button>(R.id.btn_gallery).setOnClickListener {
            // Open gallery to choose image
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY)
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_camera).setOnClickListener {
            // Open camera to take picture
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.martvalley.suvidha_u.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, TAKE_PICTURE)
                    }
                }
            }
            alertDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_FROM_GALLERY -> {
                    // Handle image picked from gallery
                    val selectedImageUri = data?.data
                    // Do something with the selected image URI
                    if (adharFront){
                        selectedImageUri?.let { uri ->
                            uri.getBase64String(contentResolver)?.let { media ->
                                aadharFrontImage = ("data:image/png;base64,$media")
                                val real_path = FileUtils.getRealPath(this, uri)
                                binding.aadharFrontImage.text = real_path?.split("/")?.last()
                            }
                        }
                    } else if (adharBack){
                        selectedImageUri?.let { uri ->
                            uri.getBase64String(contentResolver)?.let { media ->
                                aadharBackImage = ("data:image/png;base64,$media")
                                val real_path = FileUtils.getRealPath(this, uri)
                                binding.aadharBackImage.text = real_path?.split("/")?.last()
                            }
                        }
                    } else {
                        selectedImageUri?.getBase64String(contentResolver)?.let { media ->
                            customerImage = ("data:image/png;base64,$media")
                            val real_path = FileUtils.getRealPath(this, selectedImageUri)
                            binding.customerImage.text = real_path?.split("/")?.last()
                        }
                    }
                }
                TAKE_PICTURE -> {
                    // Handle picture taken from camera.get("data")
                    val file = File(currentPhotoPath)
                    val imageUri = Uri.fromFile(file)

                    if (adharFront){
                        imageUri?.let { uri ->
                            uri.getBase64String(contentResolver)?.let { media ->
                                aadharFrontImage = ("data:image/png;base64,$media")
                                val real_path = FileUtils.getRealPath(this, uri)
                                binding.aadharFrontImage.text = real_path?.split("/")?.last()
                            }
                        }
                        adharFront = false
                    } else if (adharBack){
                        imageUri?.let { uri ->
                            uri.getBase64String(contentResolver)?.let { media ->
                                aadharBackImage = ("data:image/png;base64,$media")
                                val real_path = FileUtils.getRealPath(this, uri)
                                binding.aadharBackImage.text = real_path?.split("/")?.last()
                            }
                            adharBack = false
                        }
                    } else {
                        imageUri.getBase64String(contentResolver)?.let { media ->
                            customerImage = ("data:image/png;base64,$media")
                            val real_path = FileUtils.getRealPath(this, imageUri)
                            binding.customerImage.text = real_path?.split("/")?.last()
                        }
                    }

                }
                10 -> {
                    if (bitmap != null) {
                        binding.signView.show()
                        binding.signView.setImageBitmap(bitmap)
                        binding.checkbox.isChecked = true
                    }
                }
            }
        }
    }



    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath

        }
    }




    private fun callApi(request: Dashboard.CreateUserRequest) {
        request.logd()
        binding.pb.show()
        val call = RetrofitInstance.apiService.userCreateApi(request)
        call.enqueue(object : Callback<Dashboard.CreateUserResponse> {
            override fun onResponse(
                call: Call<Dashboard.CreateUserResponse>,
                response: Response<Dashboard.CreateUserResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            it.logd()
                            showToast(it.message + "with user id " + it.customer_id)
                            DialogFragment().apply {
                                userid = it.customer_id
                                show(supportFragmentManager, "")
                            }
                            bitmap = null
                        }
                    }
                    else -> {
                        showApiErrorToast()
                        bitmap = null
                    }
                }
            }

            override fun onFailure(call: Call<Dashboard.CreateUserResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

    override fun onBarcodeScanResult(barcodeResult: String) {
        binding.imeiNumberTxt.setText(barcodeResult)
    }

}