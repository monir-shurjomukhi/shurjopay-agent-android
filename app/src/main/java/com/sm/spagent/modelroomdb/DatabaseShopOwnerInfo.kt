package com.sm.spagent.modelroomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ModelShopOwnerInfo::class], version = 1)
abstract class DatabaseShopOwnerInfo : RoomDatabase() {
    abstract fun shopOwnerInfoDao(): DaoShopOwnerInfo
}