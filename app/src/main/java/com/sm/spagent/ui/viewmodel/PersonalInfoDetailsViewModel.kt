package com.sm.spagent.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.sm.spagent.R
import com.sm.spagent.model.PersonalInfoDetails
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PersonalInfoDetailsViewModel(application: Application) : BaseViewModel(application) {

  private val _personalInfoDetails = MutableLiveData<PersonalInfoDetails>()
  val personalInfoDetails: LiveData<PersonalInfoDetails>
    get() = _personalInfoDetails

  fun getPersonalInfo(shopOwnerId: Int) {
    viewModelScope.launch {
      progress.value = true
      val response = try {
        authApiClient.getPersonalInfo(shopOwnerId)
      } catch (e: IOException) {
        Log.e(TAG, "getPersonalInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      } catch (e: HttpException) {
        Log.e(TAG, "getPersonalInfo: ${e.message}", e)
        progress.value = false
        message.value = R.string.unable_to_connect
        return@launch
      }

      progress.value = false
      if (response.isSuccessful && response.body() != null) {
        _personalInfoDetails.value = response.body()
      } else {
        message.value = R.string.unable_to_connect
      }
    }
  }

  companion object {
    private const val TAG = "PersonalInfoDetailsViewModel"
  }
}
