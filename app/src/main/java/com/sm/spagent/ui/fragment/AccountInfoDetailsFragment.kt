package com.sm.spagent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentAccountInfoDetailsBinding
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

    val accountId = (activity as MerchantDetailsActivity).getAccountId()
    if (accountId != -1) {
      viewModel.getAccountInfo(accountId)
    }/* else {
      shortSnack(binding.accountNameTextView, R.string.something_went_wrong)
    }*/

    val nomineeId = (activity as MerchantDetailsActivity).getNomineeId()
    if (nomineeId != -1) {
      viewModel.getNomineeInfo(nomineeId)
    }/* else {
      shortSnack(binding.nomineeNameTextView, R.string.something_went_wrong)
    }*/

    if (accountId == -1 && nomineeId == -1) {
      shortToast(R.string.account_info_not_found)
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
    viewModel.progress.observe(viewLifecycleOwner, {
      if (it) {
        showProgress()
      } else {
        hideProgress()
      }
    })

    viewModel.message.observe(viewLifecycleOwner, {
      shortToast(it)
    })

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

    viewModel.nomineeInfoDetails.observe(viewLifecycleOwner, {
      val nomineeInfo = it.nominee_info?.get(0)
      if (!nomineeInfo?.name.isNullOrEmpty()) {
        binding.nomineeNameTextView.text = nomineeInfo?.name
      }
      if (!nomineeInfo?.father_or_husband_name.isNullOrEmpty()) {
        binding.fathersNameTextView.text = nomineeInfo?.father_or_husband_name
      }
      if (!nomineeInfo?.mother_name.isNullOrEmpty()) {
        binding.mothersNameTextView.text = nomineeInfo?.mother_name
      }
      if (!nomineeInfo?.relation_name.isNullOrEmpty()) {
        binding.relationTextView.text = nomineeInfo?.relation_name
      }
      if (!nomineeInfo?.contact_no.isNullOrEmpty()) {
        binding.contactNoTextView.text = nomineeInfo?.contact_no
      }
      if (!nomineeInfo?.email_address.isNullOrEmpty()) {
        binding.emailTextView.text = nomineeInfo?.email_address
      }
      if (!nomineeInfo?.dob.isNullOrEmpty()) {
        binding.dobTextView.text = nomineeInfo?.dob
      }
      if (!nomineeInfo?.nid_no.isNullOrEmpty()) {
        binding.nidTextView.text = nomineeInfo?.nid_no
      }
      if (!nomineeInfo?.occupation_name.isNullOrEmpty()) {
        binding.occupationTextView.text = nomineeInfo?.occupation_name
      }
      if (!nomineeInfo?.addess.isNullOrEmpty()) {
        binding.addressTextView.text = nomineeInfo?.addess
      }
      if (!nomineeInfo?.division_name.isNullOrEmpty()) {
        binding.divisionTextView.text = nomineeInfo?.division_name
      }
      if (!nomineeInfo?.district_name.isNullOrEmpty()) {
        binding.districtTextView.text = nomineeInfo?.district_name
      }
      if (!nomineeInfo?.police_station_name.isNullOrEmpty()) {
        binding.policeStationTextView.text = nomineeInfo?.police_station_name
      }

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/nominee_img/${nomineeInfo?.nominee_img}")
        .placeholder(R.drawable.ic_baseline_person_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.nomineeImageView)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/nid_img/frontside/${nomineeInfo?.nid_front}")
        .placeholder(R.drawable.ic_baseline_credit_card_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.nomineeNIDFrontImageView)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/nid_img/backside/${nomineeInfo?.nid_back}")
        .placeholder(R.drawable.ic_baseline_credit_card_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.nomineeNIDBackImageView)
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
