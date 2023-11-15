package com.property.propertyuser.ui.main.my_property.view_details.rental_view_details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentRentalViewDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.my_property.view_details.ViewDetailsActivity
import com.property.propertyuser.ui.main.my_property.view_details.view_payment_history.ViewPaymentHistoryFragment
import com.property.propertyuser.ui.main.payment.PaymentActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.fragment_rental_view_details.*
import kotlinx.android.synthetic.main.layout_no_network.*

class RentalViewDetailsFragment : BaseFragment() {
    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentRentalViewDetailsBinding
    private lateinit var rentalViewDetailsViewModel: RentalViewDetailsViewModel
    private var userPropertyId = ""
    private var propertyCode = ""
    private var propertyName = ""
    private var rentAmount = ""
    private var propertyImage = ""
    private var viewContractFile = ""

    companion object {
        const val ARG_USER_PROPERTY_ID = "user_property_id"
        fun newInstance(user_property_id: String): RentalViewDetailsFragment {
            val fragment = RentalViewDetailsFragment()

            val bundle = Bundle().apply {
                putString(ARG_USER_PROPERTY_ID, user_property_id)
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
        userPropertyId = arguments?.getString(ARG_USER_PROPERTY_ID).toString()
        binding = FragmentRentalViewDetailsBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_rental_view_details,container,false)*/
    }

    override fun initData() {
        if (userPropertyId != null) {
            rentalViewDetailsViewModel.fetchRentalUserPropertyViewDetails(userPropertyId)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as ViewDetailsActivity

    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        rentalViewDetailsViewModel = RentalViewDetailsViewModel()
    }

    override fun setupObserver() {
        rentalViewDetailsViewModel.getRentalUserPropertyViewDetailsResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showLoader()
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("responseproceeddetails", Gson().toJson(it))
                        if (it.data?.user_property != null) {
                            includeNoInternetRentalViewDetails.visibility = View.GONE
                            linearNoDataFoundRentalViewDetails.visibility = View.GONE
                            constraintMainRentalViewDetails.visibility = View.VISIBLE

                            it.data.user_property.document.let { t ->
                                roundedRentalViewDetailsPropertyImage.loadImagesWithGlideExt(it.data.user_property.document)
                            }

                            tvCheckInDate.text = it.data.user_property.check_in
                            tvCheckOutDate.text = it.data.user_property.check_out
                            if (it.data.user_property.user_property_related != null) {
                                tvRentalAmount.text =
                                    it.data.user_property.user_property_related.rent
                                tvPropertyCode.text =
                                    it.data.user_property.user_property_related.property_reg_no
                                tvNextDueDate.text = it.data.user_property.due_date
                                activityListener.setTitle(it.data.user_property.user_property_related.property_name)
                                propertyName =
                                    it.data.user_property.user_property_related.property_name
                                propertyCode =
                                    it.data.user_property.user_property_related.property_reg_no
                                rentAmount = it.data.user_property.user_property_related.rent
                                propertyImage = it.data.user_property.document
                                viewContractFile = it.data.user_property.contract_rel.contract_file
                                //tvNextDueDate.text = it.data.user_property.user_property_related.rent
                                binding.constraintMainRentalViewDetails.visibility = View.VISIBLE

                            }
                        } else {
                            includeNoInternetRentalViewDetails.visibility = View.GONE
                            linearNoDataFoundRentalViewDetails.visibility = View.VISIBLE
                            constraintMainRentalViewDetails.visibility = View.GONE
                        }


                    }
                    Status.ERROR -> {
                        dismissLoader()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        includeNoInternetRentalViewDetails.visibility = View.GONE
                        linearNoDataFoundRentalViewDetails.visibility = View.VISIBLE
                        constraintMainRentalViewDetails.visibility = View.GONE
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            includeNoInternetRentalViewDetails.visibility = View.VISIBLE
                            linearNoDataFoundRentalViewDetails.visibility = View.GONE
                            constraintMainRentalViewDetails.visibility = View.GONE
                        }
                    }

                }
            })
    }

    override fun onClicks() {
        btnPayNow.setOnClickListener {
            val intent = Intent(context, PaymentActivity::class.java)
            ("passed_type").toString()
            intent.putExtra("passed_type", "rental_view_details_pay")
            intent.putExtra("user_property_id_passed", userPropertyId)
            context?.startActivity(intent)
        }
        btnViewPaymentHistory.setOnClickListener {
            Log.e("code", propertyCode)
            Log.e("name", propertyName)
            Log.e("rent", rentAmount)
            Log.e("image", propertyImage)
            activityListener.navigateToFragment(
                ViewPaymentHistoryFragment.newInstance(
                    userPropertyId,
                    propertyName,
                    propertyCode,
                    rentAmount,
                    propertyImage
                )
            )
        }
        cvViewContract.setOnClickListener {
            Log.e("contract", viewContractFile)
            if (viewContractFile != "") {
                val builder = CustomTabsIntent.Builder()
                val colorInt: Int = Color.parseColor("#2F80ED")
                builder.setToolbarColor(colorInt)
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, Uri.parse(viewContractFile))
            }
        }
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternetRentalViewDetails.visibility = View.GONE
                rentalViewDetailsViewModel.fetchRentalUserPropertyViewDetails(userPropertyId)
            }
        }
    }
}