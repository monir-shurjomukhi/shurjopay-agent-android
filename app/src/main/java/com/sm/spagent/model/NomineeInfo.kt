package com.sm.spagent.model

data class NomineeInfo(
  val name: String,
  val father_or_husband_name: String,
  val mother_name: String,
  val relation_with_nominee_id: Int,
  val contact_no: String,
  val email_address: String?,
  val dob: String,
  val nid_no: String,
  val occupation_id: Int,
  val address: String,
  val division_id: Int,
  val district_id: Int,
  val police_station_id: Int,
  val shop_owner_id: Int,
  val nominee_img: String,
  val nid_front: String,
  val nid_back: String,
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val errors: List<String>?
)
