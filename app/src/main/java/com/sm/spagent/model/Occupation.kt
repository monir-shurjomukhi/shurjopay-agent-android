package com.sm.spagent.model

data class Occupation(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val occupation_names: List<OccupationData>?
)
