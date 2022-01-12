package com.sm.spagent.model

data class ShopInfo(
  val shop_or_business_name: String,
  val tin_no: String?,
  val business_type_id: Int,
  val shop_size: String,
  val shop_addess: String,
  val perm_division_id: Int,
  val perm_district_id: Int,
  val perm_police_station_id: Int,
  val shop_gps_location: String,
  val trade_licence: String?,
  val shop_front_img: String,
  val shop_owner_id: Int,
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val errors: List<String>?
)
