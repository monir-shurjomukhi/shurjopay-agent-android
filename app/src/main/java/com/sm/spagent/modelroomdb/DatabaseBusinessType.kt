package com.sm.spagent.modelroomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ModelBusinessType::class], version = 1)
abstract class DatabaseBusinessType : RoomDatabase() {
    abstract fun businessTypeDao(): DaoBusinessType
    companion object {
        private var instance: DatabaseBusinessType? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseBusinessType {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, DatabaseBusinessType::class.java,
                    "qr_business_type"
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

        private fun populateDatabase(db: DatabaseBusinessType) {
            val businessTypeDao = db.businessTypeDao()
            /*subscribeOnBackground {
            }*/
        }
    }
}
