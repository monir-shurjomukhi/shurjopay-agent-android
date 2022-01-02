package com.sm.spagent.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivityNewMerchantBinding

class NewMerchantActivity : AppCompatActivity(), StepperNavListener {

  private lateinit var binding: ActivityNewMerchantBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityNewMerchantBinding.inflate(layoutInflater)
    setContentView(binding.root)
    //setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)


    binding.stepper.setupWithNavController(findNavController(R.id.frame_stepper))
    binding.stepper.stepperNavListener = this
  }

  fun goToNextStep() {
    binding.stepper.goToNextStep()
  }

  override fun onCompleted() {
    Log.d(TAG, "onCompleted: ")
  }

  override fun onStepChanged(step: Int) {
    Log.d(TAG, "onStepChanged: ")
  }

  companion object {
    private const val TAG = "NewMerchantActivity"
  }
}