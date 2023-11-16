package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ncomfortsagent.model.building_details.AgentBuildingDetailsDocument
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.fragment.BuildingImageFragment

class BuildingImageSliderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val imageList: ArrayList<AgentBuildingDetailsDocument>):
    FragmentStateAdapter(fragmentManager,lifecycle)
{

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun createFragment(position: Int): Fragment {
        return BuildingImageFragment.newInstance(imageList[position].document, imageList)

        /*return if(position%2==0){
            PropertyImageFragment.newInstance(imageList[position])
        } else{
            PropertyVideoFragment.newInstance(imageList[position])
        }*/
    }
}
