package com.sm.spagent.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sm.spagent.modelroomdb.DaoShopOwnerInfo
import com.sm.spagent.modelroomdb.DatabaseShopOwnerInfo
import com.sm.spagent.modelroomdb.ModelShopOwnerInfo

class ShopOwnerRepository(application: Application) {
  private lateinit var shopOwnerInfoDao: DaoShopOwnerInfo
  private lateinit var allShopOwnerInfo: LiveData<List<ModelShopOwnerInfo>>
  private val databaseShopOwnerInfo = DatabaseShopOwnerInfo.getInstance(application)
}