package com.sm.spagent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.databinding.FragmentPersonalInfoDetailsBinding
import com.sm.spagent.ui.viewmodel.PersonalInfoDetailsViewModel


class PersonalInfoDetailsFragment : Fragment() {

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
    viewModel.getPersonalInfo(1)

    return root
  }

  private fun observeData() {
    viewModel.personalInfoDetails.observe(viewLifecycleOwner, {

    })
  }

  companion object {
    fun newInstance(): PersonalInfoDetailsFragment {
      return PersonalInfoDetailsFragment()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
