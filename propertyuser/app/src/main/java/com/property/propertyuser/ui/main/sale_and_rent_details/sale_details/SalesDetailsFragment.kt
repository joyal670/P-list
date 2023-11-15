package com.property.propertyuser.ui.main.sale_and_rent_details.sale_details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentRentDetailsBinding
import com.property.propertyuser.databinding.FragmentSalesDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.sale_and_rent_details.SaleAndRentActivity
import com.property.propertyuser.ui.main.sale_and_rent_details.rent_details.RentDetailsFragment

class SalesDetailsFragment:BaseFragment() {
    private lateinit var binding: FragmentSalesDetailsBinding
    private lateinit var activityListener: ActivityListener
    private var propertyIdPassed=""
    companion object {
        const val ARG_PROPERTY_ID = "property_id"

        fun newInstance(property_id: String): SalesDetailsFragment {
            val fragment = SalesDetailsFragment()
            val bundle = Bundle().apply {
                putString(ARG_PROPERTY_ID, property_id)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        propertyIdPassed = arguments?.getString(ARG_PROPERTY_ID).toString()
        binding= FragmentSalesDetailsBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_sales_details, container, false)*/
    }

    override fun initData() {
        Log.e("propertyId",propertyIdPassed)
    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.sales_title))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener=activity as SaleAndRentActivity

    }
    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }
}