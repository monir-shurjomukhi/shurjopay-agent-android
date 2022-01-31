package com.sm.spagent.model

data class AccountInfo(
  val account_category: String,
  val account_type: String,
  val account_name: String,
  val account_no: String,
  val bank_name_or_mfs_id: Int,
  val bank_branch_name: String?,
  val routing_no: String?,
  val is_mfs: Int?,
  val shop_owner_id: Int,
  val account_id: Int?,
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val errors: List<String>?
)
