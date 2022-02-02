package com.sm.spagent.ui.activity

import android.os.Bundle
import com.sm.spagent.databinding.ActivityEditAccountInfoBinding

class EditAccountInfoActivity : BaseActivity() {

  private lateinit var binding: ActivityEditAccountInfoBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEditAccountInfoBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }
}
