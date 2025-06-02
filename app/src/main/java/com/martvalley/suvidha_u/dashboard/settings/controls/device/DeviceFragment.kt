package com.martvalley.suvidha_u.dashboard.settings.controls.device

import android.app.AlertDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.retailerModule.DashBoardNewActivity
import com.martvalley.suvidha_u.dashboard.settings.controls.Control
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.dashboard.settings.controls.device.popup.LocationData
import com.martvalley.suvidha_u.dashboard.settings.controls.device.popup.LocationLisRecylerview
import com.martvalley.suvidha_u.databinding.FragmentDeviceBinding
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.SharedPref
import com.martvalley.suvidha_u.utils.getCurrentDate
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.isGone
import com.martvalley.suvidha_u.utils.loadImage
import com.martvalley.suvidha_u.utils.logd
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class DeviceFragment : Fragment() {
    val binding by lazy { FragmentDeviceBinding.inflate(layoutInflater) }
    var selectedAction = Constants.BASIC
    private lateinit var popUpDetails: PopUpDetails
    lateinit var adapter: BasicAdapter
    private var locList = ArrayList<LocationData>()
    private lateinit var locationLisRecylerview: LocationLisRecylerview

    var list1 = ArrayList<Control.DeviceActionOld>()
    var list2 = ArrayList<Control.DeviceActionOld>()
    var list3 = ArrayList<Control.DeviceActionOld>()
    var gotTheRefreshedDate = false
    var swd: Control.DeviceActionOld= Control.DeviceActionOld(
        "swd", "Set wallpaper Disable", true,  "list1"
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        popUpDetails = PopUpDetails(requireContext())
        // initListener();


        return binding.root
    }

    private fun initListener() {

        //   ("lock_task").setOnPreferenceClickListener(this);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationLisRecylerview = LocationLisRecylerview(requireContext(), listOf())
        callLocationApi(null, null)

        list2.add(Control.DeviceActionOld(sm = "audio", display = "Audio", value = false, "list2"))
        list2.add(Control.DeviceActionOld(sm = "swd", display = "Wallpaper", value = false, "list2"))
        list2.add(Control.DeviceActionOld(sm = "whatsapp", display = "Whatsapp", value = false, "list1"))
        list2.add(Control.DeviceActionOld(sm = "whatsapp_buss", display = "WA Business", value = false, "list1"))
        list2.add(Control.DeviceActionOld(sm = "fb", display = "Facebook", value = false, "list1"))
        list2.add(Control.DeviceActionOld(sm = "insta", display = "Instagram", value = false, "list1"))
        list2.add(Control.DeviceActionOld(sm = "youtube", display = "Youtube", value = false, "list1"))
        list2.add(Control.DeviceActionOld(sm = "twitter", display = "Twitter", value = false, "list1"))
        list2.add(Control.DeviceActionOld(sm = "thread", display = "Thread", value = false, "list1"))

        //list3.add(Control.DeviceActionOld(sm = "location", display = "Location", value = false, "list3"))
        //list3.add(Control.DeviceActionOld(sm = "call_list", display = "Call List", value = false, "list3"))
        list3.add(Control.DeviceActionOld(sm = "mobile_no", display = "Mobile No.", value = false, "list3"))
        list3.add(Control.DeviceActionOld(sm = "online_check", display = "Online", value = false, "list3"))
        list3.add(Control.DeviceActionOld(sm = "sr", display = "Soft Reset", value = true, "list1"))
        list3.add(Control.DeviceActionOld(sm = "debug", display = "Debugging", value = false, "list1"))
        list3.add(
            Control.DeviceActionOld(
                sm = "fr",
                display = "Factory Reset",
                value = false,
                "list1"
            )
        )
        list3.add(
            Control.DeviceActionOld(
                sm = "uftd",
                display = "File Transfer",
                value = false,
                "list1"
            )
        )
        // list3.add(Control.DeviceActionOld(sm = "", display = "Debugging", value = true, "list3"))


        binding.getDetail.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(), ViewUsersDetailActivity::class.java
                ).putExtra("id", (requireActivity() as ControlsActivity).id.toString())
            )
        }

        adapter = BasicAdapter(list1, requireContext()) { pos, data, action ->
            when (data) {
                "Wallpaper" -> {
                    withNetwork { callSetWallpaperApi() }
                }
                "Audio" -> {
                    withNetwork { callSetAudioApi() }
                }

                "Call List" -> {
                    withNetwork { callRequestDeviceInfoApi("call") }
                    showCallListPopUp()
                }

                "Location" -> {
                    withNetwork { callRequestDeviceInfoApi("location") }
                    showLocationPopUp()
                }

                "facebook" -> {
                    withNetwork { callRequestDeviceInfoApi("facebook") }
                }

                "Mobile No." -> {
                    withNetwork { callRequestDeviceInfoApi("mobile") }
                    showPhonePopUp()
                }

                "list1" -> {

                    withNetwork { callSubmitActionApi(false) }
                }




            }
        }
        binding.rv.adapter = adapter
        binding.activate.setOnClickListener({
            withNetwork { callRequestDeviceInfoApi("activate") }
        })

        binding.deactivate.setOnClickListener({
            withNetwork { callRequestDeviceInfoApi("deactive") }
        })
        setAdapter(list1)

        binding.refresh.setOnClickListener {
            withNetwork { callGetUserApi() }
            // binding.userDetail.syncValueTv.text = getCurrentDate()

        }

        withNetwork { callGetUserApi() }

        binding.surrenderbtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm Action")
            builder.setMessage("Are you sure you want to proceed?")

            builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                withNetwork { callRequestDeviceInfoApi("remove") }
                withNetwork { surrenderCustomer() }
            }

            builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                // Handle negative button click
                // e.g., cancel the action
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        binding.basicBtn.backgroundTintList =
            ColorStateList.valueOf(requireContext().getColor(R.color.blue))
        binding.basicBtn.setTextColor(requireContext().getColor(R.color.white))

        binding.applicationBtn.backgroundTintList = null
        binding.applicationBtn.setTextColor(requireContext().getColor(R.color.blue))

        binding.advanceBtn.backgroundTintList = null
        binding.advanceBtn.setTextColor(requireContext().getColor(R.color.blue))

        binding.basicBtn.setOnClickListener {
            selectedAction = Constants.BASIC
            (requireActivity() as ControlsActivity).selectedList = "list1"
            setAdapter(list1)

            binding.basicBtn.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.blue))
            binding.basicBtn.setTextColor(requireContext().getColor(R.color.white))

            binding.applicationBtn.backgroundTintList = null
            binding.applicationBtn.setTextColor(requireContext().getColor(R.color.blue))

            binding.advanceBtn.backgroundTintList = null
            binding.advanceBtn.setTextColor(requireContext().getColor(R.color.blue))
            withNetwork { callGetUserApi() }
        }

        binding.applicationBtn.setOnClickListener {
            selectedAction = Constants.APPLICATION
            (requireActivity() as ControlsActivity).selectedList = "list2"
            Log.d("tab", (requireActivity() as ControlsActivity).selectedList)
            setAdapter(list2)
            binding.applicationBtn.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.blue))
            binding.applicationBtn.setTextColor(requireContext().getColor(R.color.white))

            binding.basicBtn.backgroundTintList = null
            binding.basicBtn.setTextColor(requireContext().getColor(R.color.blue))

            binding.advanceBtn.backgroundTintList = null
            binding.advanceBtn.setTextColor(requireContext().getColor(R.color.blue))
            withNetwork { callGetUserApi()
            }
        }

        binding.advanceBtn.setOnClickListener {
            withNetwork { callGetUserApi() }
            selectedAction = Constants.ADVANCE
            (requireActivity() as ControlsActivity).selectedList = "list3"
            setAdapter(list3)
            binding.advanceBtn.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.blue))
            binding.advanceBtn.setTextColor(requireContext().getColor(R.color.white))

            binding.basicBtn.backgroundTintList = null
            binding.basicBtn.setTextColor(requireContext().getColor(R.color.blue))

            binding.applicationBtn.backgroundTintList = null
            binding.applicationBtn.setTextColor(requireContext().getColor(R.color.blue))

        }

        binding.offlineLock.setOnClickListener {
            showLockPhonePopUp(true)
        }

        binding.offlineUnlock.setOnClickListener {
            showLockPhonePopUp(false)
        }

    }

    private fun showLockPhonePopUp(isLock: Boolean) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.phone_dialog_pop_up, null)
        val textView = dialogView.findViewById<TextView>(R.id.dialogTextView)
        textView.text = SharedPref(requireContext()).getValueString("UserPhoneNum")
        textView.setOnClickListener {
            openSmsApp(textView.text.toString(), isLock)
        }
        val current = dialogView.findViewById<TextView>(R.id.currentPhoneNo)
        current.setOnClickListener {
            openSmsApp(current.text.toString(), isLock)
        }
        callMobileApi(dialogView)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun openSmsApp(phoneNumber: String, isLock: Boolean) {
        try {
            var message = if(isLock) "<<ACTION 100>> " else "<<ACTION 200>> "
            message += Base64.encodeToString((requireActivity() as ControlsActivity).id.toString().toByteArray(), Base64.DEFAULT)
            val smsUri = "smsto:$phoneNumber" // Format the URI with the phone number
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(smsUri) // Set the SMS URI
                putExtra("sms_body", message) // Add the predefined message
            }
            requireContext().startActivity(intent)
        }catch (e:Exception){
            showToast("Invalid Mobile number")
            e.printStackTrace()
        }


    }

    private fun showLocationPopUp() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.location_listpopup, null)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.close_btn)
        val progress = dialogView.findViewById<TextView>(R.id.no_data)
        val prog = dialogView.findViewById<ProgressBar>(R.id.prog)
        val recylerview = dialogView.findViewById<RecyclerView>(R.id.location_list_rv)
        val refresh = dialogView.findViewById<ImageView>(R.id.refresh)
        refresh.setOnClickListener {
            prog.show()
            withNetwork { callLocationApi(recylerview, prog) }
        }
        if (locList.isEmpty()) {
            progress.show()
        } else {
            progress.isGone()
        }


        recylerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationLisRecylerview
        }


        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)
        val dialog = dialogBuilder.create()
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showCallListPopUp() {

        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.call_list_popup, null)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.close_btn)

        callApi(dialogView)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)
        val dialog = dialogBuilder.create()
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showPhonePopUp() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.phone_dialog_pop_up, null)
        val textView = dialogView.findViewById<TextView>(R.id.dialogTextView)
        textView.text = SharedPref(requireContext()).getValueString("UserPhoneNum")
        callMobileApi(dialogView)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun callSubmitActionApi(wall: Boolean) {

        val jsonData = convertToJson(list1, list3)
        if(wall){
            swd.value= !swd.value;
        }

        val valueJson = JSONObject()
        valueJson.put("display", swd.display)
        valueJson.put("value", swd.value)
        jsonData.put(swd.sm, valueJson)
        val gson = Gson().fromJson(jsonData.toString(), Control.Data::class.java)
        Log.d("currentJson", jsonData.toString())
        binding.pb.show()
        val call = RetrofitInstance.apiService.submitActionApi(
            Control.ActionUpdateRequest(
                (requireActivity() as ControlsActivity).id.toString(), gson
            )
        )
        call.enqueue(object : Callback<Control.ActionUpdateResponse> {
            override fun onResponse(
                call: Call<Control.ActionUpdateResponse>,
                response: Response<Control.ActionUpdateResponse>
            ) {
                response.logd("data action response")
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            showToast(it.message)
                        }
                    }

                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<Control.ActionUpdateResponse>, t: Throwable
            ) {
                t.logd("data action response")
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })


    }

    private fun callSetWallpaperApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.sendWallpaperApi(
            DeviceBasics.SendRequest((requireActivity() as ControlsActivity).id.toString())
        )
        call.enqueue(object : Callback<DeviceBasics.DeviceInfoResponse> {
            override fun onResponse(
                call: Call<DeviceBasics.DeviceInfoResponse>,
                response: Response<DeviceBasics.DeviceInfoResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            showToast("Wallpaper Updated Succussfully")
                            callSubmitActionApi(true)
                        }
                    }

                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<DeviceBasics.DeviceInfoResponse>, t: Throwable
            ) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })
    }

    private fun callSetAudioApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.sendAudioApi(
            DeviceBasics.SendRequest((requireActivity() as ControlsActivity).id.toString())
        )
        call.enqueue(object : Callback<DeviceBasics.DeviceInfoResponse> {
            override fun onResponse(
                call: Call<DeviceBasics.DeviceInfoResponse>,
                response: Response<DeviceBasics.DeviceInfoResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            Toast.makeText(
                                context,
                                "Audio Playing in the User device",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<DeviceBasics.DeviceInfoResponse>, t: Throwable
            ) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })
    }

    private fun callRequestDeviceInfoApi(type: String) {
        binding.pb.show()
        val call = RetrofitInstance.apiService.requestDeviceInfoApi(
            DeviceBasics.DeviceInfoRequest(
                (requireActivity() as ControlsActivity).id.toString(), type
            )
        )
        call.enqueue(object : Callback<DeviceBasics.DeviceInfoResponse> {
            override fun onResponse(
                call: Call<DeviceBasics.DeviceInfoResponse>,
                response: Response<DeviceBasics.DeviceInfoResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            showToast(it.message)
                        }
                    }

                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<DeviceBasics.DeviceInfoResponse>, t: Throwable
            ) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })
    }

    private fun setAdapter(list: ArrayList<Control.DeviceActionOld>) {
        adapter.mList = list
        adapter.notifyDataSetChanged()
    }

    fun setUserData(data: Control.Customer) {
        var model: String? = ""
        if (data.device_detail != null) {
            model = getModelFromJson(data.device_detail)
        }

        SharedPref(requireContext()).save("UserPhoneNum", data.phone)

        binding.userDetail.apply {
            nameValueTv.text = data.name
            pNoValueTv.text = data.phone
            binding.userDetail.modelValueTv.text = data.imei1
            binding.userDetail.nameValueTv.text = data.name
            binding.userDetail.pNoValueTv.text = data.phone
            if (data.brand != null){
                modelImage.loadImage(Constants.BASEURL + data.brand.image)
            }
            if(data.bank != null){

                bankImage.loadImage(Constants.BASEURL + data.bank!!.image)
            }

            productType.text = data.application_type
            modelText.text = data.model
            if (data.reference_name != null){
                referenceName.text = "Name: " + data.reference_name
            }
            if (data.reference_number != null){
                referPhone.text = "Phone: " + data.reference_number
            }
            // date.text = data.created_at.convertISOTimeToDate()
            if (gotTheRefreshedDate) {
                syncValueTv.text = getCurrentDate()
            } else {
                syncValueTv.text = datetimeToString(data.last_sync!!)
                gotTheRefreshedDate = false
            }



            if (!data.image.isNullOrEmpty()) {
                binding.userimg.loadImage(Constants.BASEURL + "storage/" + data.image)
                binding.userimg.setOnClickListener {
                    showImagePopUp(Constants.BASEURL + "storage/" + data.image)
                }
            }
        }

    }
    private fun showImagePopUp(imageUrl: String) {
        // Inflate the popup layout
        val inflater = requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.pop_image_view, null)

        // Create the PopupWindow
        val popupWindow = PopupWindow(popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        // Find the ImageView in the popup layout
        val imageView = popupView.findViewById<ImageView>(R.id.dialog_image_view)

        // Set an image resource or bitmap
        imageView.loadImage(imageUrl)

        // Show the PopupWindow
        popupWindow.showAtLocation(binding.userimg,
            Gravity.CENTER, 0, 0)

        // Set a dismiss listener for when the popup is outside clicked
        popupView.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    fun getModelFromJson(jsonString: String): String? {
        try {
            val jsonObject = JSONObject(jsonString)
            return jsonObject.optString("Model")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isLastSyncOlderThanTenMinutes(lastSync: String): Boolean {
        // Parse the last_sync time
        val lastSyncTime = OffsetDateTime.parse(lastSync, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        // Get the current time
        val currentTime = OffsetDateTime.now()

        // Calculate the difference in minutes
        val minutesDifference = ChronoUnit.MINUTES.between(lastSyncTime, currentTime)
        return minutesDifference < 10
    }


    private fun datetimeToString(lastSync: String): String {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Parse the last_sync time
                val lastSyncTime = OffsetDateTime.parse(lastSync, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                return lastSyncTime.format(outputFormatter)
            } else {
                return lastSync
            }
        }catch (e: Exception){
            return lastSync
        }

    }

    private fun callGetUserApi() {
        binding.pb.show()
        val call =
            RetrofitInstance.apiService.getCustomerbyIdApi((requireActivity() as ControlsActivity).id.toString())
        call.enqueue(object : Callback<Control.GetCustomerResponse> {
            override fun onResponse(
                call: Call<Control.GetCustomerResponse>,
                response: Response<Control.GetCustomerResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            (requireActivity() as ControlsActivity).cust_data = it
                            setUserData(it.customer)
                            if(it.customer.key_type ==5 && it.customer.payment_type != 1){
                                showToast("No Emi for this anti theft")
                                activity?.finish();
                            }
                            try {
                                if(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && it.customer.last_sync != null) {
                                        isLastSyncOlderThanTenMinutes(it.customer.last_sync)
                                    } else {
                                        false
                                    }
                                ){
                                    list3.forEachIndexed { i, it ->
                                        if (it.sm == "online_check") {
                                            list3[i].value = true
                                        }
                                    }
                                }
                            }catch (e: Exception){
                            }
                            if( it.customer.is_set_wallpaper == "1"){
                                it.action.swd.value = false
                            }else{
                                it.action.swd.value = false
                            }
                            setList1Data(it.action)
                        }
                    }

                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Control.GetCustomerResponse>, t: Throwable) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }
        })
    }

    private fun surrenderCustomer() {
        binding.pb.show()
        val call =
            RetrofitInstance.apiService.surrenderCustomer((requireActivity() as ControlsActivity).id.toString())
        call.enqueue(object : Callback<Control.SurrenderResponse> {
            override fun onResponse(
                call: Call<Control.SurrenderResponse>,
                response: Response<Control.SurrenderResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        startActivity(Intent(requireContext(), DashBoardNewActivity::class.java))
                        activity?.finish()

                    }

                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Control.SurrenderResponse>, t: Throwable) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }
        })
    }


    private fun setList1Data(action: Control.Action) {
        list1.clear()
        list1.add(
            Control.DeviceActionOld(
                "brl", "Brightness", false, "list1"
            )
        )
        val json = JSONObject(Gson().toJson(action))
        val iter: Iterator<String> = json.keys()
        while (iter.hasNext()) {
            val key = iter.next()
            try {
                val value = json.get(key) as JSONObject
                if (arrayListOf("fr" ,"debug","uftd").contains(key)) {
                    list3.forEachIndexed { i, it ->
                        if (it.sm == key) {
                            list3[i].value = value.getBoolean("value")


                        }
                    }
                } else if( arrayListOf("swd", "whatsapp","twitter","thread", "fb", "insta", "youtube", "whatsapp_buss").contains(key) ) {
                    list2.forEachIndexed { i, it ->
                        if (it.sm == key) {
                            list2[i].value = value.getBoolean("value")


                        }
                    }

                } else if( arrayListOf("brl").contains(key) ) {
                    list1.forEachIndexed { i, it ->
                        if (it.sm == key) {

                            list1[i].value = value.getBoolean("value")
                        }
                    }
                } else  {
                    list1.add(
                        Control.DeviceActionOld(
                            key, value.getString("display"), value.getBoolean("value"), "list1"
                        )
                    )
                }

            } catch (e: JSONException) {
                // Something went wrong!
            }
        }

        adapter.notifyDataSetChanged()

        selectedAction = Constants.BASIC
        (activity as ControlsActivity).selectedList = "list1"

    }

    fun convertToJson(
        list: ArrayList<Control.DeviceActionOld>,
        list3: ArrayList<Control.DeviceActionOld>
    ): JSONObject {
        val jsonObject = JSONObject()
        list.forEach {
            val valueJson = JSONObject()
            valueJson.put("display", it.display)
            valueJson.put("value", it.value)
            jsonObject.put(it.sm, valueJson)
        }

        list3.map {

            if(it.sm != ""){
                val valueJson = JSONObject()
                valueJson.put("display", it.display)

                valueJson.put("value", it.value)
                jsonObject.put(it.sm, valueJson)
            }

        }

        list2.map {
            if(it.sm != ""){
                val valueJson = JSONObject()
                valueJson.put("display", it.display)

                valueJson.put("value", it.value)
                jsonObject.put(it.sm, valueJson)
            }
        }
        jsonObject.logd("json data")
        return jsonObject
    }

    private fun callApi(dialogView: View) {
        val progress = dialogView.findViewById<ProgressBar>(R.id.progress)
        val errorMessage = dialogView.findViewById<TextView>(R.id.title_txt)
        val callValue = dialogView.findViewById<TextView>(R.id.call_value)
        progress.show()
        val call = RetrofitInstance.apiService.getCustomerDeviceLastDataApi(
            (requireActivity() as ControlsActivity).id.toString()
        )
        call.enqueue(object : Callback<DeviceBasics.GetDeviceInfo> {
            override fun onResponse(
                call: Call<DeviceBasics.GetDeviceInfo>,
                response: Response<DeviceBasics.GetDeviceInfo>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        if (response.body()?.data == null) {
                            progress.show()
                            errorMessage.setText(R.string.no_data_found)
                        } else {
                            progress.hide()
                            response.body()?.let {
                                errorMessage.setText(R.string.data_found)
                                Log.d("ReceentCh", it.data.location.toString())

//                                    .let {
//                                    locList = it.replace("{", "").replace("}", "")
//                                }
//                                it.data.mobile?.let {
//                                    binding.mobileValue.text =
//                                        it.replace("null", "---").removeSuffix(",")
//                                }
                                it.data.call_list?.let {
                                    callValue.text = it
                                }


                            }
                        }
                    }

                    else -> {
                        progress.show()
                        // requireContext().showApiErrorToast()
                    }
                }
            }

            override fun onFailure(
                call: Call<DeviceBasics.GetDeviceInfo>, t: Throwable
            ) {

            }

        })
    }

    private fun callLocationApi(recyclerView: RecyclerView?, prog: ProgressBar?) {
        val call = RetrofitInstance.apiService.getCustomerDeviceLastDataApi(
            (requireActivity() as ControlsActivity).id.toString()
        )
        call.enqueue(object : Callback<DeviceBasics.GetDeviceInfo> {
            override fun onResponse(
                call: Call<DeviceBasics.GetDeviceInfo>,
                response: Response<DeviceBasics.GetDeviceInfo>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        if(prog != null){
                            prog.hide()
                        }
                        if (response.body()?.data == null) {

                        } else {

                            response.body()?.let {
                                locList.clear()
                                for (i in it.data.location) {
                                    try {
                                        val jsonObject = JSONObject(i.coordinates)
                                        val latitude = jsonObject.getDouble("lat")
                                        val longitude = jsonObject.getDouble("long")
                                        locList.addAll(
                                            listOf(
                                                LocationData(
                                                    latitude,
                                                    longitude,
                                                    i.created_at
                                                )
                                            )
                                        )
                                        locationLisRecylerview =
                                            LocationLisRecylerview(requireContext(), locList)
                                        if(recyclerView != null){
                                            recyclerView.adapter = locationLisRecylerview
                                            binding.pb.hide()
                                        }

                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                it.data.call_list?.let {

                                }



                            }
                        }
                    }

                    else -> {
                    }
                }
            }

            override fun onFailure(
                call: Call<DeviceBasics.GetDeviceInfo>, t: Throwable
            ) {
            }

        })
    }

    private fun callMobileApi(dialogView: View) {
        val call = RetrofitInstance.apiService.getCustomerDeviceLastDataApi(
            (requireActivity() as ControlsActivity).id.toString()
        )
        call.enqueue(object : Callback<DeviceBasics.GetDeviceInfo> {
            override fun onResponse(
                call: Call<DeviceBasics.GetDeviceInfo>,
                response: Response<DeviceBasics.GetDeviceInfo>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.data?.mobile?.let {
                            dialogView.findViewById<TextView>(R.id.currentPhoneNo).text = it
                        }
                        if (response.body()?.data == null) {

                        } else {

                            response.body()?.let {
                                if(it.data.mobile != null){
                                    dialogView.findViewById<TextView>(R.id.currentPhoneNo).text = it.data.mobile
                                }else{
                                    dialogView.findViewById<TextView>(R.id.currentPhoneNo).text = "No Data Found"
                                }
                            }
                        }
                    }

                    else -> {
                    }
                }
            }

            override fun onFailure(
                call: Call<DeviceBasics.GetDeviceInfo>, t: Throwable
            ) {
            }

        })
    }
}


