package com.sm.spagent.networking

import com.sm.spagent.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

  ///////////////////// GET ///////////////////

  @GET("qr-division-names")
  suspend fun getDivisions(): Response<Division>

  @GET("qr-district-names/{division_id}")
  suspend fun getDistricts(
    @Path("division_id") divisionId: Int
  ): Response<District>

  @GET("qr-police-station-names/{district_id}")
  suspend fun getPoliceStations(
    @Path("district_id") districtId: Int
  ): Response<PoliceStation>

  @GET("qr-business-type-names")
  suspend fun getBusinessTypes(): Response<BusinessType>

  @GET("qr-relation-names")
  suspend fun getRelations(): Response<Relation>

  @GET("qr-occupation-names")
  suspend fun getOccupations(): Response<Occupation>

  @GET("qr-bank-names")
  suspend fun getBanks(): Response<Bank>

  @GET("qr-mfs-names")
  suspend fun getMfs(): Response<Mfs>


  //////////////////// POST ///////////////////

  @POST("qr-login")
  suspend fun login(
    @Body login: Login
  ): Response<Login>

  @POST("nid_ocr_predict")
  suspend fun ocrNid(
    @Body ocr: Ocr
  ): Response<Ocr>

  @POST("info")
  suspend fun getHtml(
    @Body qrCode: QrCode
  ): Response<String>

  @POST("customer-register")
  suspend fun register(
    @Body registration: Registration
  ): Response<Registration>

  @POST("customer-verify-otp")
  suspend fun verifyOTP(
    @Body otp: Otp
  ): Response<Otp>

  @POST("customer-verify-account")
  suspend fun verifyAccount(
    @Body otp: Otp
  ): Response<Otp>

  @POST("customer-forgot-pass")
  suspend fun forgotPassword(
    @Body forgotPassword: ForgotPassword
  ): Response<ForgotPassword>

  @POST("customer-pass-change")
  suspend fun changePassword(
    @Body changePassword: ChangePassword
  ): Response<ChangePassword>

  @POST("customer-logout")
  suspend fun logout(
    @Body logout: Logout
  ): Response<Logout>
}
