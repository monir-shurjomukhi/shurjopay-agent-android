package com.sm.spagent.model

data class NomineeInfoDetailsData(
  val id: Int?,
  val name: String?,
  val father_or_husband_name: String?,
  val mother_name: String?,
  val relation_name: String?,
  val relation_with_nominee_id: Int?,
  val contact_no: String?,
  val email_address: String?,
  val dob: String?,
  val nid_no: String?,
  val occupation_name: String?,
  val occupation_id: Int?,
  val addess: String?,
  val division_id: Int?,
  val division_name: String?,
  val district_id: Int?,
  val district_name: String?,
  val police_station_id: Int?,
  val police_station_name: String?,
  val shop_owner_id: Int?,
  val nominee_img: String?,
  val nid_front: String?,
  val nid_back: String?,
  val status: Int?,
  val nid_front_base64: String?,
  val nid_back_base64: String?,
  val nominee_img_base64: String?
)
