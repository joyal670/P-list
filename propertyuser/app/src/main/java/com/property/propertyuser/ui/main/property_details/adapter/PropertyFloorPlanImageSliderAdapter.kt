package com.property.propertyuser.ui.main.property_details.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyuser.modal.property_details.FloorPlan
import com.property.propertyuser.ui.main.property_details.floor_plan.PropertyFloorPlanImageFragment

class PropertyFloorPlanImageSliderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val imageList: List<FloorPlan>):
        FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun createFragment(position: Int): Fragment {
        return PropertyFloorPlanImageFragment.newInstance(imageList[position].document)
    }
}