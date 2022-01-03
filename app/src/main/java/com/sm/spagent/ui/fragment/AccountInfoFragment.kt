package com.sm.spagent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentAccountInfoBinding
import com.sm.spagent.ui.activity.NewMerchantActivity
import com.sm.spagent.ui.viewmodel.DashboardViewModel

class AccountInfoFragment : Fragment() {

  private lateinit var dashboardViewModel: DashboardViewModel
  private var _binding: FragmentAccountInfoBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

    _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
    val root: View = binding.root

    binding.accountRadioGroup.setOnCheckedChangeListener { _, checkedId ->
      when (checkedId) {
        R.id.existingAccountRadioButton -> {
          binding.accountInfoLayout.visibility = View.VISIBLE
          binding.nomineeInfoLayout.visibility = View.GONE
          binding.mfsInfoLayout.visibility = View.GONE
        }
        R.id.newAccountRadioButton -> {
          binding.accountInfoLayout.visibility = View.GONE
          binding.nomineeInfoLayout.visibility = View.VISIBLE
          binding.mfsInfoLayout.visibility = View.GONE
        }
        R.id.mfsAccountRadioButton -> {
          binding.accountInfoLayout.visibility = View.GONE
          binding.nomineeInfoLayout.visibility = View.GONE
          binding.mfsInfoLayout.visibility = View.VISIBLE
        }
      }
    }

    binding.saveNextButton.setOnClickListener {
      (activity as NewMerchantActivity).goToNextStep()
    }

    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
