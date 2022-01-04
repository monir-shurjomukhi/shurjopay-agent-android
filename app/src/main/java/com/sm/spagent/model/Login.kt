package com.sm.spagent.model

data class Login(
  val username: String,
  val password: String,
  val token: String?,
  val token_type: String?,
  val sp_code: String?,
  val message: String?,
  val expires_in: Int?
)
