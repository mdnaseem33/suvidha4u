package com.martvalley.suvidha_u.login

import com.google.gson.annotations.SerializedName

object Login {

    data class LoginRequest(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String,
        @SerializedName("role") val role: Int
    )

    data class LoginResponse(
        val access_token: String,
        val token_type: String
    )

    data class ErrorResponse(   // 401 code
        val message: String
    )
}