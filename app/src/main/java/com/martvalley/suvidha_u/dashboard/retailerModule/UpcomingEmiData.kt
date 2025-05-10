package com.martvalley.suvidha_u.dashboard.retailerModule

object UpcomingEmiData {
    data class UpcomingEmiResponse(
        val emi: ArrayList<Emi>
    )

    data class Emi(
        val amount: Int,
        val created_at: String,
        val customer_id: Int,
        val id: Int,
        val name: String,
        val order_id: Any,
        val pay_date: Any,
        val pay_on_date: String,
        val payment_id: Any,
        val payment_status: Any,
        val phone: String,
        val split_status: Any,
        val status: String,
        val updated_at: String
    )
}