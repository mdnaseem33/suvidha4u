package com.martvalley.suvidha_u.api

import com.martvalley.suvidha_u.dashboard.home.Dashboard
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.dashboard.people.user.User
import com.martvalley.suvidha_u.dashboard.retailerModule.NotificationData
import com.martvalley.suvidha_u.dashboard.retailerModule.UpcomingEmiData
import com.martvalley.suvidha_u.dashboard.retailerModule.WhatsNewData
import com.martvalley.suvidha_u.dashboard.retailerModule.fragments.AllTransactionData
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.CreateCustomerData
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.CustomerUpdateRequest
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.RequestAntiKey
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.RequestSmartKey
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.SmartKey
import com.martvalley.suvidha_u.dashboard.settings.Settings
import com.martvalley.suvidha_u.dashboard.settings.controls.Control
import com.martvalley.suvidha_u.dashboard.settings.controls.device.DeviceBasics
import com.martvalley.suvidha_u.dashboard.settings.controls.reminder.ChargesRequest
import com.martvalley.suvidha_u.forgot_pass.ForgotPass
import com.martvalley.suvidha_u.login.Auth
import com.martvalley.suvidha_u.login.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //<!------------------------------- login --------------------------->

    @POST("api/login")
    fun loginApi(@Body loginRequest: Login.LoginRequest): Call<Login.LoginResponse>

    @POST("api/me")
    fun getAuthApi(): Call<Auth.AuthResponse>

    //<!------------------------------- dashboard --------------------------->

    @Headers("Accept: application/json")
    @GET("api/retailers/dashboard")
    fun getRetailerDashboardApi(): Call<Dashboard.RetailerResponse>

    @Headers("Accept: application/json")
    @GET("api/distributors/dashboard")
    fun getDistributorDashboardApi(): Call<Dashboard.DistributorResponse>

    @Headers("Accept: application/json")
    @POST("api/customer/superkey/create")
    fun createSmartKey(@Body smartKey: SmartKey): Call<Dashboard.CreateUserResponse>

    @Headers("Accept: application/json")
    @POST("api/customer/smartkey/create")
    fun registerSmartKey(@Body smartKey: RequestSmartKey): Call<Dashboard.CreateUserResponse>

    @Headers("Accept: application/json")
    @POST("api/customer/smartkey/update")
    fun updateSmartKey(@Body smartKey: CustomerUpdateRequest): Call<Dashboard.CreateUserResponse>


    @Headers("Accept: application/json")
    @POST("api/customer/superkey/create")
    fun registerSuperKey(@Body smartKey: RequestSmartKey): Call<Dashboard.CreateUserResponse>

    @Headers("Accept: application/json")
    @POST("api/customer/anti/create")
    fun registerAntiKey(@Body smartKey: RequestAntiKey): Call<Dashboard.CreateUserResponse>

    @Headers("Accept: application/json")
    @POST("api/customer/homekey/create")
    fun registerHomeKey(@Body smartKey: RequestSmartKey): Call<Dashboard.CreateUserResponse>

    @Headers("Accept: application/json")
    @POST("api/customer/udharkey/create")
    fun registerUdharKey(@Body smartKey: RequestSmartKey): Call<Dashboard.CreateUserResponse>

    //<!------------------------------- people list --------------------------->

    @Headers("Accept: application/json")
    @GET("api/customer")
    fun getCustomerListApi(): Call<User.UserListResponse>

    @Headers("Accept: application/json")
    @GET("api/whatsnew/all")
    fun getWhatsAll(): Call<WhatsNewData.WhatsNewRequest>

    @Headers("Accept: application/json")
    @GET("api/customer/emi/upcoming")
    fun getUpcomingEmi(): Call<UpcomingEmiData.UpcomingEmiResponse>

    @Headers("Accept: application/json")
    @GET("api/notification/all")
    fun getNotificationAll(): Call<NotificationData.NotificationResponse>

    @Headers("Accept: application/json")
    @GET("api/notification/read")
    fun readNotificationAll(): Call<NotificationData.ReadNotification>

    @Headers("Accept: application/json")
    @GET("api/get/tutorials")
    fun getTutorialsListApi(): Call<Dashboard.YoutbeLinksResponse>

    @Headers("Accept: application/json")
    @GET("api/retailersss")
    fun getRetailerListApi(@Query("type") type: String? = null): Call<Retailer.RetailerListResponse>

    //<!-------------------------------- status change --------------------------->

    @Headers("Accept: application/json")
    @POST("api/retailer-status-change")
    fun retailerStatusChangeApi(@Body request: Retailer.StatusChangeRequest): Call<Retailer.StatusChangeResponse>


    @Headers("Accept: application/json")
    @POST("api/customer-status-change")
    fun userStatusChangeApi(@Body request: Retailer.StatusChangeRequest): Call<Retailer.StatusChangeResponse>

    //<!----------------------------------- delete -------------------------------->

    @Headers("Accept: application/json")
    @POST("api/retailers/delete")
    fun retailerDeleteApi(@Body request: Retailer.DeleteRequest): Call<Retailer.StatusChangeResponse>


    @Headers("Accept: application/json")
    @POST("api/customer/delete")
    fun userDeleteApi(@Body request: Retailer.DeleteRequest): Call<Retailer.StatusChangeResponse>


    //<!------------------------------- forgot password --------------------------->

    @Headers("Accept: application/json")
    @POST("api/forgot-password-request")
    fun forgotPasswordApi(@Body request: ForgotPass.Request): Call<ForgotPass.Response>

    //<!---------------------------------- add retailer --------------------------------->

    @Headers("Accept: application/json")
    @POST("api/retailer/store")
    fun addRetailerApi(@Body request: Retailer.AddRetailerRequest): Call<Retailer.StatusChangeResponse>

    @Headers("Accept: application/json")
    @POST("api/retailer/update")
    fun editRetailerApi(@Body request: Retailer.UpdateRetailerRequest): Call<Retailer.StatusChangeResponse>


    //<!------------------------------ view ---------------------------->


    @Headers("Accept: application/json")
    @GET("api/distributors/retailers/view/{id}")
    fun viewRetilerApi(
        @Path("id") id: String,
    ): Call<Retailer.ViewRetailerResponse>


    @Headers("Accept: application/json")
    @GET("api/customer/view/{id}")
    fun viewUserApi(
        @Path("id") id: String,
    ): Call<User.ViewUserDetailsResponse>

    //<!------------------------------- credit debit --------------------------->

    @Headers("Accept: application/json")
    @POST("api/fundmoney")
    fun creditRetailerApi(@Body request: Retailer.CreditRetailerRequest): Call<Retailer.StatusChangeResponse>

    @Headers("Accept: application/json")
    @POST("api/theft/transaction/credit")
    fun antiCreditRetailerApi(@Body request: Retailer.AntiCreditRetailerRequest): Call<Retailer.StatusChangeResponse>

    @Headers("Accept: application/json")
    @POST("api/theft/transaction/reverse")
    fun antiReverseRetailerApi(@Body request: Retailer.AntiReverseRetailerRequest): Call<Retailer.StatusChangeResponse>

    @Headers("Accept: application/json")
    @POST("api/debitmoney")
    fun debitRetailerApi(@Body request: Retailer.CreditRetailerRequest): Call<Retailer.StatusChangeResponse>


    //<!------------------------------- get all transactions/report --------------------------->


    @Headers("Accept: application/json")
    @GET("api/transaction/sender/all/show")
    fun getDistributorTransactionsApi(
        @Query("page") page: Int
    ): Call<Retailer.ViewAllTransactionResponse>

    @Headers("Accept: application/json")
    @GET("api/get/transaction/all")
    fun getAllTransaction(): Call<AllTransactionData.TransactionResponse>

    @Headers("Accept: application/json")
    @GET("api/transaction/reciever/all/show")
    fun getRetailerTransactionsApi(
        @Query("page") page: Int
    ): Call<Retailer.ViewAllTransactionResponse>

    //<!------------------------------- distributor dashboard apis --------------------------->

    @Headers("Accept: application/json")
    @GET("api/retailer/active")
    fun getActiveDistributorListApi(): Call<Dashboard.ActiveDistributorListResponse>


    @Headers("Accept: application/json")
    @GET("api/retailer/credit")
    fun getDistributorCreditUsedListApi(): Call<Dashboard.CreditUsedDistributorResponse>


    @Headers("Accept: application/json")
    @GET("api/retailer/today")
    fun getDistributorTodayActivationListApi(): Call<Dashboard.TodaysActivationDistributorResponse>


    //<!------------------------------- retailer dashboard apis --------------------------->


    @Headers("Accept: application/json")
    @GET("api/customer/active")
    fun getActiveRetailerListApi(): Call<Dashboard.ActiveRetailerListResponse>


    @Headers("Accept: application/json")
    @GET("api/customer/credit")
    fun getRetailerCreditUsedListApi(): Call<Dashboard.CreditUsedRetailerResponse>


    @Headers("Accept: application/json")
    @GET("api/customer/today")
    fun getRetailerTodayActivationListApi(): Call<Dashboard.TodaysActivationRetailerResponse>


    //<!------------------------------- password change --------------------------->


    @Headers("Accept: application/json")
    @POST("api/profile/password-change-save")
    fun passwordChangeApi(@Body request: Settings.PasswordChangeRequest): Call<Retailer.StatusChangeResponse>

    @Headers("Accept: application/json")
    @POST("api/retailer/change/password")
    fun passwordChangeReatiler(@Body request: Settings.PasswordChangeRetailerRequest): Call<Retailer.StatusChangeResponse>


    @Headers("Accept: application/json")
    @POST("api/profile/store")
    fun profileUpdateApi(@Body request: Settings.ProfileUpdateRequest): Call<Settings.ProfileUpdateResponse>

    @Headers("Accept: application/json")
    @GET("api/customer/view/{id}")
    fun getCustomerbyIdApi(
        @Path("id") id: String,
    ): Call<Control.GetCustomerResponse>


    @Headers("Accept: application/json")
    @GET("api/customer/emi/view/{id}")
    fun getEmiTransactionListApi(
        @Path("id") id: String,
    ): Call<Control.EmiTransactionListResponse>


    @Headers("Accept: application/json")
    @GET("api/customer/emi/record/view/{id}")
    fun getSingleEmiTransactionApi(
        @Path("id") id: String,
    ): Call<Control.SingleEmiTransactionResponse>


    @Headers("Accept: application/json")
    @POST("api/customer/emi/status/change")
    fun updateEmiTransactionDateApi(@Body request: Control.UpdateDateEmiRequest): Call<Retailer.StatusChangeResponse>

    @Headers("Accept: application/json")
    @POST("api/customer/charges/update")
    fun updateChargesCustomer(@Body request: ChargesRequest): Call<Retailer.StatusChangeResponse>


    @Headers("Accept: application/json")
    @POST("api/set/wallpaper")
    fun saveWallpaperApi(@Body request: Settings.SaveWallpaperRequest): Call<Settings.SaveWallpaperResponse>

    @Headers("Accept: application/json")
    @POST("api/set/photo")
    fun saveProfileImage(@Body request: Settings.SaveProfileRequest): Call<Settings.SaveWallpaperResponse>

    @Headers("Accept: application/json")
    @POST("api/set/payment-qr")
    fun saveQrApi(@Body request: Settings.SavePaymentRequest): Call<Settings.SaveWallpaperResponse>

    @Headers("Accept: application/json")
    @POST("api/set/frp")
    fun savefrpEmailApi(@Body request: Settings.SaveFrpEmailRequest): Call<Settings.SaveWallpaperResponse>

    @Headers("Accept: application/json")
    @POST("api/set/loan_prefix")
    fun saveLoanPrefixApi(@Body request: Settings.SaveLoanPrefixRequest): Call<Settings.SaveWallpaperResponse>

    @Headers("Accept: application/json")
    @POST("api/customer/store")
    fun userCreateApi(@Body loginRequest: Dashboard.CreateUserRequest): Call<Dashboard.CreateUserResponse>


    @Headers("Accept: application/json")
    @GET("api/customer/qr/{id}")
    fun getCustomerQRApi(
        @Path("id") id: String,
    ): Call<User.QRResponse>

    @Headers("Accept: application/json")
    @POST("api/send-wallpaper")
    fun sendWallpaperApi(@Body request: DeviceBasics.SendRequest): Call<DeviceBasics.DeviceInfoResponse>




    @Headers("Accept: application/json")
    @POST("api/send-audio")
    fun sendAudioApi(@Body request: DeviceBasics.SendRequest): Call<DeviceBasics.DeviceInfoResponse>


    @Headers("Accept: application/json")
    @POST("api/request-device-info")
    fun requestDeviceInfoApi(@Body request: DeviceBasics.DeviceInfoRequest): Call<DeviceBasics.DeviceInfoResponse>


    @Headers("Accept: application/json")
    @POST("api/action-update")
    fun submitActionApi(@Body request: Control.ActionUpdateRequest): Call<Control.ActionUpdateResponse>

    @Headers("Accept: application/json")
    @POST("api/action-update")
    fun YoutubeActionApi(@Body request: Control.ActionUpdateRequest): Call<Control.ActionUpdateResponse>

    @Headers("Accept: application/json")
    @POST("api/set-audio")
    fun setAudioApi(@Body request: Settings.SetAudioRequest): Call<Settings.SetAudioResponse>


    @Headers("Accept: application/json")
    @GET("api/get-device-info")
    fun getCustomerDeviceLastDataApi(
        @Query("customerId") customerId: String,
    ): Call<DeviceBasics.GetDeviceInfo>

    @Headers("Accept: application/json")
    @POST("api/surrender")
    fun    surrenderCustomer(
        @Query("customerId") customerId: String,
    ): Call<Control.SurrenderResponse>

    @Headers("Accept: application/json")
    @GET("api/customer/get/create")
    fun getCustomerCreateData( @Query("is_mobile") is_mobile: Char?, @Query("is_appliance") is_appliance: Char? , @Query("customer_id") customer_id: String?): Call<CreateCustomerData>

    @Headers("Accept: application/json")
    @GET("api/customer/get/create")
    fun getCustomerCreateData(): Call<CreateCustomerData>

}