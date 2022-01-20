package com.sm.spagent.ui.activity.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sm.spagent.databinding.FragmentPersonalInfoDetailsBinding


class PersonalInfoDetailsFragment : Fragment() {

  private lateinit var pageViewModel: PageViewModel
  private var _binding: FragmentPersonalInfoDetailsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]
    _binding = FragmentPersonalInfoDetailsBinding.inflate(inflater, container, false)
    val root = binding.root



    return root
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