package com.sm.spagent.modelroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_bank_branch_name")
data class ModelBankBranchName(
  @PrimaryKey(autoGenerate = false)
  val id: String?,
  val branch_name: String?,
  val status: Boolean,
)
