package com.sm.spagent.model

data class ShopInfoDetails(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val shop_info: List<ShopInfoDetailsData>?,
  val errors: List<String>?,
)
