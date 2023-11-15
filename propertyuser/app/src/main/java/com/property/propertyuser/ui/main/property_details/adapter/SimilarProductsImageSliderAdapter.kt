package com.property.propertyuser.ui.main.property_details.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.property.propertyuser.ui.main.property_details.floor_plan.PropertyFloorPlanImageFragment
import com.property.propertyuser.ui.main.property_details.similar_products.SimilarProductsListFragment
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyImageFragment
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyVideoFragment

class SimilarProductsImageSliderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val imageList: List<Int>):
        FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun createFragment(position: Int): Fragment {
        return SimilarProductsListFragment()
    }
}