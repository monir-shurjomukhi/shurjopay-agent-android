package com.sm.spagent.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sm.spagent.R
import com.sm.spagent.databinding.ShopOwnerListItemBinding
import com.sm.spagent.model.ShopOwnerData
import com.squareup.picasso.Picasso


class MerchantsAdapter(
  private val list: List<ShopOwnerData>,
  private val onItemClicked: (position: Int) -> Unit
) :
  RecyclerView.Adapter<MerchantsAdapter.MyViewHolder>() {

  inner class MyViewHolder(val binding: ShopOwnerListItemBinding, private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
      binding.merchantCard.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      onItemClicked(adapterPosition)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val binding = ShopOwnerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MyViewHolder(binding, onItemClicked)
  }

  override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    with(holder) {
      with(list[position]) {
        Picasso.get()
          .load("https://stagingapp.engine.shurjopayment.com/shop_owner_img/${this.owner_img}")
          .placeholder(R.drawable.ic_baseline_person_24)
          .error(R.drawable.ic_baseline_broken_image_24)
          .into(binding.ownerImageView)
        binding.ownerNameTextView.text = this.owner_name
        binding.contactNoTextView.text = this.contact_no
        binding.addressTextView.text = this.perm_addess
        binding.policeStationTextView.text = this.police_station_name
        binding.districtTextView.text = holder.itemView.context.getString(R.string.district_name, this.district_name)
        binding.divisionTextView.text = this.division_name
      }
    }
  }

  override fun getItemCount(): Int {
    return list.size
  }
}