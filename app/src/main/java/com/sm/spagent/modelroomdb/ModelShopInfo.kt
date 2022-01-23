package com.sm.spagent.modelroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_info")
data class ModelShopInfo(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    val shop_owner_id: String,
)
///