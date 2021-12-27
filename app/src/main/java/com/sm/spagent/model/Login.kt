package com.sm.spagent.model

data class Login(
  val mobile_no: String,
  val ch_password: String,
  val message: String?,
  val customer_name: String?
)
