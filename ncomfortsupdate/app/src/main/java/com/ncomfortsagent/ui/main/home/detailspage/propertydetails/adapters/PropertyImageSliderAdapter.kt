package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsDocument
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.fragment.PropertyImageFragment

class PropertyImageSliderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val imageList: ArrayList<AgentPropertyDetailsDocument>):
    FragmentStateAdapter(fragmentManager,lifecycle)
{

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun createFragment(position: Int): Fragment {
        return PropertyImageFragment.newInstance(imageList[position].document, imageList)

        /*return if(position%2==0){
            PropertyImageFragment.newInstance(imageList[position])
        } else{
            PropertyVideoFragment.newInstance(imageList[position])
        }*/
    }
}
