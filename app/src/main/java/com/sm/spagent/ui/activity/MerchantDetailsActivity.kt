package com.sm.spagent.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sm.spagent.databinding.ActivityMerchantDetailsBinding
import com.sm.spagent.ui.activity.pager.SectionsPagerAdapter

class MerchantDetailsActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMerchantDetailsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMerchantDetailsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
    binding.viewPager.adapter = sectionsPagerAdapter
    binding.tabLayout.setupWithViewPager(binding.viewPager)
  }
}
