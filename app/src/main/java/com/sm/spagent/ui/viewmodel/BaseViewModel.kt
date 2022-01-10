package com.sm.spagent.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sm.spagent.networking.ApiClient
import com.sm.spagent.networking.ApiInterface
import com.sm.spagent.preference.AgentPreference

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
  val progress = MutableLiveData<Boolean>()
  val message = MutableLiveData<Int>()
  private val preference = AgentPreference(application)

  val ocrApiClient: ApiInterface =
    ApiClient().getOcrApiClient("http://192.168.0.36:8000/api/v1/")
      .create(ApiInterface::class.java)
  val apiClient: ApiInterface =
    ApiClient().getApiClient("https://stagingapp.engine.shurjopayment.com/api/")
      .create(ApiInterface::class.java)
  val ocrApiClient: ApiInterface =
    ApiClient().getOcrApiClient("http://192.168.0.36:8000/api/v1/")
      .create(ApiInterface::class.java)
  val authApiClient: ApiInterface =
    ApiClient().getAuthApiClient(
      "https://stagingapp.engine.shurjopayment.com/api/",
      preference.getToken()!!
    ).create(ApiInterface::class.java)

  /*val apiClient: ApiInterface =
    ApiClient().getApiClient("http://192.168.10.42/QR_Merchant_Acquiring/api/")
      .create(ApiInterface::class.java)
  val authApiClient: ApiInterface =
    ApiClient().getAuthApiClient(
      "http://192.168.10.42/QR_Merchant_Acquiring/api/",
      preference.getToken()!!
    ).create(ApiInterface::class.java)*/

  init {
    Log.d(TAG, "token: ${preference.getToken()}")
  }

  companion object {
    private const val TAG = "BaseViewModel"
  }
}
