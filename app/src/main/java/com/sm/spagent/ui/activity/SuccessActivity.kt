package com.sm.spagent.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivitySuccessBinding
import com.sm.spagent.utils.ACCOUNT_ID
import com.sm.spagent.utils.MERCHANT_ID
import com.sm.spagent.utils.NOMINEE_ID
import com.sm.spagent.utils.SHOP_ID

class SuccessActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySuccessBinding

  private var merchantId: Int = -1
  private var shopId: Int = -1
  private var accountId: Int = -1
  private var nomineeId: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySuccessBinding.inflate(layoutInflater)
    setContentView(binding.root)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = getString(R.string.success)

    merchantId = intent.getIntExtra(MERCHANT_ID, -1)
    Log.d(TAG, "onCreate: merchantId = $merchantId")
    shopId = intent.getIntExtra(SHOP_ID, -1)
    Log.d(TAG, "onCreate: shopId = $shopId")
    accountId = intent.getIntExtra(ACCOUNT_ID, -1)
    Log.d(TAG, "onCreate: accountId = $accountId")
    nomineeId = intent.getIntExtra(NOMINEE_ID, -1)
    Log.d(TAG, "onCreate: nomineeId = $nomineeId")

    binding.showDetailsButton.setOnClickListener {
      val intent = Intent(this, MerchantDetailsActivity::class.java)
      intent.putExtra(MERCHANT_ID, merchantId)
      intent.putExtra(SHOP_ID, shopId.toString())
      intent.putExtra(ACCOUNT_ID, accountId.toString())
      intent.putExtra(NOMINEE_ID, nomineeId.toString())
      startActivity(intent)
      finish()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // handle arrow click here
    if (item.itemId == android.R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  companion object {
    private const val TAG = "SuccessActivity"
  }
}
