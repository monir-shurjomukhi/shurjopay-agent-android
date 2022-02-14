package com.sm.spagent.modelroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_bank_name")
data class ModelBankName(
  @PrimaryKey(autoGenerate = false)
  val id: String?,
  val bank_name: String?,
  val status: Int?,
  val is_bank: Int?,
  val is_mfs: Int?,
)
