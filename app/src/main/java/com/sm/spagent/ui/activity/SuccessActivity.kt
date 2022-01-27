package com.sm.spagent.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivitySuccessBinding
import com.sm.spagent.utils.ACCOUNT_ID
import com.sm.spagent.utils.MERCHANT_ID
import com.sm.spagent.utils.NOMINEE_ID
import com.sm.spagent.utils.SHOP_ID

class SuccessActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySuccessBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySuccessBinding.inflate(layoutInflater)
    setContentView(binding.root)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = getString(R.string.success)

    binding.showDetailsButton.setOnClickListener {
      val intent = Intent(this, MerchantDetailsActivity::class.java)
      intent.putExtra(MERCHANT_ID, 1)
      intent.putExtra(SHOP_ID, "1")
      intent.putExtra(ACCOUNT_ID, "1")
      intent.putExtra(NOMINEE_ID, "1")
      startActivity(intent)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // handle arrow click here
    if (item.itemId == android.R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }
}
