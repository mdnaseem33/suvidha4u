package com.martvalley.suvidha_u.login

object Auth {

    data class AuthResponse(
        val address: String,
        val author: Int,
        val balance: String,
        val created_at: String,
        val email: String,
        val email_verified_at: Any,
        val gst: String,
        val id: Int,
        val member: String,
        val image: String,
        val name: String,
        val owner_name: String,
        val phone: String,
        val role: Int,
        val state: String,
        val status: Int,
        val updated_at: String,
        val deleted_at: String,
        val qr_id: String,
        val message: String,
        var wallpaper: String,
        var distributor_type: Int?,
        var payment_qr: String,
        val frp_email: String?,
        var loan_prefix: String,
        val sign: String?,
        val audio: String,
        val version: Long?
    )

}