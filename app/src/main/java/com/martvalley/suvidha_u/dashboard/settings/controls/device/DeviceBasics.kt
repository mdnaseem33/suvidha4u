package com.martvalley.suvidha_u.dashboard.settings.controls.device

object DeviceBasics {
    data class DeviceInfoRequest(
        val customerId: String,
        val type: String,
    )

    data class DeviceInfoResponse(
        val message: String, val status: Int
    )

    data class SendRequest(
        val customerId: String,
    )


    data class GetDeviceInfo(
        val data: Data, val message: String, val status: Int
    )

    data class Data(
        val call_list: String?,
        val created_at: String?,
        val customer_id: Int,
        val id: Int,
        val location: List<location>,
        val mobile: String?,
        val updated_at: String?
    )

    data class location(
        val coordinates: String,
        val created_at: String
    )
}