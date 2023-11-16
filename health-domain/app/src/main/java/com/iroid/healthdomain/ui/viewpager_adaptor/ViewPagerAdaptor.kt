package com.iroid.healthdomain.ui.viewpager_adaptor

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.hds.HdsFragment
import com.iroid.healthdomain.ui.home.my_health.history_and_activity.history.steps.StepsFragment

class ViewPagerAdaptor(
        list: ArrayList<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
        FragmentStateAdapter(fragmentManager, lifecycle) {


    private val fragmentList = list

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getViewAtPosition(position: Int): View? {
        return fragmentList[position].view
    }
}
