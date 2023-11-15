package com.property.propertyuser.ui.main.property_details.packages.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentPropertyFloorImageBinding
import com.property.propertyuser.databinding.FragmentWeeklyBinding
import com.property.propertyuser.ui.main.property_details.packages.adapter.PackageDescriptionAdapter
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import kotlinx.android.synthetic.main.fragment_weekly.*

class WeeklyFragment(private  val function: (String) -> Unit) :BaseFragment() {
    private lateinit var binding: FragmentWeeklyBinding
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWeeklyBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_weekly, container, false)*/
    }

    override fun initData() {
        setPackageDescriptionRecyclerView()
    }
    private fun setPackageDescriptionRecyclerView() {
        rvPackageDescriptionList.layoutManager = LinearLayoutManager(context)
        rvPackageDescriptionList.adapter = context?.let {
            PackageDescriptionAdapter(
                it
            )
        }

    }
    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        btnSelect.setOnClickListener {
            function.invoke("1")
        }
    }
}