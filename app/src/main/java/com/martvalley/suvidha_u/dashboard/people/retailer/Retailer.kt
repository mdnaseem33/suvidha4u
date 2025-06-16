package com.martvalley.suvidha_u.dashboard.people.retailer

object Retailer {

    data class RetailerModel(
        var id: String,
        var name: String,
        var email: String,
        var phone: String,
        var status: String,
        var state: String
    )


    data class RetailerListResponse(
        val users: ArrayList<User>
    )

    data class User(
        val address: String,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val email: String,
        val email_verified_at: Any,
        val gst: String,
        val id: Int,
        val image: Any,
        val name: String,
        val owner_name: String,
        val phone: String,
        val role: Int,
        val state: String,
        var status: Int,
        val updated_at: String,
        val anti_balance: Int,
        val deleted_at: Any,
    )


    data class StatusChangeRequest(
        val id: String,
        val status: String
    )

    data class StatusChangeResponse(
        val message: String,
        val status: Int
    )


    data class DeleteRequest(
        val id: String,
    )


    data class AddRetailerRequest(
        val address: String,
        val email: String,
        val gst: String,
        val name: String,
        val owner_name: String,
        val pass: String,
        val phone: String,
        val state: String,
        val member: String
    )

    data class ViewRetailerResponse(
        val active_customer: Int,
        val customer: Int,
        val inactive_customer: Int,
        val retailer: User,
        val transaction_sum: Int
    )

    data class Retailer(
        val address: String,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val email: String,
        val email_verified_at: Any,
        val gst: String,
        val id: Int,
        val image: Any,
        val name: String,
        val owner_name: String,
        val phone: String,
        val role: Int,
        val state: String,
        val status: Int,
        val updated_at: String
    )

    data class UpdateRetailerRequest(
        val address: String,
        val gst: String,
        val id: String,
        val name: String,
        val owner_name: String,
        val pass: String,
        val state: String
    )


    data class CreditRetailerRequest(
        val amount: String,
        val id: String,
        val notes: String?
    )

    data class AntiCreditRetailerRequest(
        val amount: String,
        val receiver_id: String,
        val description: String? = null,
    )

    data class AntiReverseRetailerRequest(
        val amount: String,
        val user_id: String,
        val description: String? = null,
    )


    data class AddRetailerErrorResponse(
        val errors: Errors,
        val message: String
    )

    data class Errors(
        val email: List<String>,
        val phone: List<String>
    )


    data class ViewAllTransactionResponse(
        val page_count: Double,
        val transactions: Transactions
    )

    data class Transactions(
        val current_page: Int,
        val data: ArrayList<Data>,
        val first_page_url: String,
        val from: Int,
        val last_page: Int,
        val last_page_url: String,
        val links: List<Link>,
        val next_page_url: Any,
        val path: String,
        val per_page: Int,
        val prev_page_url: String,
        val to: Int,
        val total: Int
    )

    data class Data(
        val amount: Int,
        val closing: Int,
        val created_at: String,
        val id: Int,
        val notes: String,
        val opening: Int,
        val reciever: Reciever,
        val reciever_id: Int,
        val sender: Sender,
        val sender_id: Int,
        val type: Any,
        val updated_at: String
    )

    data class Link(
        val active: Boolean,
        val label: String,
        val url: String
    )

    data class Reciever(
        val address: String,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val deleted_at: Any,
        val email: String,
        val email_verified_at: String,
        val gst: String,
        val id: Int,
        val image: Any,
        val name: String,
        val owner_name: String,
        val phone: String,
        val role: Int,
        val state: String,
        val status: Int,
        val updated_at: String
    )

    data class Sender(
        val address: String,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val deleted_at: Any,
        val email: String,
        val email_verified_at: String,
        val gst: String,
        val id: Int,
        val image: String,
        val name: String,
        val owner_name: String,
        val phone: String,
        val role: Int,
        val state: String,
        val status: Int,
        val updated_at: String
    )

}