package com.sm.spagent.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sm.spagent.R
import com.sm.spagent.model.AccountInfoDetails
import com.sm.spagent.model.NomineeInfoDetails
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AccountInfoDetailsViewModel(application: Application) : BaseViewModel(application) {

  private val _accountInfoDetails = MutableLiveData<AccountInfoDetails>()
  val accountInfoDetails: LiveData<AccountInfoDetails>
    get() = _accountInfoDetails

  private val _nomineeInfoDetails = MutableLiveData<NomineeInfoDetails>()
  val nomineeInfoDetails: LiveData<NomineeInfoDetails>
    get() = _nomineeInfoDetails

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
        progress.value = false
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
      progress.value = true
      val response = try {
        authApiClient.getNomineeInfo(id)
      } catch (e: IOException) {
        Log.e(TAG, "getNomineeInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getNomineeInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _nomineeInfoDetails.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  companion object {
    private const val TAG = "AccountInfoDetailsViewModel"
  }
}
