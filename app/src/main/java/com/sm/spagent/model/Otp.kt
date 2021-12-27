package com.sm.spagent.model

data class Otp(
  val verify_code: String,
  val mobile_no: String,
  val message: String?
)
