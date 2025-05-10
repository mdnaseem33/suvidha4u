package com.martvalley.suvidha_u.dashboard.retailerModule.key.model

data class CustomerUpdateRequest(
    val bank_id: Int,
    val brand_id: Int,
    val customerId: String,
    val customer_id_back: String?,
    val customer_id_front: String?,
    val down_payment: String,
    val image: String?,
    val loan_number: String,
    val mobile: String,
    val model_number: String,
    val name: String,
    val product_price: String,
    val reference_id_back: String?,
    val reference_id_front: String?,
    val reference_name: String?,
    val reference_number: String?,
    val product_photo: String?,
    val sign: String?
)