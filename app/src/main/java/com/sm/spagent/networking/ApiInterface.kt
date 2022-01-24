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

  @GET("qr-creator-all-shop-owner")
  suspend fun getShopOwners(): Response<ShopOwner>

  @GET("qr-single-shop-owner-info/{shop_owner_id}")
  suspend fun getPersonalInfo(
    @Path("shop_owner_id") shopOwnerId: Int
  ): Response<PersonalInfoDetails>

  @GET("qr-single-shop-info/{shop_id}")
  suspend fun getShopInfo(
    @Path("shop_id") shopId: Int
  ): Response<ShopInfoDetails>

  @GET("qr-single-settlement-ac-info/{id}")
  suspend fun getAccountInfo(
    @Path("id") id: Int
  ): Response<AccountInfoDetails>

  @GET("qr-single-nominee-info/{id}")
  suspend fun getNomineeInfo(
    @Path("id") id: Int
  ): Response<NomineeInfoDetails>


  //////////////////// POST ///////////////////

  @POST("qr-login")
  suspend fun login(
    @Body login: Login
  ): Response<Login>

  @POST("nid_ocr_predict/")
  suspend fun ocrNid(
    @Body ocr: Ocr
  ): Response<Ocr>

  @POST("qr-nid-information")
  suspend fun getNidInfo(
    @Body nid: Nid
  ): Response<Nid>

  @POST("qr-store-shop-owner-info")
  suspend fun submitOwnerInfo(
    @Body ownerInfo: OwnerInfo
  ): Response<OwnerInfo>

  @POST("qr-store-shop-info")
  suspend fun submitShopInfo(
    @Body shopInfo: ShopInfo
  ): Response<ShopInfo>

  @POST("qr-store-settlement-ac-info")
  suspend fun submitAccountInfo(
    @Body accountInfo: AccountInfo
  ): Response<AccountInfo>

  @POST("qr-save-nominee-info")
  suspend fun submitNomineeInfo(
    @Body nomineeInfo: NomineeInfo
  ): Response<NomineeInfo>
}
