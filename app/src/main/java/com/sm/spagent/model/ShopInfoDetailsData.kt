package com.sm.spagent.model

data class ShopInfoDetailsData(
  val id: Int?,
  val shop_or_business_name: String?,
  val tin_no: String?,
  val business_type_id: Int?,
  val business_type_Name: String?,
  val shop_size: String?,
  val shop_addess: String?,
  val shop_address_division_id: Int?,
  val division_name: String?,
  val shop_address_district_id: Int?,
  val district_name: String?,
  val shop_address_police_station_id: Int?,
  val police_station_name: String?,
  val shop_gps_location: String?,
  val trade_licence: String?,
  val shop_front_img: String?,
  val shop_owner_id: Int?,
  val status: Int?,
  val trade_licence_base64: String?,
  val shop_front_img_base64: String?
)
