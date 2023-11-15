package com.property.propertyuser.ui.main.property_details.similar_products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentSimilarProductsItemBinding
import com.property.propertyuser.databinding.FragmentWeeklyBinding
import com.property.propertyuser.pojo.BathsModelData
import com.property.propertyuser.ui.main.property_details.adapter.PropertyImageSliderAdapter
import kotlinx.android.synthetic.main.activity_property_details.*
import java.util.*
import kotlin.collections.ArrayList

class SimilarProductsListFragment:BaseFragment() {
    private lateinit var binding: FragmentSimilarProductsItemBinding
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSimilarProductsItemBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_similar_products_item, container, false)*/
    }

    override fun initData() {

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