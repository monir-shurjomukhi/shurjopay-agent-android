package com.sm.spagent.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentShopInfoDetailsBinding
import com.sm.spagent.model.FragmentType
import com.sm.spagent.ui.activity.MerchantDetailsActivity
import com.sm.spagent.ui.viewmodel.ShopInfoDetailsViewModel
import com.squareup.picasso.Picasso


class ShopInfoDetailsFragment : BaseFragment() {

  private lateinit var viewModel: ShopInfoDetailsViewModel
  private var _binding: FragmentShopInfoDetailsBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[ShopInfoDetailsViewModel::class.java]
    _binding = FragmentShopInfoDetailsBinding.inflate(inflater, container, false)
    val root = binding.root

    observeData()
    val shopId = (activity as MerchantDetailsActivity).getShopId()
    if (shopId != -1) {
      viewModel.getShopInfo(shopId)
    } else {
      shortToast(R.string.shop_info_not_found)
    }

    return root
  }

  private fun observeData() {
    viewModel.progress.observe(viewLifecycleOwner) {
      if (it) {
        showProgress()
      } else {
        hideProgress()
      }
    }

    viewModel.message.observe(viewLifecycleOwner) {
      shortToast(it)
    }

    viewModel.shopInfoDetails.observe(viewLifecycleOwner) {
      val shopInfo = it.shop_info?.get(0)
      if (!shopInfo?.shop_or_business_name.isNullOrEmpty()) {
        binding.shopNameTextView.text = shopInfo?.shop_or_business_name
      }
      if (!shopInfo?.tin_no.isNullOrEmpty()) {
        binding.tinTextView.text = shopInfo?.tin_no
      }
      if (!shopInfo?.business_type_name.isNullOrEmpty()) {
        binding.businessTypeTextView.text = shopInfo?.business_type_name
      }
      if (!shopInfo?.shop_size.isNullOrEmpty()) {
        binding.shopSizeTextView.text = shopInfo?.shop_size
      }
      if (!shopInfo?.shop_addess.isNullOrEmpty()) {
        binding.addressTextView.text = shopInfo?.shop_addess
      }
      if (!shopInfo?.division_name.isNullOrEmpty()) {
        binding.divisionTextView.text = shopInfo?.division_name
      }
      if (!shopInfo?.district_name.isNullOrEmpty()) {
        binding.districtTextView.text = shopInfo?.district_name
      }
      if (!shopInfo?.police_station_name.isNullOrEmpty()) {
        binding.policeStationTextView.text = shopInfo?.police_station_name
      }
      if (!shopInfo?.shop_gps_location.isNullOrEmpty()) {
        binding.locationTextView.text = shopInfo?.shop_gps_location
      }

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/trade_license/${shopInfo?.trade_licence}")
        .placeholder(R.drawable.ic_baseline_image_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.tradeLicenseImageView)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/shop_front_img/${shopInfo?.shop_front_img}")
        .placeholder(R.drawable.ic_baseline_storefront_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.shopFrontImageView)
    }
  }

  companion object {
    fun newInstance(): ShopInfoDetailsFragment {
      return ShopInfoDetailsFragment()
    }

    private const val TAG = "ShopInfoDetailsFragment"
  }

  override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume: ")
    (activity as MerchantDetailsActivity).setFragmentType(FragmentType.SHOP_INFO)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
