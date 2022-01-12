package com.sm.spagent.model

data class Mfs(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val mfs_names: List<MfsData>?
)
