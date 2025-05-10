package com.martvalley.suvidha_u.dashboard.retailerModule.fragments

object AllTransactionData {
    data class TransactionResponse(
        val transactions: List<Transaction>
    )

    data class Transaction(
        val amount: Int,
        val closing: Int,
        val created_at: String,
        val id: Int,
        val notes: Any,
        val opening: Int,
        val reciever: Reciever,
        val reciever_id: Int,
        val sender: Sender,
        val sender_id: Int,
        val type: Any,
        val updated_at: String
    )

    data class Reciever(
        val address: String,
        val audio: Any,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val deleted_at: Any,
        val distributor_type: Int,
        val email: String,
        val email_verified_at: Any,
        val frp_email: String,
        val gst: String,
        val id: Int,
        val image: String,
        val loan_prefix: String,
        val member: String,
        val message: Any,
        val name: String,
        val owner_name: String,
        val payment_qr: String,
        val phone: String,
        val qr_id: String,
        val role: Int,
        val sign: String,
        val state: String,
        val status: Int,
        val updated_at: String,
        val wallet_balance: Int,
        val wallpaper: String
    )

    data class Sender(
        val address: String,
        val audio: Any,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val deleted_at: Any,
        val distributor_type: Int,
        val email: String,
        val email_verified_at: Any,
        val frp_email: String,
        val gst: String,
        val id: Int,
        val image: String,
        val loan_prefix: String,
        val member: String,
        val message: Any,
        val name: String,
        val owner_name: String,
        val payment_qr: String,
        val phone: String,
        val qr_id: String,
        val role: Int,
        val sign: String,
        val state: String,
        val status: Int,
        val updated_at: String,
        val wallet_balance: Int,
        val wallpaper: String
    )
}