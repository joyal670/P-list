package com.iroid.emergency.main.home.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val homeFragmentList: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return homeFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return homeFragmentList[position]
    }
    fun getViewAtPosition(position: Int): View? {
        return homeFragmentList[position].view
    }
}
