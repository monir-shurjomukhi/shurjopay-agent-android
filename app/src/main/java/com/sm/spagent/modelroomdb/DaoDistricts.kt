package com.sm.spagent.modelroomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoDistricts {
    @Insert
    fun insert(modelDistricts: ModelDistricts)

    @Update
    fun update(modelDistricts: ModelDistricts)

    @Delete
    fun delete(modelDistricts: ModelDistricts)

    @Query("delete from qr_districts")
    fun deleteAllDistrict()

    @Query("select * from qr_districts order by name_en asc")
    fun getAllDistrict(): LiveData<List<ModelDistricts>>
}