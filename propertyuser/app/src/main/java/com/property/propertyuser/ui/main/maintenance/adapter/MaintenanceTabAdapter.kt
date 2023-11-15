package com.property.propertyuser.ui.main.maintenance.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyuser.ui.main.maintenance.service.ServiceFragment
import com.property.propertyuser.ui.main.maintenance.status.StatusFragment

class MaintenanceTabAdapter(private var propertyId:String,
                            private var userPropertyId:String,fm: FragmentManager?, lifecycle: Lifecycle) : FragmentStateAdapter(fm!!, lifecycle) {

    private val items: Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ServiceFragment.newInstance(propertyId,userPropertyId)
            1 -> fragment = StatusFragment.newInstance(userPropertyId)
        }
        return fragment!!
    }
    override fun getItemCount(): Int {
        return items
    }
}