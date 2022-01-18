package com.sm.spagent.model

data class ShopOwner(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val shop_owners: List<ShopOwnerData>?,
  val errors: List<String>?,
)
