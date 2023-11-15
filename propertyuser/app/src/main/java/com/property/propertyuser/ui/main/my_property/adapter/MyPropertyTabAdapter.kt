package com.property.propertyuser.ui.main.my_property.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyuser.ui.main.property_details.packages.fragments.WeeklyFragment
import com.property.propertyuser.ui.main.my_property.booked.BookedFragment
import com.property.propertyuser.ui.main.my_property.owned.OwnedFragment
import com.property.propertyuser.ui.main.my_property.rental.RentalFragment
import com.property.propertyuser.ui.main.my_property.scheduled.ScheduledFragment

class MyPropertyTabAdapter(fm: FragmentManager?, lifecycle: Lifecycle) : FragmentStateAdapter(fm!!, lifecycle) {

    private val items: Int = 4

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = RentalFragment()
            1 -> fragment = OwnedFragment()
            2 -> fragment = BookedFragment()
            3 -> fragment = ScheduledFragment()
        }
        return fragment!!
    }
    override fun getItemCount(): Int {
        return items
    }
}