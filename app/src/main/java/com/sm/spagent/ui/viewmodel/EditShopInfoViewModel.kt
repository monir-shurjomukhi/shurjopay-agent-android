package com.sm.spagent.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sm.spagent.R
import com.sm.spagent.model.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class EditShopInfoViewModel(application: Application) : BaseViewModel(application) {

  private val _businessType = MutableLiveData<BusinessType>()
  val businessType: LiveData<BusinessType>
    get() = _businessType

  private val _division = MutableLiveData<Division>()
  val division: LiveData<Division>
    get() = _division

  private val _district = MutableLiveData<District>()
  val district: LiveData<District>
    get() = _district

  private val _policeStation = MutableLiveData<PoliceStation>()
  val policeStation: LiveData<PoliceStation>
    get() = _policeStation

  private val _shopInfoDetails = MutableLiveData<ShopInfoDetails>()
  val shopInfoDetails: LiveData<ShopInfoDetails>
    get() = _shopInfoDetails

  private val _shopInfo = MutableLiveData<ShopInfo>()
  val shopInfo: LiveData<ShopInfo>
    get() = _shopInfo

  fun getBusinessTypes() {
    viewModelScope.launch {
      val response = try {
        authApiClient.getBusinessTypes()
      } catch (e: IOException) {
        Log.e(TAG, "getBusinessTypes: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getBusinessTypes: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      }

      if (response.isSuccessful && response.body() != null) {
        _businessType.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun getDivisions() {
    viewModelScope.launch {
      val response = try {
        authApiClient.getDivisions()
      } catch (e: IOException) {
        Log.e(TAG, "getDivisions: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getDivisions: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      }

      if (response.isSuccessful && response.body() != null) {
        _division.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun getDistricts(divisionId: Int) {
    viewModelScope.launch {
      val response = try {
        authApiClient.getDistricts(divisionId)
      } catch (e: IOException) {
        Log.e(TAG, "getDistricts: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getDistricts: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      }

      if (response.isSuccessful && response.body() != null) {
        _district.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun getPoliceStations(districtId: Int) {
    viewModelScope.launch {
      val response = try {
        authApiClient.getPoliceStations(districtId)
      } catch (e: IOException) {
        Log.e(TAG, "getPoliceStations: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getPoliceStations: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      }

      if (response.isSuccessful && response.body() != null) {
        _policeStation.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

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

  fun submitShopInfo(shopInfo: ShopInfo) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        authApiClient.submitShopInfo(shopInfo)
      } catch (e: IOException) {
        Log.e(TAG, "submitShopInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "submitShopInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _shopInfo.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  companion object {
    private const val TAG = "EditShopInfoViewModel"
  }
}
