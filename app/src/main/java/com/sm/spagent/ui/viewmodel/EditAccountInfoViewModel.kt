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

class EditAccountInfoViewModel(application: Application) : BaseViewModel(application) {

  private val _bank = MutableLiveData<Bank>()
  val bank: LiveData<Bank>
    get() = _bank

  private val _mfs = MutableLiveData<Mfs>()
  val mfs: LiveData<Mfs>
    get() = _mfs

  private val _relation = MutableLiveData<Relation>()
  val relation: LiveData<Relation>
    get() = _relation

  private val _occupation = MutableLiveData<Occupation>()
  val occupation: LiveData<Occupation>
    get() = _occupation

  private val _division = MutableLiveData<Division>()
  val division: LiveData<Division>
    get() = _division

  private val _district = MutableLiveData<District>()
  val district: LiveData<District>
    get() = _district

  private val _policeStation = MutableLiveData<PoliceStation>()
  val policeStation: LiveData<PoliceStation>
    get() = _policeStation

  private val _accountInfoDetails = MutableLiveData<AccountInfoDetails>()
  val accountInfoDetails: LiveData<AccountInfoDetails>
    get() = _accountInfoDetails

  private val _nomineeInfoDetails = MutableLiveData<NomineeInfoDetails>()
  val nomineeInfoDetails: LiveData<NomineeInfoDetails>
    get() = _nomineeInfoDetails

  private val _accountInfo = MutableLiveData<AccountInfo>()
  val accountInfo: LiveData<AccountInfo>
    get() = _accountInfo

  private val _nomineeInfo = MutableLiveData<NomineeInfo>()
  val nomineeInfo: LiveData<NomineeInfo>
    get() = _nomineeInfo

  fun getBankNames() {
    viewModelScope.launch {
      val response = try {
        authApiClient.getBanks()
      } catch (e: IOException) {
        Log.e(TAG, "getBanks: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getBanks: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      }

      if (response.isSuccessful && response.body() != null) {
        _bank.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun getMfsNames() {
    viewModelScope.launch {
      val response = try {
        authApiClient.getMfs()
      } catch (e: IOException) {
        Log.e(TAG, "getMfs: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getMfs: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      }

      if (response.isSuccessful && response.body() != null) {
        _mfs.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun getRelations() {
    viewModelScope.launch {
      val response = try {
        authApiClient.getRelations()
      } catch (e: IOException) {
        Log.e(TAG, "getRelations: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getRelations: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      }

      if (response.isSuccessful && response.body() != null) {
        _relation.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun getOccupations() {
    viewModelScope.launch {
      val response = try {
        authApiClient.getOccupations()
      } catch (e: IOException) {
        Log.e(TAG, "getOccupations: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getOccupations: ${e.message}", e)
        message.value = R.string.unable_to_connect
        return@launch
      }

      if (response.isSuccessful && response.body() != null) {
        _occupation.value = response.body()
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

  fun getAccountInfo(id: Int) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        authApiClient.getAccountInfo(id)
      } catch (e: IOException) {
        Log.e(TAG, "getAccountInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getAccountInfo: ${e.message}", e)
        //progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _accountInfoDetails.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun getNomineeInfo(id: Int) {
    viewModelScope.launch {
      //progress.value = true
      val response = try {
        authApiClient.getNomineeInfo(id)
      } catch (e: IOException) {
        Log.e(TAG, "getNomineeInfo: ${e.message}", e)
        //progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getNomineeInfo: ${e.message}", e)
        //progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      //progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _nomineeInfoDetails.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun updateAccountInfo(accountInfo: AccountInfo) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        authApiClient.updateAccountInfo(accountInfo)
      } catch (e: IOException) {
        Log.e(TAG, "updateAccountInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "updateAccountInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _accountInfo.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  fun updateNomineeInfo(nomineeInfo: NomineeInfo) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        authApiClient.updateNomineeInfo(nomineeInfo)
      } catch (e: IOException) {
        Log.e(TAG, "updateNomineeInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "updateNomineeInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _nomineeInfo.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  companion object {
    private const val TAG = "EditAccountInfoViewModel"
  }
}
