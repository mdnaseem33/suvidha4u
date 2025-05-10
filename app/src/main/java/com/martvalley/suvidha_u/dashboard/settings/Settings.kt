package com.martvalley.suvidha_u.dashboard.settings

import com.martvalley.suvidha_u.login.Auth

object Settings {

    data class PasswordChangeRequest(
        val confirm_password: String,
        val new_password: String,
        val old_password: String
    )

    data class PasswordChangeRetailerRequest(
        val confirm_password: String,
        val password: String,
        val id: String
    )

    data class ProfileUpdateRequest(
        val address: String,
        val gst: String,
        val name: String,
        val owner_name: String,
        val state: String,
        val sign: String,  // to do
    )


    data class ProfileUpdateResponse(
        val message: String,
//        val record: Record,
        val record: Auth.AuthResponse,
        val status: Int
    )

    data class Record(
        val address: String,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val deleted_at: Any,
        val email: String,
        val email_verified_at: String,
        val gst: String,
        val id: Int,
        val image: String,
        val name: String,
        val owner_name: String,
        val phone: String,
        val qr_id: Any,
        val role: Int,
        val state: String,
        val status: Int,
        val updated_at: String
    )


    data class SaveWallpaperRequest(
        var wallpaper: String
    )
    data class SaveProfileRequest(
        var image: String
    )

    data class SaveWallpaperResponse(
        val message: String,
        val retailer: Auth.AuthResponse,
        val status: Int
    )

    data class SavePaymentRequest(
        var payment_qr: String
    )

    data class SaveFrpEmailRequest(
        var frp_email: String
    )

    data class SaveLoanPrefixRequest(
        var loan_prefix: String
    )


    data class Retailer(
        val address: String,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val deleted_at: Any,
        val email: String,
        val email_verified_at: Any,
        val gst: String,
        val id: Int,
        val image: Any,
        val message: String,
        val name: String,
        val owner_name: String,
        val phone: String,
        val qr_id: String,
        val role: Int,
        val sign: String,
        val state: String,
        val status: Int,
        val updated_at: String,
        val wallpaper: String
    )


    data class SetAudioRequest(
        val audio: String
    )

    data class SetAudioResponse(
        val message: String,
        val retailer: Auth.AuthResponse,
        val status: Int
    )

}