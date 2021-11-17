package com.vkc.loyaltyme.api

import com.vkc.loyaltyme.activity.common.model.district.DistrictResponseModel
import com.vkc.loyaltyme.activity.common.model.new_register.NewRegisterModel
import com.vkc.loyaltyme.activity.common.model.register.RegisterModel
import com.vkc.loyaltyme.activity.common.model.resend_otp.ResendOTPModel
import com.vkc.loyaltyme.activity.common.model.state.StateResponseModel
import com.vkc.loyaltyme.activity.common.model.user_details.UserDetailsResponseModel
import com.vkc.loyaltyme.activity.common.model.verify_otp.VerifyOTPModel
import com.vkc.loyaltyme.activity.dealers.model.dealers.DealersModel
import com.vkc.loyaltyme.activity.home.model.app_version.AppVersionModel
import com.vkc.loyaltyme.activity.home.model.device_registration.DeviceRegistrationModel
import com.vkc.loyaltyme.activity.home.model.my_points.MyPointsModel
import com.vkc.loyaltyme.activity.inbox.model.notification.NotificationModel
import com.vkc.loyaltyme.activity.issue_points.model.submit_points.SubmitPointsResponse
import com.vkc.loyaltyme.activity.issue_points.model.user.UserModel
import com.vkc.loyaltyme.activity.issue_points.model.user_type.TypeModel
import com.vkc.loyaltyme.activity.profile.model.profile.ProfileModel
import com.vkc.loyaltyme.activity.profile.model.update_phone.UpdatePhoneModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("getstate")
    fun getStateResponse(): Call<StateResponseModel>

    @FormUrlEncoded
    @POST("getdistrict")
    fun getDistrictResponse(
        @Field("state") stateId: String
    ): Call<DistrictResponseModel>

    @FormUrlEncoded
    @POST("getuserdetailswithMobileUAE")
    fun getUserDetailsResponse(
        @Field("mobile") mobileNo: String,
        @Field("customer_id") customerID: String,
        @Field("imei_no") imeiNo: String
    ): Call<UserDetailsResponseModel>

    @FormUrlEncoded
    @POST("registration")
    fun getRegisterResponse(
        @Field("phone") phone: String,
        @Field("role") role: String,
        @Field("cust_id") customerID: String,
        @Field("contact_person") owner: String,
        @Field("city") city:String
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("newRegRequest")
    fun getNewRegisterResponse(
        @Field("customer_id") customerID: String,
        @Field("shop_name") shopName: String,
        @Field("state_name") stateName: String,
        @Field("district") district: String,
        @Field("city") city: String,
        @Field("pincode") pincode: String,
        @Field("contact_person") contactPerson: String,
        @Field("phone") phone: String,
        @Field("door_no") doorNo: String,
        @Field("address_line1") addressLine1: String,
        @Field("landmark") landmark: String,
        @Field("user_type") userType: String
    ): Call<NewRegisterModel>

    @FormUrlEncoded
    @POST("OTP_verification")
    fun getVerifyOTPResponse(
        @Field("otp") otp: String,
        @Field("role") role: String,
        @Field("cust_id") customerID: String,
        @Field("phone") mobileNo: String,
        @Field("isnewMobile") isNewMobile: String
    ): Call<VerifyOTPModel>

    @FormUrlEncoded
    @POST("resend_otp")
    fun getResendOTPResponse(
        @Field("role") role: String,
        @Field("cust_id") customerID: String
    ): Call<ResendOTPModel>

    @FormUrlEncoded
    @POST("getLoyalityPoints")
    fun getMyPointsResponse(
        @Field("cust_id") customerID: String,
        @Field("role") role: String
    ): Call<MyPointsModel>

    @POST("loyalty_dubai_appversion")
    fun getAppVersionResponse(): Call<AppVersionModel>

    @FormUrlEncoded
    @POST("device_registration")
    fun getDeviceRegistrationResponse(
        @Field("cust_id") customerID: String,
        @Field("role") role: String,
        @Field("device_id") deviceID: String
    ): Call<DeviceRegistrationModel>

    @FormUrlEncoded
    @POST("getDealerswithState")
    fun getDealersResponse(
        @Field("cust_id") customerID: String,
        @Field("role") role: String,
        @Field("search_key") searchKey: String
    ): Call<DealersModel>

    @FormUrlEncoded
    @POST("phoneUpdateOTP")
    fun getPhoneUpdateResponse(
        @Field("cust_id") customerID: String,
        @Field("role") role: String,
        @Field("phone") phone: String
    ): Call<UpdatePhoneModel>

    @FormUrlEncoded
    @POST("getProfile")
    fun getProfileResponse(
        @Field("cust_id") customerID: String,
        @Field("role") role: String
    ): Call<ProfileModel>

    @FormUrlEncoded
    @POST("NotificationsList")
    fun getNotificationResponse(
        @Field("cust_id") customerID: String,
        @Field("role") role: String
    ): Call<NotificationModel>

    @FormUrlEncoded
    @POST("fetchUserData")
    fun getUserResponse(
        @Field("cust_id") customerID: String,
        @Field("role") role: String
    ): Call<UserModel>

    @FormUrlEncoded
    @POST("getRetailerswithState")
    fun getUserType(
        @Field("cust_id") customerID: String
    ): Call<TypeModel>

    @FormUrlEncoded
    @POST("issueLoyalityPoints")
    fun getSubmitPointsResponse(
        @Field("userid") customerID: String,
        @Field("to_user_id") selectedID: String,
        @Field("to_role") toRole: String,
        @Field("points") points: String,
        @Field("role") role: String
    ): Call<SubmitPointsResponse>
}
