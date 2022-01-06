package com.sm.spagent.model

data class Division(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val divisions: List<DivisionData>?
)
