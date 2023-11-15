package com.property.propertyuser.ui.main.home.property_image_view.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyuser.modal.property_details.Document
import com.property.propertyuser.ui.main.home.property_image_view.fragment.ImageFragment
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyImageFragment
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyVideoFragment

class ImageSliderAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle, private val imageList: List<String>
):
        FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(imageList[position])
    }
}