package com.sm.spagent.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivityEditPersonalInfoBinding

class EditPersonalInfoActivity : AppCompatActivity() {

  private lateinit var binding: ActivityEditPersonalInfoBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEditPersonalInfoBinding.inflate(layoutInflater)
    setContentView(binding.root)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = getString(R.string.edit_personal_info)
  }
}
