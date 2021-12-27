package com.sm.spagent.modelroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_owner_info")
data class ModelShopOwnerInfo(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    val owner_name: String,
    val nid_no: String,
    val contact_no: String,
    val email_address: String,
    val nid_front: String,
    val nid_back: String,
    val owner_img: String,
    val perm_address: String,
    val perm_division_id: Int,
    val perm_district_id: Int,
    val perm_police_station_id: Int,
    val owner_dob: String,
    val is_sync: String,
)