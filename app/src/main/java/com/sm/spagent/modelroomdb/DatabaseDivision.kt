package com.sm.spagent.modelroomdb

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

abstract class DatabaseDivision : RoomDatabase() {
  abstract fun divisionDao(): DaoDivisions

  companion object {
    private var instance: DatabaseDivision? = null

    @Synchronized
    fun getInstance(ctx: Context): DatabaseDivision {
      if (instance == null)
        instance = Room.databaseBuilder(
          ctx.applicationContext, DatabaseDivision::class.java,
          "qr_divisions"
        )
          .fallbackToDestructiveMigration()
          .addCallback(roomCallback)
          .build()

      return instance!!

    }

    private val roomCallback = object : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        populateDatabase(instance!!)
      }
    }

    private fun populateDatabase(db: DatabaseDivision) {
      val divisionDao = db.divisionDao()
      /*subscribeOnBackground {
      }*/
    }
  }
}