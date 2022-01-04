package com.sm.spagent.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sm.spagent.R
import com.sm.spagent.model.Division
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PersonalInfoViewModel(application: Application) : BaseViewModel(application) {

  private val _division = MutableLiveData<Division>()
  val division: LiveData<Division>
    get() = _division

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

  companion object {
    private const val TAG = "PersonalInfoViewModel"
  }
}