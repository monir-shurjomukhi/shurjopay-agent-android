package com.sm.spagent.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivityMerchantDetailsBinding
import com.sm.spagent.model.FragmentType
import com.sm.spagent.ui.adapter.MerchantDetailsPagerAdapter
import com.sm.spagent.utils.ACCOUNT_ID
import com.sm.spagent.utils.MERCHANT_ID
import com.sm.spagent.utils.NOMINEE_ID
import com.sm.spagent.utils.SHOP_ID

class MerchantDetailsActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMerchantDetailsBinding

  private var merchantId: Int = -1
  private var shopId: String = ""
  private var accountId: String = ""
  private var nomineeId: String = ""
  private var fragmentType: FragmentType = FragmentType.PERSONAL_INFO

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMerchantDetailsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = getString(R.string.merchant_details)

    merchantId = intent.getIntExtra(MERCHANT_ID, -1)
    Log.d(TAG, "onCreate: merchantId = $merchantId")
    shopId = intent.getStringExtra(SHOP_ID).toString()
    Log.d(TAG, "onCreate: shopId = $shopId")
    accountId = intent.getStringExtra(ACCOUNT_ID).toString()
    Log.d(TAG, "onCreate: accountId = $accountId")
    nomineeId = intent.getStringExtra(NOMINEE_ID).toString()
    Log.d(TAG, "onCreate: nomineeId = $nomineeId")

    val adapter = MerchantDetailsPagerAdapter(this, supportFragmentManager)
    binding.viewPager.adapter = adapter
    binding.tabLayout.setupWithViewPager(binding.viewPager)
  }

  fun getMerchantId() = merchantId

  fun getShopId(): Int {
    return if (shopId.isEmpty()) {
      -1
    } else {
      val shopIds = shopId.split(",")
      shopIds[0].toInt()
    }
  }

  fun getAccountId(): Int {
    return if (accountId.isEmpty()) {
      -1
    } else {
      val accountIds = accountId.split(",")
      accountIds[0].toInt()
    }
  }

  fun getNomineeId(): Int {
    return if (nomineeId.isEmpty()) {
      -1
    } else {
      val nomineeIds = nomineeId.split(",")
      nomineeIds[0].toInt()
    }
  }

  fun setFragmentType(fragmentType: FragmentType) {
    this.fragmentType = fragmentType
  }

  fun getFragmentType(): FragmentType {
    return this.fragmentType
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.merchant_details_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle item selection
    return when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }
      R.id.edit -> {
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  companion object {
    private const val TAG = "MerchantDetailsActivity"
  }
}
