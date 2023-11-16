package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.imageFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.fragment_property_floor_image.*

class PropertyFloorPlanImageFragment : BaseFragment() {

    private var propertyFloorImageSlide: String = ""

    companion object {
        fun newInstance(imagePassed: String) =
            PropertyFloorPlanImageFragment()
                .apply {
                    propertyFloorImageSlide = imagePassed
                }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_property_floor_image, container, false)
    }

    override fun initData() {
        ivImage.loadImagesWithGlideExt(propertyFloorImageSlide)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }
}