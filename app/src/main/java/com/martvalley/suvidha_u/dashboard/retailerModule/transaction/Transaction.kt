package com.martvalley.suvidha_u.dashboard.retailerModule.transaction

data class Transaction(
    val amount: Int,
    val created_at: String,
    val customer_id: Int,
    val id: Int,
    val recieved: Recieved,
    val sender_id: Int,
    val updated_at: String
)