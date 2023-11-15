package com.iroid.emergency.start_up.intro.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroSliderAdapter(
    fragmentActivity: FragmentActivity,
    private val introFragmentList: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return introFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return introFragmentList[position]
    }
}
