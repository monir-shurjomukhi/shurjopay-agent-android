package com.sm.spagent.modelroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_nominee_info")
data class ModelNomineeInfo(
  @PrimaryKey(autoGenerate = false)
  val id: String?,
)
