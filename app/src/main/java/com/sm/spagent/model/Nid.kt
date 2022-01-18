package com.sm.spagent.model

data class Nid(
  val person_photo: String,
  val national_id: String,
  val person_dob: String,
  val status: String?,
  val sp_code: String?,
  val message: String?,
  val nid_response: NidData?,
  val errors: List<String>?,
)
