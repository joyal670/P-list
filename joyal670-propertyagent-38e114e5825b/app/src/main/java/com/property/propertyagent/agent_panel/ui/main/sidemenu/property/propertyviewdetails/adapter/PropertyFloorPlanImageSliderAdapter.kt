package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.imageFragment.PropertyFloorPlanImageFragment
import com.property.propertyagent.modal.agent.agent_assigned_property_details.FloorPlan

class PropertyFloorPlanImageSliderAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var floorPlans: List<FloorPlan>,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return floorPlans.size
    }

    override fun createFragment(position: Int): Fragment {
        return PropertyFloorPlanImageFragment.newInstance(floorPlans[position].document_image)
    }
}