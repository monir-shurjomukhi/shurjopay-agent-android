package com.sm.spagent.modelroomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoBusinessType {
    @Insert
    fun insert(businessType: ModelBusinessType)

    @Update
    fun update(businessType: ModelBusinessType)

    @Delete
    fun delete(businessType: ModelBusinessType)

    @Query("delete from qr_business_type")
    fun deleteAllBusinessType()

    @Query("select * from qr_business_type order by business_type_name asc")
    fun getAllBusinessType(): LiveData<List<ModelBusinessType>>
}