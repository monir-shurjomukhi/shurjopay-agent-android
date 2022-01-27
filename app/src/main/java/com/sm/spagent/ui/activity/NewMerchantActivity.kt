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
