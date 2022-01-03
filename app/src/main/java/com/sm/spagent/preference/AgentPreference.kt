package com.sm.spagent.preference

import android.content.Context
import android.content.SharedPreferences
import com.sm.spagent.utils.MOBILE_NUMBER
import com.sm.spagent.utils.PREFERENCE_TITLE
import com.sm.spagent.utils.TOKEN


class AgentPreference(context: Context) {

  private val preferences: SharedPreferences =
    context.getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE)
  private val editor: SharedPreferences.Editor = preferences.edit()

  fun putMobileNumber(mobileNumber: String?) {
    editor.putString(MOBILE_NUMBER, mobileNumber)
    editor.apply()
  }

  fun getMobileNumber(): String? {
    return preferences.getString(MOBILE_NUMBER, "")
  }

  fun putToken(token: String?) {
    editor.putString(TOKEN, token)
    editor.apply()
  }

  fun getToken(): String? {
    return preferences.getString(TOKEN, "")
  }
}
