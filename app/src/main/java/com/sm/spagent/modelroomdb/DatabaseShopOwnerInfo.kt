package com.sm.spagent.modelroomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ModelShopOwnerInfo::class], version = 1)
abstract class DatabaseShopOwnerInfo : RoomDatabase() {
  abstract fun shopOwnerInfoDao(): DaoShopOwnerInfo

  companion object {
    private var instance: DatabaseShopOwnerInfo? = null

    @Synchronized
    fun getInstance(ctx: Context): DatabaseShopOwnerInfo {
      if (instance == null)
        instance = Room.databaseBuilder(
          ctx.applicationContext, DatabaseShopOwnerInfo::class.java,
          "shop_owner_info_database"
        )
          .fallbackToDestructiveMigration()
          .addCallback(roomCallback)
          .build()

      return instance!!

    }

    private val roomCallback = object : Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        populateDatabase(instance!!)
      }
    }

    private fun populateDatabase(db: DatabaseShopOwnerInfo) {
      val shopOwnerInfoDao = db.shopOwnerInfoDao()
      /*subscribeOnBackground {
      }*/
    }
  }
}