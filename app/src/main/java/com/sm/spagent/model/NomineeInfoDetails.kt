package com.sm.spagent.model

data class NomineeInfoDetails(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val nominee_info: List<NomineeInfoDetailsData>?,
  val errors: List<String>?,
)
