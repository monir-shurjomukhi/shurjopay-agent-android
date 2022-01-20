package com.sm.spagent.model

data class PersonalInfoDetails(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val shop_owner: List<ShopOwnerData>?,
  val errors: List<String>?,
)
