package com.property.propertyagent.owner_panel.ui.main.home.property.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyagent.modal.owner.owner_building_details.OwnerBuildingDetailsListDocument
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.images.OwnerPropertyImageFragmentBuildingNew

class OwnerPropertyImageSliderAdapter(
    fragmentManager : FragmentManager ,
    lifecycle : Lifecycle ,
    private var documents : ArrayList<OwnerBuildingDetailsListDocument>
):
    FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return documents.size
    }

    override fun createFragment(position: Int): Fragment {
        /*return if(documents[position].type==0){
            OwnerPropertyImageFragment.newInstance(documents[position].document)
        } else{
            OwnerPropertyVideoFragment.newInstance(documents[position].document_image)
        }*/
        return OwnerPropertyImageFragmentBuildingNew.newInstance(documents[position].document, documents)
    }
}
