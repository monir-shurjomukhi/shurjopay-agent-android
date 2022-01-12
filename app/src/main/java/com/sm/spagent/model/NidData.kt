package com.sm.spagent.model

data class NidData(
  val name: String?,
  val nameEn: String?,
  val father: String?,
  val mother: String?,
  val gender: String?,
  val profession: String?,
  val spouse: String?,
  val dob: String?,
  val permanentAddress: String?,
  val presentAddress: String?,
  val nationalId: String?,
  val photo: String?,
  val fatherEn: String?,
  val motherEn: String?,
  val spouseEn: String?,
  val permanentAddressEn: String?,
  val presentAddressEn: String?,
  val faceMatch_matched: String?,
  val faceMatch_percentage: String?,
)
