package com.sm.spagent.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sm.spagent.R

class HomeAdapter(private val context: Context, private val items: List<String>, private val icons: List<Int>) : BaseAdapter() {
  private var inflater: LayoutInflater? = null
  override fun getCount(): Int {
    return items.size
  }

  override fun getItem(position: Int): Any {
    return items[position]
  }

  override fun getItemId(position: Int): Long {
    return 0
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    var view = convertView
    if (inflater == null) {
      inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
    if (view == null) {
      view = inflater!!.inflate(R.layout.grid_item, parent, false)
    }
    val nameTextView = view?.findViewById<TextView>(R.id.itemNameTextView)
    nameTextView?.text = items[position]
    val iconImageView = view?.findViewById<ImageView>(R.id.itemImageView)
    iconImageView?.setImageResource(icons[position])
    return view!!
  }
}