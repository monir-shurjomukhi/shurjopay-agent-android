package com.sm.spagent.model

data class Relation(
  val status: Boolean?,
  val sp_code: String?,
  val message: String?,
  val relation_names: List<RelationData>?
)
