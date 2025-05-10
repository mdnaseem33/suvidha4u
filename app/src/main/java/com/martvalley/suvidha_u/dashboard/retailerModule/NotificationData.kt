package com.martvalley.suvidha_u.dashboard.retailerModule

object NotificationData {
    
    data class NotificationResponse(
        val data: List<Notify>,
        val status: Int
    )

    data class Notify(
        val created_at: String,
        val customer_id: Any,
        val id: Int,
        val is_read: Int,
        val message: String,
        val title: String,
        val updated_at: String,
        val user_id: Int
    )
    data class ReadNotification(
        val message: String,
        val status: Int
    )
}