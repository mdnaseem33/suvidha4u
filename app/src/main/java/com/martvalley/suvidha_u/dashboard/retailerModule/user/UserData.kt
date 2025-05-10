package com.martvalley.suvidha_u.dashboard.retailerModule.user

data class UserData(
    val name:String,
    val id: Long,
    val createdAt:String,
    val type:String,
    val model:String
){
    companion object {
        fun loadData():List<UserData> {
            return listOf(
                UserData("Deepak Singh Kathayat",17669797987,"2024-02-13","SuperKey","A52"),
                UserData("Rajesh Singh",23535234242,"2024-05-3","SmartKey","Y21e"),
            )
        }
    }
}
