package com.sm.spagent.modelroomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopOwnerInfoDao {
    @Insert
    fun insert(shopOwnerInfo: ShopOwnerInfoModel)

    @Update
    fun update(shopOwnerInfo: ShopOwnerInfoModel)

    @Delete
    fun delete(note: ShopOwnerInfoModel)

    @Query("delete from shop_owner_info")
    fun deleteAllNotes()

    @Query("select * from shop_owner_info order by owner_name desc")
    fun getAllNotes(): LiveData<List<ShopOwnerInfoModel>>
}