package com.sm.spagent.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sm.spagent.databinding.ActivityNewMerchantBinding

class NewMerchantActivity : AppCompatActivity() {

  private lateinit var binding: ActivityNewMerchantBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityNewMerchantBinding.inflate(layoutInflater)
    setContentView(binding.root)
    //setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
  }
}