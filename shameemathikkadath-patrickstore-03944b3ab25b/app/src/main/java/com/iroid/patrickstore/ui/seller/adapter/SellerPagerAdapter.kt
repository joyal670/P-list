package com.iroid.patrickstore.ui.seller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.iroid.patrickstore.model.seller.SingleSeller
import com.iroid.patrickstore.ui.seller.SellerListFragment

class SellerPagerAdapter(
    fragmentManager: FragmentManager,
    private val titleList: List<String>
):
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return SellerListFragment.newInstance(titleList[position])
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}