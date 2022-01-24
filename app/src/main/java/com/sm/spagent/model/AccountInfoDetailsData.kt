package com.sm.spagent.model

data class AccountInfoDetailsData(
  val id: Int?,
  val account_category: String?,
  val account_type: String?,
  val account_name: String?,
  val account_no: String?,
  val bank_name: String?,
  val bank_name_or_mfs_id: Int?,
  val bank_branch_name: String?,
  val routing_no: String?,
  val is_mfs: Int?,
  val shop_owner_id: Int?,
  val status: Int?,
)
