package com.sm.spagent.model

data class Bank(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val bank_names: List<BankData>?
)
