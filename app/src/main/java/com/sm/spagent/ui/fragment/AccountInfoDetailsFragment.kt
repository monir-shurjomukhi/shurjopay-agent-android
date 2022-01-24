package com.sm.spagent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentAccountInfoDetailsBinding
import com.sm.spagent.model.AccountCategory
import com.sm.spagent.ui.activity.MerchantDetailsActivity
import com.sm.spagent.ui.viewmodel.AccountInfoDetailsViewModel
import com.squareup.picasso.Picasso


class AccountInfoDetailsFragment : BaseFragment() {

  private lateinit var viewModel: AccountInfoDetailsViewModel
  private var _binding: FragmentAccountInfoDetailsBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[AccountInfoDetailsViewModel::class.java]
    _binding = FragmentAccountInfoDetailsBinding.inflate(inflater, container, false)
    val root = binding.root

    setupViews()
    observeData()

    val merchantId = (activity as MerchantDetailsActivity).getMerchantId()
    if (merchantId != -1) {
      viewModel.getAccountInfo(1)
    } else {
      shortSnack(binding.root, R.string.something_went_wrong)
    }

    return root
  }

  private fun setupViews() {
    binding.accountRadioGroup.setOnCheckedChangeListener { _, checkedId ->
      when (checkedId) {
        R.id.existingAccountRadioButton -> {
          binding.existingBankLayout.visibility = View.VISIBLE
          binding.mfsLayout.visibility = View.GONE
          binding.nomineeLayout.visibility = View.GONE
        }
        R.id.mfsAccountRadioButton -> {
          binding.existingBankLayout.visibility = View.GONE
          binding.mfsLayout.visibility = View.VISIBLE
          binding.nomineeLayout.visibility = View.GONE
        }
        R.id.newAccountRadioButton -> {
          binding.existingBankLayout.visibility = View.GONE
          binding.mfsLayout.visibility = View.GONE
          binding.nomineeLayout.visibility = View.VISIBLE
        }
      }
    }
  }

  private fun observeData() {
    viewModel.accountInfoDetails.observe(viewLifecycleOwner, {
      val accountInfo = it.account_info?.get(0)

      if (accountInfo?.is_mfs != null && accountInfo.is_mfs == 1) {
        if (!accountInfo.account_type.isNullOrEmpty()) {
          binding.mfsAccountTypeTextView.text = accountInfo.account_type
        }
        if (!accountInfo.account_name.isNullOrEmpty()) {
          binding.mfsAccountNameTextView.text = accountInfo.account_name
        }
        if (!accountInfo.account_no.isNullOrEmpty()) {
          binding.mfsAccountNumberTextView.text = accountInfo.account_no
        }
        if (!accountInfo.bank_name.isNullOrEmpty()) {
          binding.mfsNameTextView.text = accountInfo.bank_name
        }
      } else {
        if (!accountInfo?.account_type.isNullOrEmpty()) {
          binding.accountTypeTextView.text = accountInfo?.account_type
        }
        if (!accountInfo?.account_name.isNullOrEmpty()) {
          binding.accountNameTextView.text = accountInfo?.account_name
        }
        if (!accountInfo?.account_no.isNullOrEmpty()) {
          binding.accountNumberTextView.text = accountInfo?.account_no
        }
        if (!accountInfo?.bank_name.isNullOrEmpty()) {
          binding.bankNameTextView.text = accountInfo?.bank_name
        }
        if (!accountInfo?.bank_branch_name.isNullOrEmpty()) {
          binding.branchNameTextView.text = accountInfo?.bank_branch_name
        }
        if (!accountInfo?.routing_no.isNullOrEmpty()) {
          binding.routingNumberTextView.text = accountInfo?.routing_no
        }
      }
    })
  }

  companion object {
    fun newInstance(): AccountInfoDetailsFragment {
      return AccountInfoDetailsFragment()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
