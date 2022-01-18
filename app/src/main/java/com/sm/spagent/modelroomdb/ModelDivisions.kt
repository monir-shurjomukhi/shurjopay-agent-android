package com.sm.spagent.modelroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_divisions")
data class ModelDivisions(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name_en: String?,
    val name_bn: String?,
    val status: Int?,
)
//