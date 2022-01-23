package com.sm.spagent.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sm.spagent.databinding.ActivityMerchantDetailsBinding
import com.sm.spagent.ui.adapter.MerchantDetailsPagerAdapter
import com.sm.spagent.utils.MERCHANT_ID

class MerchantDetailsActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMerchantDetailsBinding

  private var merchantId: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMerchantDetailsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    merchantId = intent.getIntExtra(MERCHANT_ID, -1)

    val adapter = MerchantDetailsPagerAdapter(this, supportFragmentManager)
    binding.viewPager.adapter = adapter
    binding.tabLayout.setupWithViewPager(binding.viewPager)
  }

  fun getMerchantId() = merchantId
}
