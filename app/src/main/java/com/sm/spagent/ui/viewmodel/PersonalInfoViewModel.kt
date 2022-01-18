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

class PersonalInfoViewModel(application: Application) : BaseViewModel(application) {

  private val _division = MutableLiveData<Division>()
  val division: LiveData<Division>
    get() = _division

  private val _district = MutableLiveData<District>()
  val district: LiveData<District>
    get() = _district

  private val _policeStation = MutableLiveData<PoliceStation>()
  val policeStation: LiveData<PoliceStation>
    get() = _policeStation

  private val _ocr = MutableLiveData<Ocr>()
  val ocr: LiveData<Ocr>
    get() = _ocr

  private val _nid = MutableLiveData<Nid>()
  val nid: LiveData<Nid>
    get() = _nid

  private val _ownerInfo = MutableLiveData<OwnerInfo>()
  val ownerInfo: LiveData<OwnerInfo>
    get() = _ownerInfo

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

  fun ocrNid(ocr: Ocr) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        ocrApiClient.ocrNid(ocr)
      } catch (e: IOException) {
        Log.e(TAG, "ocrNid: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "ocrNid: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _ocr.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun getNidInfo(nid: Nid) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        authApiClient.getNidInfo(nid)
      } catch (e: IOException) {
        Log.e(TAG, "getNidInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getNidInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _nid.value = response.body()
      } else {
        message.value = R.string.unable_to_connect_please_try_again
      }
    }
  }

  fun submitOwnerInfo(ownerInfo: OwnerInfo) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        authApiClient.submitOwnerInfo(ownerInfo)
      } catch (e: IOException) {
        Log.e(TAG, "submitOwnerInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "submitOwnerInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _ownerInfo.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  companion object {
    private const val TAG = "PersonalInfoViewModel"
  }
}
