package com.sm.spagent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.sm.spagent.databinding.RecyclerViewHomeMenuBinding
import com.sm.spagent.modelroomdb.ModelHomeMenu

class RecyclerAdapterHomeMenu : RecyclerView.Adapter<RecyclerAdapterHomeMenu.ViewHolder>() {
    private val adapterItemList = mutableListOf<ModelHomeMenu>()
    private var _binding: RecyclerViewHomeMenuBinding? = null
    private val binding get() = _binding!!
    fun submitList(newData: List<ModelHomeMenu>) {
        adapterItemList.clear()
        adapterItemList.addAll(newData)
        adapterItemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return adapterItemList.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        _binding = RecyclerViewHomeMenuBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder){
            with(adapterItemList[position]){
                binding.sysTvTitle.text = this.title
            }
        }
    }

    inner class ViewHolder(val itemBind: RecyclerViewHomeMenuBinding) :
        RecyclerView.ViewHolder(binding.root)
}