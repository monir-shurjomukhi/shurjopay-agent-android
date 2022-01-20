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
    fun delete(shopOwnerInfo: ModelShopOwnerInfo)

    @Query("delete from shop_owner_info")
    fun deleteAllShopOwnerInfo()

    @Query("select * from shop_owner_info order by owner_name asc")
    fun getAllShopOwnerInfo(): LiveData<List<ModelShopOwnerInfo>>
}