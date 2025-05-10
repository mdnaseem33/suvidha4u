package com.martvalley.suvidha_u.dashboard.model

import com.martvalley.suvidha_u.R

data class KeyModel(
    val image:Int,
    val name:String,
    val color:String
){
    companion object {
        fun loadData():List<KeyModel> {
            return listOf(
                KeyModel(R.drawable.img,"SmartKey",""),
                KeyModel(R.drawable.img,"SuperKey",""),
                KeyModel(R.drawable.img,"Home Appliance",""),
                KeyModel(R.drawable.img,"Udhar",""),
            )
        }
    }
}
