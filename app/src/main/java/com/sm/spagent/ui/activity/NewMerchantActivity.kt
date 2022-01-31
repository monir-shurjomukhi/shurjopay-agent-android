package com.sm.spagent.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivityNewMerchantBinding

class NewMerchantActivity : AppCompatActivity(), StepperNavListener {

  private lateinit var binding: ActivityNewMerchantBinding

  private var shopOwnerId: Int = -1
  private var shopId: Int = -1
  private var accountId: Int = -1
  private var nomineeId: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityNewMerchantBinding.inflate(layoutInflater)
    setContentView(binding.root)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = getString(R.string.new_merchant)

    binding.stepper.setupWithNavController(findNavController(R.id.frame_stepper))
    binding.stepper.stepperNavListener = this
  }

  fun setShopOwnerId(id: Int) {
    shopOwnerId = id
  }

  fun getShopOwnerId(): Int {
    return shopOwnerId
  }

  fun setShopId(id: Int) {
    shopId = id
  }

  fun getShopId(): Int {
    return shopId
  }

  fun setAccountId(id: Int) {
    accountId = id
  }

  fun getAccountId(): Int {
    return accountId
  }

  fun setNomineeId(id: Int) {
    nomineeId = id
  }

  fun getNomineeId(): Int {
    return nomineeId
  }

  fun goToNextStep() {
    binding.stepper.goToNextStep()
  }

  override fun onCompleted() {
    Log.d(TAG, "onCompleted: ")
  }

  override fun onStepChanged(step: Int) {
    Log.d(TAG, "onStepChanged: $step")
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // handle arrow click here
    if (item.itemId == android.R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  companion object {
    private const val TAG = "NewMerchantActivity"
  }
}
