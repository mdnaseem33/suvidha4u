package com.martvalley.suvidha_u.dashboard.retailerModule.key.model

import com.martvalley.suvidha_u.dashboard.people.user.User

data class CreateCustomerData(
    val banks: List<Bank>,
    val brands: List<Brand>,
    val loan_id: String,
    val customer: User.Customer?
)