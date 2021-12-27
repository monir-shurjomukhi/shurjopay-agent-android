package com.sm.spagent.modelroomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoShopOwnerInfo {
    @Insert
    fun insert(shopOwnerInfo: ModelShopOwnerInfo)

    @Update
    fun update(shopOwnerInfo: ModelShopOwnerInfo)

    @Delete
    fun delete(note: ModelShopOwnerInfo)

    @Query("delete from shop_owner_info")
    fun deleteAllNotes()

    @Query("select * from shop_owner_info order by owner_name asc")
    fun getAllNotes(): LiveData<List<ModelShopOwnerInfo>>
}