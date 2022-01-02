package com.sm.spagent.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentHomeBinding
import com.sm.spagent.ui.activity.NewMerchantActivity
import com.sm.spagent.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private var _binding: FragmentHomeBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    homeViewModel =
      ViewModelProvider(this)[HomeViewModel::class.java]

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val gridItems = listOf(
      getString(R.string.attendance),
      getString(R.string.exit),
      getString(R.string.new_merchant),
      getString(R.string.synchronize)
    )
    val gridIcons = listOf(
      R.drawable.ic_attendance,
      R.drawable.ic_exit,
      R.drawable.ic_new_merchant,
      R.drawable.ic_synchronize
    )
    //val adapter = HomeAdapter(requireContext(), gridItems, gridIcons)
    //binding.gridView.adapter = adapter

    binding.newMerchantLayout.setOnClickListener {
      startActivity(Intent(requireContext(), NewMerchantActivity::class.java))
    }
    binding.attendanceLayout.setOnClickListener {
//      startActivity(Intent(requireContext(), NewMerchantActivity::class.java))
    }
    binding.exitLayout.setOnClickListener {
//      startActivity(Intent(requireContext(), NewMerchantActivity::class.java))
    }
    binding.synchronizeLayout.setOnClickListener {
//      startActivity(Intent(requireContext(), NewMerchantActivity::class.java))
    }

    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}