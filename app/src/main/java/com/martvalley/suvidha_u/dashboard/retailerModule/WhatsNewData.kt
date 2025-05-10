package com.martvalley.suvidha_u.dashboard.retailerModule

object WhatsNewData {
    class WhatsNewRequest : ArrayList<WhatsNewRequestItem>()

    data class WhatsNewRequestItem(
        val created_at: String,
        val id: Int,
        val message: String,
        val title: String,
        val updated_at: String
    )
}