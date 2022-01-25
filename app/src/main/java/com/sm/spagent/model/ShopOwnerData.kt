package com.sm.spagent.model

data class ShopOwnerData(
  val id: Int?,
  val owner_name: String?,
  val father_name: String?,
  val mother_name: String?,
  val contact_no: String?,
  val email_address: String?,
  val nid_no: String?,
  val owner_dob: String?,
  val tin_no: String?,
  val perm_addess: String?,
  val division_name: String?,
  val district_name: String?,
  val police_station_name: String?,
  val perm_division_id: Int?,
  val perm_district_id: Int?,
  val perm_police_station_id: Int?,
  val owner_img: String?,
  val nid_front: String?,
  val nid_back: String?,
  val owner_signature: String?,
  val status: Int?,
  val shop_ids: String?,
  val settlement_ac_ids: String?,
  val nominee_ids: String?
)
