package com.martvalley.suvidha_u.forgot_pass

object ForgotPass {
    data class Request(
        val email: String
    )

    data class Response(
        val message: String,
        val status: Int
    )
}