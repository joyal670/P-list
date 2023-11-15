package com.property.propertyuser.ui.main.property_details.floor_plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentPropertyFloorImageBinding
import com.property.propertyuser.databinding.FragmentRentalViewDetailsBinding
import com.property.propertyuser.pojo.BathsModelData
import com.property.propertyuser.ui.main.property_details.adapter.PropertyImageSliderAdapter
import com.property.propertyuser.utils.CommonMethods
import com.property.propertyuser.utils.Constants.ARG_IMAGE
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.fragment_property_floor_image.*
import java.util.*
import kotlin.collections.ArrayList

class PropertyFloorPlanImageFragment:BaseFragment() {
    private lateinit var binding: FragmentPropertyFloorImageBinding
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPropertyFloorImageBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_property_floor_image, container, false)*/
    }
    companion object{
        fun newInstance(image:String)=PropertyFloorPlanImageFragment().apply {
            arguments=Bundle().apply {
                putString(ARG_IMAGE,image)

            }
        }
    }

    override fun initData() {
        CommonMethods.setImage(requireContext(),arguments?.get(ARG_IMAGE).toString(),binding.ivImage)
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