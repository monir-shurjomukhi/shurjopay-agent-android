package com.sm.spagent.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.databinding.FragmentMerchantsBinding
import com.sm.spagent.ui.activity.MerchantDetailsActivity
import com.sm.spagent.ui.adapter.MerchantsAdapter
import com.sm.spagent.ui.viewmodel.MerchantsViewModel

class MerchantsFragment : BaseFragment() {

  private lateinit var viewModel: MerchantsViewModel
  private var _binding: FragmentMerchantsBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

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
    viewModel.shopOwner.observe(viewLifecycleOwner, { shopOwner ->
      Log.d(TAG, "shopOwner: $shopOwner")
      if (shopOwner.shop_owners.isNullOrEmpty()) {
        shortToast("Empty List")
      } else {
        binding.showOwnerRecyclerView.adapter = MerchantsAdapter(shopOwner.shop_owners, ::onMerchantItemClick)
      }
    })
  }

  private fun onMerchantItemClick(position: Int) {
    shortToast(position.toString())
    startActivity(Intent(requireContext(), MerchantDetailsActivity::class.java))
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    private const val TAG = "MerchantsFragment"
  }
}
