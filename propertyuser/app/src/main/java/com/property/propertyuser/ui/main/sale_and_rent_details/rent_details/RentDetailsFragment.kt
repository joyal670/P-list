package com.property.propertyuser.ui.main.sale_and_rent_details.rent_details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentPropertyVideoBinding
import com.property.propertyuser.databinding.FragmentRentDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.booking.book_a_property.BookAPropertyFragment
import com.property.propertyuser.ui.main.my_property.view_details.rental_view_details.RentalViewDetailsViewModel
import com.property.propertyuser.ui.main.sale_and_rent_details.SaleAndRentActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_rent_details.*

class RentDetailsFragment:BaseFragment() {
    private lateinit var binding: FragmentRentDetailsBinding
    private lateinit var rentalDetailsViewModel: RentDetailsViewModel
    private lateinit var activityListener: ActivityListener
    private var propertyIdPassed=""
    companion object {
        const val ARG_PROPERTY_ID = "property_id"

        fun newInstance(property_id: String): RentDetailsFragment {
            val fragment = RentDetailsFragment()
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
        binding= FragmentRentDetailsBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_rent_details, container, false)*/
    }

    override fun initData() {
        rentalDetailsViewModel.fetchPropertyRentalDetails(propertyIdPassed)
    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.rent_title))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener=activity as SaleAndRentActivity

    }
    override fun setupViewModel() {
        rentalDetailsViewModel= RentDetailsViewModel()
    }

    override fun setupObserver() {
        rentalDetailsViewModel.getPropertyRentalDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    if(it.data?.rent_details!=null){
                        tvRentAmount.text=getString(R.string.sar)+" "+it.data?.rent_details.rent
                        tvSecurityDepositAmount.text=getString(R.string.sar)+" "+it.data?.rent_details.security_deposit
                        tvRentDuration.text = it.data.rent_details.property_rent_frequency.type
                    }
                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(requireContext().isConnected){
                        Toaster.showToast(requireContext(),getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(requireContext(),getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
    }

    override fun onClicks() {

    }
}