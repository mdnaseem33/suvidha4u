package com.martvalley.suvidha_u.dashboard.retailerModule.key.model


data class SmartKey(
    val bank_id: Int,
    val brand_id: Int,
    val customer_id_back: String,
    val customer_id_front: String,
    val down_payment: Int,
    val image: String,
    val imei1: String,
    val imei2: String,
    val loan_amount: Int,
    val loan_date: String,
    val loan_number: String,
    val mobile: String,
    val model_number: String,
    val name: String,
    val payment_term: Int,
    val payment_type: Int,
    val product_price: Int,
    val reference_id_back: String,
    val reference_id_front: String,
    val reference_name: String,
    val reference_number: String,
    val sign: String,
    val tenure: Int
)