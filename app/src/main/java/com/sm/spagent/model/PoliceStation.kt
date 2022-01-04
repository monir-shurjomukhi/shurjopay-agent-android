package com.sm.spagent.model

data class PoliceStation(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val police_stations: List<PoliceStationData>?
)
