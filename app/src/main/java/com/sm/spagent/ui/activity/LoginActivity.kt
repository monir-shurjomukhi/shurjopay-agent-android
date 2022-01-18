package com.sm.spagent.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivityLoginBinding
import com.sm.spagent.model.Login
import com.sm.spagent.preference.AgentPreference
import com.sm.spagent.ui.viewmodel.LoginViewModel

class LoginActivity : BaseActivity() {
  private lateinit var preference: AgentPreference
  private lateinit var binding: ActivityLoginBinding
  private lateinit var viewModel: LoginViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    preference = AgentPreference(this)
    if (preference.getToken()?.isNotEmpty() == true) {
      startActivity(Intent(this, MainActivity::class.java))
      finishAffinity()
    }

    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

    binding.loginButton.setOnClickListener {
      login()
    }

    viewModel.progress.observe(this, {
      if (it) {
        showProgress()
      } else {
        hideProgress()
      }
    })

    viewModel.message.observe(this, {
      shortSnack(binding.root, it)
    })

    viewModel.login.observe(this, {
      when {
        it.sp_code.equals("200") -> {
          preference.putToken(it.token)
          startActivity(Intent(this, MainActivity::class.java))
          finishAffinity()
        }
        it.sp_code.equals("1064") -> {
          shortSnack(binding.root, R.string.mobile_number_or_password_did_not_match)
        }
      }
    })
  }

  private fun login() {
    val username = binding.phoneLayout.editText?.text.toString()
    val password = binding.passwordLayout.editText?.text.toString()

    if (username.isEmpty()) {
      binding.phoneLayout.error = getString(R.string.this_field_is_required)
      return
    } else {
      binding.phoneLayout.error = null
    }
    if (username.length != 11 || !username.startsWith("01")) {
      binding.phoneLayout.error = getString(R.string.mobile_number_is_invalid)
      return
    } else {
      binding.phoneLayout.error = null
    }
    if (password.isEmpty()) {
      binding.passwordLayout.error = getString(R.string.this_field_is_required)
      return
    } else {
      binding.passwordLayout.error = null
    }

    val login = Login(username, password, null, null, null, null, null)
    viewModel.login(login)
  }
}
