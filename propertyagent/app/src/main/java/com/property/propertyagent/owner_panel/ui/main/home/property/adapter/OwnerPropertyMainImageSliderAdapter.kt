package com.property.propertyagent.owner_panel.ui.main.home.property.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyagent.modal.owner.owner_property_main_details.new.OwnerPropertyMainDetailsNewDocument
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.images.OwnerPropertyImageFragment
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.images.OwnerPropertyImageFragmentNew
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.images.OwnerPropertyVideoFragment

class OwnerPropertyMainImageSliderAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var documents: ArrayList<OwnerPropertyMainDetailsNewDocument>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return documents.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (documents[position].type == 0) {
            OwnerPropertyImageFragmentNew.newInstance(documents[position].document, documents)
        } else {
            OwnerPropertyVideoFragment.newInstance(documents[position].document)
        }
    }
}
