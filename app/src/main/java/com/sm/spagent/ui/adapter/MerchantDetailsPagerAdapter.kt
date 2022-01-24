package com.sm.spagent.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sm.spagent.R
import com.sm.spagent.ui.fragment.AccountInfoDetailsFragment
import com.sm.spagent.ui.fragment.AccountInfoFragment
import com.sm.spagent.ui.fragment.PersonalInfoDetailsFragment
import com.sm.spagent.ui.fragment.ShopInfoDetailsFragment


class MerchantDetailsPagerAdapter(private val context: Context, fm: FragmentManager) :
  FragmentPagerAdapter(fm) {

  override fun getItem(position: Int): Fragment {
    return when(position) {
      0 -> PersonalInfoDetailsFragment.newInstance()
      1 -> ShopInfoDetailsFragment.newInstance()
      else -> AccountInfoDetailsFragment.newInstance()
    }
  }

  override fun getPageTitle(position: Int): CharSequence {
    return when(position) {
      0 -> context.getString(R.string.personal_info)
      1 -> context.getString(R.string.shop_info)
      else -> context.getString(R.string.account_info)
    }
  }

  override fun getCount(): Int {
    return 3
  }
}
