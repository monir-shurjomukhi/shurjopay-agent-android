package com.sm.spagent.modelroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_nid_info")
data class ModelNidInfo(
  @PrimaryKey(autoGenerate = false)
  val id: String?,
  val fullNameEN: String?,
  val fullNameBN: String?,
  val fathersNameEN: String?,
  val fathersNameBN: String?,
  val mothersNameEN: String?,
  val mothersNameBN: String?,
  val presentAddressEN: String?,
  val presentAddressBN: String?,
  val permanentAddressEN: String?,
  val permanentAddressBN: String?,
  val spouseNameBN: String?,
  val gender: String?,
  val profession: String?,
  val dateOfBirth: String?,
  val nationalIdNumber: String?,
  val photoUrl: String?,
  val full_response: String?,
)
