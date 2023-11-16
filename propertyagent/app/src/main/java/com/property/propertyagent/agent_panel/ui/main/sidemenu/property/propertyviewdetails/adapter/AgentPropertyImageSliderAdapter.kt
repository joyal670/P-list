package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyagent.modal.agent.agent_assigned_property_details.Document
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.images.OwnerPropertyImageFragment
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.images.OwnerPropertyVideoFragment

class AgentPropertyImageSliderAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var documents: List<Document>,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return documents.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (documents[position].type == 0) {
            OwnerPropertyImageFragment.newInstance(documents[position].document_image)
        } else {
            OwnerPropertyVideoFragment.newInstance(documents[position].document_image)
        }
    }
}