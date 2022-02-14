package com.sm.spagent.modelroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_business_type")
data class ModelBusinessType(
  @PrimaryKey(autoGenerate = false)
  val id: String?,
  val business_type_name: String?,
  val status: Int?,
)
