package com.sm.spagent.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sm.spagent.databinding.FragmentHomeBinding
import com.sm.spagent.ui.activity.NewMerchantActivity
import com.sm.spagent.ui.activity.SuccessActivity

class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    binding.attendanceLayout.setOnClickListener {
//      startActivity(Intent(requireContext(), NewMerchantActivity::class.java))
    }
    binding.exitLayout.setOnClickListener {
      startActivity(Intent(requireContext(), SuccessActivity::class.java))
    }
    binding.newMerchantLayout.setOnClickListener {
      startActivity(Intent(requireContext(), NewMerchantActivity::class.java))
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
