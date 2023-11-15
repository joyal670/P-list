package com.property.propertyuser.ui.main.property_details.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyuser.modal.property_details.Document
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyImageFragment
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyVideoFragment
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyYouTubeVideoFragment

class PropertyImageSliderAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle, private val imageList: List<Document>
):
        FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun createFragment(position: Int): Fragment {
        val imageArrayList = ArrayList<Document>()
        imageList.forEach {
            imageArrayList.add(it)
        }
        return if(imageList[position].type==0){
            PropertyImageFragment.newInstance(imageList[position].document, imageArrayList)
        } else if (imageList[position].type == 1){
            PropertyVideoFragment.newInstance(imageList[position].document)
        } else {
            PropertyYouTubeVideoFragment.newInstance(imageList[position].document)
        }
    }
}