package com.sm.spagent.model

data class AccountInfoDetails(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val account_info: List<AccountInfoDetailsData>?,
  val errors: List<String>?,
)
