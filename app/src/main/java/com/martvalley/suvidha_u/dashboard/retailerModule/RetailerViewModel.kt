package com.martvalley.suvidha_u.dashboard.retailerModule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.SmartKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RetailerViewModel @Inject constructor() : ViewModel() {
    private val keyMutableLiveData: MutableLiveData<SmartKey> = MutableLiveData()

    var bankid = 1
    var name = ""
    var imeiNum = ""
    var signedImg = ""
    var mobileNumber = ""
    var loanNumber = ""
    var brandMobile = ""
    var modelNo = ""
    var financeCompany = ""
    var referenceName = ""
    var referMobileNo = ""
    var productPrice = 0
    var downPayment = 0
    var loanAmount = 0
    var emiType = 3
    var emiFrequency = 3
    var totalInstallment = ""
    var loanDate = ""
    var customerPhoto = ""
    var customerIDFront = ""
    var customerIDBack = ""
    var referPhotoFront = ""
    var referPhotoBack = ""


    val getData get() = keyMutableLiveData

    fun setData(input: SmartKey) {
        keyMutableLiveData.value = input
    }

}