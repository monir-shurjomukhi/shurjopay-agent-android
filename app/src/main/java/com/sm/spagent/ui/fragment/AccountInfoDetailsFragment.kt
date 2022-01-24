package com.sm.spagent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentPersonalInfoDetailsBinding
import com.sm.spagent.ui.activity.MerchantDetailsActivity
import com.sm.spagent.ui.viewmodel.PersonalInfoDetailsViewModel
import com.squareup.picasso.Picasso


class AccountInfoDetailsFragment : BaseFragment() {

  private lateinit var viewModel: PersonalInfoDetailsViewModel
  private var _binding: FragmentPersonalInfoDetailsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[PersonalInfoDetailsViewModel::class.java]
    _binding = FragmentPersonalInfoDetailsBinding.inflate(inflater, container, false)
    val root = binding.root

    observeData()
    val merchantId = (activity as MerchantDetailsActivity).getMerchantId()
    if (merchantId != -1) {
      viewModel.getPersonalInfo(merchantId)
    } else {
      shortSnack(binding.root, R.string.something_went_wrong)
    }

    return root
  }

  private fun observeData() {
    viewModel.personalInfoDetails.observe(viewLifecycleOwner, {
      val shopOwner = it.shop_owner?.get(0)
      if (shopOwner?.owner_name.isNullOrEmpty()) {
        binding.ownerNameTextView.text = "N/A"
      } else {
        binding.ownerNameTextView.text = shopOwner?.owner_name
      }
      if (shopOwner?.father_name.isNullOrEmpty()) {
        binding.fathersNameTextView.text = "N/A"
      } else {
        binding.fathersNameTextView.text = shopOwner?.father_name
      }
      if (shopOwner?.mother_name.isNullOrEmpty()) {
        binding.mothersNameTextView.text = "N/A"
      } else {
        binding.mothersNameTextView.text = shopOwner?.mother_name
      }
      if (shopOwner?.contact_no.isNullOrEmpty()) {
        binding.contactNoTextView.text = "N/A"
      } else {
        binding.contactNoTextView.text = shopOwner?.contact_no
      }
      if (shopOwner?.email_address.isNullOrEmpty()) {
        binding.emailTextView.text = "N/A"
      } else {
        binding.emailTextView.text = shopOwner?.email_address
      }
      if (shopOwner?.nid_no.isNullOrEmpty()) {
        binding.nidTextView.text = "N/A"
      } else {
        binding.nidTextView.text = shopOwner?.nid_no
      }
      if (shopOwner?.owner_dob.isNullOrEmpty()) {
        binding.dobTextView.text = "N/A"
      } else {
        binding.dobTextView.text = shopOwner?.owner_dob
      }
      if (shopOwner?.tin_no.isNullOrEmpty()) {
        binding.tinTextView.text = "N/A"
      } else {
        binding.tinTextView.text = shopOwner?.tin_no
      }
      if (shopOwner?.perm_addess.isNullOrEmpty()) {
        binding.addressTextView.text = "N/A"
      } else {
        binding.addressTextView.text = shopOwner?.perm_addess
      }
      if (shopOwner?.division_name.isNullOrEmpty()) {
        binding.divisionTextView.text = "N/A"
      } else {
        binding.divisionTextView.text = shopOwner?.division_name
      }
      if (shopOwner?.district_name.isNullOrEmpty()) {
        binding.districtTextView.text = "N/A"
      } else {
        binding.districtTextView.text = shopOwner?.district_name
      }
      if (shopOwner?.police_station_name.isNullOrEmpty()) {
        binding.policeStationTextView.text = "N/A"
      } else {
        binding.policeStationTextView.text = shopOwner?.police_station_name
      }

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/shop_owner_img/${shopOwner?.owner_img}")
        .placeholder(R.drawable.ic_baseline_person_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.ownerImageView)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/nid_img/frontside/${shopOwner?.nid_front}")
        .placeholder(R.drawable.ic_baseline_credit_card_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.ownerNIDFrontImageView)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/nid_img/backside/${shopOwner?.nid_back}")
        .placeholder(R.drawable.ic_baseline_credit_card_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.ownerNIDBackImageView)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/owner_signature/${shopOwner?.owner_signature}")
        .placeholder(R.drawable.ic_baseline_gesture_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.ownerSignatureImageView)
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
