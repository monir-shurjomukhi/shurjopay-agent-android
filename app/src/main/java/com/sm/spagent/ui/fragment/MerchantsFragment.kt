package com.sm.spagent.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.databinding.FragmentMerchantsBinding
import com.sm.spagent.ui.viewmodel.MerchantsViewModel

class MerchantsFragment : Fragment() {

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
    })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    private const val TAG = "MerchantsFragment"
  }
}
