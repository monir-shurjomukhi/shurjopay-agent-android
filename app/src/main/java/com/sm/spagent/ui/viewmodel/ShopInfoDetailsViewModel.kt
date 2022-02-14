package com.sm.spagent.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sm.spagent.R
import com.sm.spagent.model.ShopInfoDetails
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ShopInfoDetailsViewModel(application: Application) : BaseViewModel(application) {

  private val _shopInfoDetails = MutableLiveData<ShopInfoDetails>()
  val shopInfoDetails: LiveData<ShopInfoDetails>
    get() = _shopInfoDetails

  fun getShopInfo(shopId: Int) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        authApiClient.getShopInfo(shopId)
      } catch (e: IOException) {
        Log.e(TAG, "getShopInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getShopInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _shopInfoDetails.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  companion object {
    private const val TAG = "ShopInfoDetailsViewModel"
  }
}
