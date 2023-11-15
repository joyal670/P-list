package com.property.propertyuser.ui.main.booking.book_a_tour

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentBookATourBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.booking.BookingActivity
import com.property.propertyuser.ui.main.home.dashboard.DashboardActivity
import com.property.propertyuser.ui.main.map_and_nearby.MapAndNearByActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import com.property.propertyuser.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.fragment_book_a_tour.*
import kotlinx.android.synthetic.main.layout_no_network.*

class BookATourFragment : BaseFragment() {
    private var propertyIdPassed = ""
    private var tourIdPassed = ""

    companion object {
        const val ARG_PROPERTY_ID = "property_id"
        const val ARG_TOUR_ID = "tour_id"


        fun newInstance(property_id: String, tour_id: String): BookATourFragment {
            val fragment = BookATourFragment()

            val bundle = Bundle().apply {
                putString(ARG_PROPERTY_ID, property_id)
                putString(ARG_TOUR_ID, tour_id)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentBookATourBinding
    private lateinit var bookingViewModel: BookingViewModel
    private var tour_id = ""
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookATourBinding.inflate(inflater!!, container, false)
        val view = binding.root
        propertyIdPassed = arguments?.getString(ARG_PROPERTY_ID).toString()
        tourIdPassed = arguments?.getString(ARG_TOUR_ID).toString()
        return view
        /*return inflater?.inflate(R.layout.fragment_book_a_tour,container,false)*/
    }

    override fun initData() {
        bookingViewModel.fetchBookingTourDetails(tourIdPassed, propertyIdPassed)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as BookingActivity

    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.book_a_tour_title))
    }

    override fun setupViewModel() {
        bookingViewModel = BookingViewModel()
    }

    override fun setupObserver() {
        bookingViewModel.getBookingTourResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    tour_id = ""
                    if (it.data?.data != null) {
                        includeNoInternetBookTour.visibility = View.GONE
                        linearNoDataFoundBookTour.visibility = View.GONE
                        mainConstraintBookTour.visibility = View.VISIBLE
                        tour_id = it.data.data.tour_details.id.toString()
                        if (it.data.data.property_details.property_priority_image != null) {
                            roundedPropertyImage.loadImagesWithGlideExt(it.data.data.property_details.property_priority_image.document)
                        }
                        tvPropertyName.text = it.data.data.property_details.property_name
                        tvBookedTime.text = it.data.data.tour_details.time_range
                        tvBookedDate.text = it.data.data.tour_details.booked_date
                        tvPropertyCode.text = it.data.data.property_details.property_reg_no
                        if (it.data.data.property_details.property_to == 0) {
                            tvPropertyAmount.text =
                                getString(R.string.sar) + " " + it.data.data.property_details.rent
                        } else {
                            tvPropertyAmount.text =
                                getString(R.string.sar) + " " + it.data.data.property_details.selling_price
                        }
                        tvPropertyLocation.text = it.data.data.property_details.location
                        if (it.data.data.property_details.latitude.isNotBlank() &&
                            it.data.data.property_details.longitude.isNotBlank()
                        ) {
                            tvLocationName.text = it.data.data.property_details.location
                        } else {
                            tvLocationName.visibility = View.GONE
                            btnOpenMap.visibility = View.GONE
                        }
                    } else {
                        linearNoDataFoundBookTour.visibility = View.VISIBLE
                        mainConstraintBookTour.visibility = View.GONE
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.status.toString(),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternetBookTour.visibility = View.VISIBLE
                        linearNoDataFoundBookTour.visibility = View.GONE
                        mainConstraintBookTour.visibility = View.GONE
                    }
                }

            }
        })
        bookingViewModel.getBookingTourConfirmationResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showLoader()
                    Status.SUCCESS -> {
                        dismissLoader()
                        Toaster.showToast(
                            requireContext(), it.data?.response!!,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        requireContext().startActivity(
                            Intent(
                                requireContext(),
                                DashboardActivity::class.java
                            )
                        )
                        activity?.finishAffinity()
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        Toaster.showToast(
                            requireContext(),
                            it.data!!.response.toString(),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            Toaster.showToast(
                                requireContext(), getString(R.string.no_internet),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        }
                    }
                    Status.ERROR -> {
                        dismissLoader()
                        Toaster.showToast(
                            requireContext(), it.data?.response.toString(),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }

                }
            })
    }

    override fun onClicks() {
        btnBookATourFinal.setOnClickListener {
            bookingViewModel.bookingTourConfirmation(tour_id)
            /*startActivity(Intent(context,PaymentActivity::class.java))*/
        }
        btnChangeDateTime.setOnClickListener {
            activity?.onBackPressed()
        }
        btnOpenMap.setOnClickListener {
            /*val mapIntent = Intent(Intent.ACTION_VIEW*//*, gmmIntentUri*//*)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)*/
            val intent = Intent(requireActivity(), MapAndNearByActivity::class.java)
            intent.putExtra("property_id", propertyIdPassed)
            intent.putExtra("from_type", "home_property_list")
            startActivity(intent)
        }
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternetBookTour.visibility = View.GONE
                bookingViewModel.fetchBookingTourDetails(tourIdPassed, propertyIdPassed)
            }
        }
    }
}