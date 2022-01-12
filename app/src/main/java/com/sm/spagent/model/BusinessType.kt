package com.sm.spagent.model

data class BusinessType(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val business_type_names: List<BusinessTypeData>?
)
