package com.sm.spagent.modelroomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ModelDistricts::class], version = 1)
abstract class DatabaseDistricts : RoomDatabase() {
    abstract fun districtsDao(): DaoDistricts
    companion object {
        private var instance: DatabaseDistricts? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseDistricts {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, DatabaseDistricts::class.java,
                    "qr_districts"
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

        private fun populateDatabase(db: DatabaseDistricts) {
            val districtsDao = db.districtsDao()
            /*subscribeOnBackground {
            }*/
        }
    }
}