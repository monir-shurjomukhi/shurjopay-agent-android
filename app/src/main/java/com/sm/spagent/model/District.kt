package com.sm.spagent.model

data class District(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val districts: List<DistrictData>?
)
