package com.property.propertyagent.start_up.welcome_slider

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroSliderAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val fragmentList = ArrayList<Fragment>()
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFragmentList(list: List<Fragment>) {
        fragmentList.clear()
        fragmentList.addAll(list)
        notifyDataSetChanged()
    }
}