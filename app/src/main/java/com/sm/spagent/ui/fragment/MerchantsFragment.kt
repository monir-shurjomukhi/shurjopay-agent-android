package com.sm.spagent.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.databinding.FragmentMerchantsBinding
import com.sm.spagent.model.ShopOwnerData
import com.sm.spagent.ui.activity.MerchantDetailsActivity
import com.sm.spagent.ui.adapter.MerchantsAdapter
import com.sm.spagent.ui.viewmodel.MerchantsViewModel
import com.sm.spagent.utils.ACCOUNT_ID
import com.sm.spagent.utils.MERCHANT_ID
import com.sm.spagent.utils.NOMINEE_ID
import com.sm.spagent.utils.SHOP_ID

class MerchantsFragment : BaseFragment() {

  private lateinit var viewModel: MerchantsViewModel
  private var _binding: FragmentMerchantsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  private var shopOwners: List<ShopOwnerData>? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel =
      ViewModelProvider(this)[MerchantsViewModel::class.java]

    _binding = FragmentMerchantsBinding.inflate(inflater, container, false)
    val root: View = binding.root

    observeData()

    viewModel.getShopOwners()

    return root
  }

  private fun observeData() {
    viewModel.shopOwner.observe(viewLifecycleOwner) { shopOwner ->
      Log.d(TAG, "shopOwner: $shopOwner")
      shopOwners = shopOwner.shop_owners
      if (shopOwners.isNullOrEmpty()) {
        shortToast("Empty List")
      } else {
        binding.showOwnerRecyclerView.adapter =
          MerchantsAdapter(shopOwners!!, ::onMerchantItemClick)
      }
    }
  }

  private fun onMerchantItemClick(position: Int) {
    val intent = Intent(requireContext(), MerchantDetailsActivity::class.java)
    intent.putExtra(MERCHANT_ID, shopOwners?.get(position)?.id)
    intent.putExtra(SHOP_ID, shopOwners?.get(position)?.shop_ids)
    intent.putExtra(ACCOUNT_ID, shopOwners?.get(position)?.settlement_ac_ids)
    intent.putExtra(NOMINEE_ID, shopOwners?.get(position)?.nominee_ids)
    startActivity(intent)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    private const val TAG = "MerchantsFragment"
  }
}
