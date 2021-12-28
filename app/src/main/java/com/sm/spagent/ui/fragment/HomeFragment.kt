package com.sm.spagent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sm.spagent.RecyclerAdapterHomeMenu
import com.sm.spagent.databinding.FragmentHomeBinding
import com.sm.spagent.modelroomdb.ModelHomeMenu
import com.sm.spagent.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //
    private lateinit var adapterHomeMenu: RecyclerAdapterHomeMenu
    private lateinit var homeMenuList: List<ModelHomeMenu>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        //
        loadHomeMenu()
        //val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        binding.sysRecyclerViewMenu.layoutManager = layoutManager
        adapterHomeMenu = RecyclerAdapterHomeMenu()
        adapterHomeMenu.submitList(homeMenuList)
        binding.sysRecyclerViewMenu.adapter = adapterHomeMenu
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadHomeMenu() {
        homeMenuList = listOf(
            ModelHomeMenu("Attendance", null, null),
            ModelHomeMenu("Change Password", null, null),
            ModelHomeMenu("Recommendation", null, null),
            ModelHomeMenu("New Merchandiser", null, null),
            ModelHomeMenu("Merchandiser List", null, null),
        )
    }
}