package com.sm.spagent.modelroomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoDivisions {
  @Insert
  fun insert(modelDistricts: ModelDivisions)

  @Update
  fun update(modelDistricts: ModelDivisions)

  @Delete
  fun delete(modelDistricts: ModelDivisions)

  @Query("delete from qr_divisions")
  fun deleteAllDivision()

  @Query("select * from qr_divisions order by name_en asc")
  fun getAllDivision(): LiveData<List<ModelDivisions>>
}