package com.sm.spagent.model

data class PersonalInfo(
  val owner_name: String,
  val contact_no: String?,
  val email_address: String,
  val nid_no: String,
  val tin_no: String,
  val perm_addess: String?,
  val perm_division_id: String?,
  val perm_district_id: String?,
  val perm_police_station_id: String?,
  val owner_dob: String?,
  val owner_img: String?,
  val nid_front: String?,
  val nid_back: String?
)
