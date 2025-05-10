package com.martvalley.suvidha_u.dashboard.retailerModule.key.model

data class Brand(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val updated_at: String,
    val models : List<Model>?
)

data class Model(
    val brand_id: String,
    val created_at: String,
    val id: Int,
    val image: Any,
    val name: String,
    val type: String,
    val updated_at: String
)