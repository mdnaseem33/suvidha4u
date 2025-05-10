package com.martvalley.suvidha_u.dashboard.home

import com.martvalley.suvidha_u.dashboard.people.user.User

object Dashboard {
    data class RetailerResponse(
        val active_costomer: String,
        val credit_available: String,
        val credit_used: String,
        val anti_credit_available: String,
        val anti_credit_used: String,
        val todays_activation: String,
        val total_costomer: String,
        val bannerList: List<Banner>,
        val youtubeLinks: List<YoutubeLink>,
        val notificationCount:  Int?,
        val total_retailer: String,
        val retailer_active: String,

    )

    data class DistributorResponse(
        val active_retailer: Int,
        val credit_available: Int,
        val credit_used: Int,
        val todays_activation: Int,
        val total_retailer: Int
    )

    data class YoutbeLinksResponse(
        val youtubeLinks: List<YoutubeLink>
    )

    /*------------------------- distributor ---------------------------------*/


    data class ActiveDistributorListResponse(
        val active_costomer: Int, val active_costomer_list: ArrayList<ActiveCostomer>
    )

    data class ActiveCostomer(
        val address: String,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val deleted_at: Any,
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
        val updated_at: String
    )


    data class CreditUsedDistributorResponse(
        val credit_used: Int,
        val credit_used_count: Int,
        val credit_used_list: ArrayList<CreditUsed>
    )

    data class CreditUsed(
        val amount: Int,
        val closing: Int,
        val created_at: String,
        val id: Int,
        val notes: Any,
        val opening: Int,
        val reciever: Reciever,
        val reciever_id: Int,
        val sender_id: Int,
        val type: Any,
        val updated_at: String
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
        var status: Int,
        val updated_at: String
    )


    data class TodaysActivationDistributorResponse(
        val todays_activation: Int,
        val todays_activation_list: ArrayList<TodaysActivation>
    )

    data class TodaysActivation(
        val address: String,
        val author: Int,
        val balance: Int,
        val created_at: String,
        val deleted_at: Any,
        val email: String,
        val email_verified_at: Any,
        val gst: Any,
        val id: Int,
        val image: Any,
        val name: String,
        val owner_name: String,
        val phone: String,
        val role: Int,
        val state: String,
        var status: Int,
        val updated_at: String
    )

    /*------------------------- retailer ---------------------------------*/


    data class TodaysActivationRetailerResponse(
        val todays_activation: Int,
        val todays_activation_list: ArrayList<User.Customer>
    )

    data class TodaysActivationn(
        val aadhar_back_image: Any,
        val aadhar_front_image: Any,
        val author: Int,
        val bill_no: String,
        val created_at: String,
        val deleted_at: Any,
        val emi_amount: Int,
        val emi_months: Int,
        val first_intallment_date: String,
        val id: Int,
        val key_type: Int?,
        val image: Any,
        val imei1: String,
        val imei2: String,
        val last_sync: String,
        val model: Any,
        val name: String,
        val phone: String,
        val sign: Any,
        val sim1_network: Any,
        val sim1_number: Any,
        val sim2_network: Any,
        val sim2_number: Any,
        var status: Int,
        val updated_at: String,
        val is_link: String,
    )

    data class CreditUsedRetailerResponse(
        val credit_used: String,
        val credit_used_count: Int,
        val credit_used_list: ArrayList<CreditUsedd>,
        val sender: Int
    )

    data class CreditUsedd(
        val amount: Int,
        val created_at: String,
        val customer_id: Int,
        val id: Int,
        val recieved: Recieved,
        val sender_id: Int
    )

    data class Recieved(
        val aadhar_back_image: Any,
        val aadhar_front_image: Any,
        val author: Int,
        val bill_no: String,
        val created_at: String,
        val deleted_at: Any,
        val emi_amount: Int,
        val emi_months: Int,
        val key_type: Int?,
        val first_intallment_date: String,
        val id: Int,
        val image: Any,
        val imei1: Int,
        val imei2: Int,
        val last_sync: String,
        val model: String,
        val name: String?,
        val phone: String,
        val sign: Any,
        val sim1_network: String,
        val sim1_number: String,
        val sim2_network: String,
        val sim2_number: String,
        var status: Int,
        val updated_at: String,
        val is_link: String,
    )


    data class ActiveRetailerListResponse(
        val active_costomer: Int,
        val active_costomer_list: ArrayList<User.Customer>
    )

    data class ActiveCostomerr(
        val aadhar_back_image: String,
        val aadhar_front_image: String,
        val author: Int,
        val bill_no: String,
        val created_at: String,
        val deleted_at: Any,
        val emi_amount: Int,
        val emi_months: Int,
        val key_type: Int?,
        val first_intallment_date: String,
        val id: Int,
        val image: String,
        val imei1: String,
        val imei2: String,
        val last_sync: String,
        val model: String,
        val name: String,
        val phone: String,
        val sign: String,
        val sim1_network: String,
        val sim1_number: String,
        val sim2_network: String,
        val sim2_number: String,
        var status: Int,
        val updated_at: String,
        val is_link: String,
    )


    data class CreateUserRequest(
        val aadhar_back_image: String,
        val aadhar_front_image: String,
        val bill_no: String,
        val emi_amount: String,
        val emi_months: Int,
        val first_intallment_date: String,
        val image: String,
        val name: String,
        val phone: String,
        val sign: String,
        val qr_id: String,
    )

    data class CreateUserResponse(
        val customer_id: Int,
        val message: String,
        val qr_id: String,
        val status: Int
    )
}