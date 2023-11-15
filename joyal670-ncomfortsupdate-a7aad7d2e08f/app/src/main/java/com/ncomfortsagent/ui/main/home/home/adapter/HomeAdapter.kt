package com.ncomfortsagent.ui.main.home.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ncomfortsagent.ui.main.home.home.fragment.EnquiryMainFragment
import com.ncomfortsagent.ui.main.home.home.fragment.MyPropertyFragment

class HomeAdapter(fm: FragmentManager?, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm!!, lifecycle) {

    private val items: Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MyPropertyFragment()
            1 -> fragment = EnquiryMainFragment()
        }
        return fragment!!
    }

    override fun getItemCount(): Int {
        return items
    }
}