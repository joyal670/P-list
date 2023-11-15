package com.property.propertyagent.agent_panel.ui.main.home.myclient.users.details

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertybook.PayFullPaymentActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.is_completed
import kotlinx.android.synthetic.main.fragment_ongoing_details.*

class UserPropertyViewDetailedFragment : BaseFragment() {
    private var passedTourId: String = ""
    private var phoneNo: String = ""
    private var currentUserId: String = ""
    private var propertyId: String = ""
    private var userPropertyId = ""

    private var isPaymentDone: Boolean = false
    private var isLoading: Boolean = false

    companion object {
        fun newInstance(id: String) =
            UserPropertyViewDetailedFragment()
                .apply {
                    passedTourId = id
                }
    }

    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var userPropertyViewDetailedViewModel: UserPropertyViewDetailedViewModel


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_ongoing_details, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as UsersPropertyViewDetailedActivity
        if (is_completed) {
            is_completed = false
            fragmentTransInterface.setTitleFromFragment(getString(R.string.CompletedDeal))
            layoutBookingDetails.isVisible = false
            layoutContractDetails.isVisible = true
        } else {
            fragmentTransInterface.setTitleFromFragment(getString(R.string.OngoingDeal))
        }

        fragment_user_ongoing_featuresTv.paintFlags =
            fragment_user_ongoing_featuresTv.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        fragment_user_ongoing_ratingTv.paintFlags =
            fragment_user_ongoing_ratingTv.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        fragment_user_ongoing_sarTv.paintFlags =
            fragment_user_ongoing_sarTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        userPropertyViewDetailedViewModel.agentUserPropertyViewDetails(passedTourId)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        userPropertyViewDetailedViewModel = UserPropertyViewDetailedViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        userPropertyViewDetailedViewModel.getAgentUserPropertyViewDetailsResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgress()
                        }
                    }
                    Status.SUCCESS -> {
                        dismissProgress()
                        isLoading = false
                        if (it.data?.data != null) {
                            currentUserId = it.data.data.user_id.toString()

                            if (!it.data.data.property_priority_image.equals(null)) {
                                ivPropertyImageInDetails.loadImagesWithGlideExt(it.data.data.property_priority_image.document)
                            }
                            if (!it.data.data.user_booking_data.equals(null)) {
                                tvCheckIn.text = it.data.data.user_booking_data.check_in
                                tvCheckOut.text = it.data.data.user_booking_data.check_out

                                tvUserContractStart.text = it.data.data.user_booking_data.check_in
                                tvUserContractEnd.text = it.data.data.user_booking_data.check_out
                            }
                            if (!it.data.data.property_data.equals(null)) {
                                try {
                                    tvPropertyNameOngoingDetails.text =
                                        it.data.data.property_data.property_name
                                    tvPropertyCodeOngoingDetails.text =
                                        it.data.data.property_data.property_reg_no
                                    tvPropertyAmountOngoingDetails.text =
                                        getString(R.string.sar) + " " + it.data.data.property_data.selling_price
                                    fragment_user_ongoing_sarTv.text =
                                        getString(R.string.sar) + " " + it.data.data.property_data.mrp
                                    tvRating.text = it.data.data.property_data.rating
                                    val count: Double =
                                        it.data.data.property_data.rating.toDouble()
                                    ratingbar.count = count.toInt()
                                    fragment_user_ongoing_ratingTv.text =
                                        it.data.data.property_data.rating_count.toString() + " " + getString(R.string.ratings)
                                    if (it.data.data.property_data.property_to == 0) {
                                        tvPropertyToOngoingDetails.text =
                                            getString(R.string.appartment_for_rent)
                                        tvPropertyAmountOngoingDetails.text =
                                            getString(R.string.sar) + " " + it.data.data.property_data.rent
                                        fragment_user_ongoing_sarTv.isVisible = false
                                    } else {
                                        tvPropertyToOngoingDetails.text =
                                            getString(R.string.appartment_for_sale)
                                        layoutCheckOut.isVisible = false
                                    }
                                    if (it.data.data.agent_tour.payment_check.toString() == "1") {
                                        isPaymentDone = true
                                    }
                                    if (!it.data.data.property_data.city_rel.equals(null)) {
                                        tvLocationOngoingDetails.text =
                                            it.data.data.property_data.city_rel.name
                                    }

                                    propertyId = it.data.data.property_data.id.toString()

                                    tvBedOngoingDetails.text =
                                        it.data.data.property_data.Beds.toString()
                                    tvBathOngoingDetails.text =
                                        it.data.data.property_data.Bathroom.toString()
                                    tvAreaOngoingDetails.text =
                                        it.data.data.property_data.area.toString()
                                    when (it.data.data.property_data.furnished) {
                                        0 -> {
                                            tvFurnishedOngoingDetails.text =
                                                getString(R.string.not_furnished)
                                        }
                                        1 -> {
                                            tvFurnishedOngoingDetails.text =
                                                getString(R.string.semifurnished)
                                        }
                                        2 -> {
                                            tvFurnishedOngoingDetails.text =
                                                getString(R.string.fully)
                                        }else -> {
                                        tvFurnishedOngoingDetails.text =
                                            getString(R.string.not_furnished)
                                        }
                                    }
                                    tvFloorOngoingDetails.text =
                                        it.data.data.property_data.floors.toString()
                                    if (!it.data.data.user_rel.equals(null)) {
                                        ivProfileImageOngoingDetails.loadImagesWithGlideExt(it.data.data.user_rel.profile_pic)
                                        tvProfileNameOngoingDetails.text =
                                            it.data.data.user_rel.name
                                        tvProfileEmailOngoingDetails.text =
                                            it.data.data.user_rel.email
                                        phoneNo = it.data.data.user_rel.phone
                                    }
                                    if (!it.data.data.agent_tour.equals(null)) {
                                        setupVisitedStatus(it.data.data.agent_tour.status)
                                        userPropertyId = it.data.data.agent_tour.user_property_id
                                    }
                                } catch (e: NullPointerException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            }

        userPropertyViewDetailedViewModel.getAgentOwnerContractResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()

                        if(it.data!!.data != null ){
                            if (!it.data.data.contract_file.isNullOrEmpty()) {
                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(it.data.data.contract_file))
                                startActivity(browserIntent)
                            }else{
                                Toaster.showToast(
                                    requireContext(),
                                    getString(R.string.internal_server_error),
                                    Toaster.State.ERROR,
                                    Toast.LENGTH_LONG
                                )
                            }
                        }else {
                            Toaster.showToast(
                                requireContext(),
                                getString(R.string.internal_server_error),
                                Toaster.State.ERROR,
                                Toast.LENGTH_LONG
                            )
                        }

                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            }

        userPropertyViewDetailedViewModel.getAgentUserContractResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        if(it.data!!.data != null){
                            if (!it.data.data.contract_file.isNullOrEmpty()) {
                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(it.data.data.contract_file))
                                startActivity(browserIntent)
                            }else {
                                Toaster.showToast(
                                    requireContext(),
                                    getString(R.string.internal_server_error),
                                    Toaster.State.ERROR,
                                    Toast.LENGTH_LONG
                                )
                            }
                        }else {
                            Toaster.showToast(
                                requireContext(),
                                getString(R.string.internal_server_error),
                                Toaster.State.ERROR,
                                Toast.LENGTH_LONG
                            )
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            }

        userPropertyViewDetailedViewModel.getAgentRequestContractDetailsResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        showProgress()
                        isLoading = true
                    }
                    Status.SUCCESS -> {
                        if (!it.data!!.response.equals(null)) {
                            Toaster.showToast(
                                requireContext(),
                                it.data.response,
                                Toaster.State.SUCCESS,
                                Toast.LENGTH_LONG
                            )
                            userPropertyViewDetailedViewModel.agentUserPropertyViewDetails(
                                passedTourId
                            )
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.internal_server_error),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            it.data!!.response,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            }
    }

    private fun setupVisitedStatus(status: Int) {
        when (status) {
            0 -> {
                ivAgentStatusCodeOngoingDetails1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails3.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails4.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails5.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            1 -> {

                ivAgentStatusCodeOngoingDetails1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCodeOngoingDetails2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails3.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails4.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails5.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            2 -> {
                ivAgentStatusCodeOngoingDetails1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCodeOngoingDetails2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusBooked.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusBooked.text = getString(R.string.property_booked)

                ivAgentStatusCodeOngoingDetails3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails3.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails4.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails5.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            3 -> {
                ivAgentStatusCodeOngoingDetails1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCodeOngoingDetails2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusBooked.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusBooked.text = getString(R.string.property_booked)

                ivAgentStatusCodeOngoingDetails3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails3.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusPayToken.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusPayToken.text = getString(R.string.reservation_payed)

                tvStatusRequested.isVisible = false
                btnRequestContract.isVisible = true

                ivAgentStatusCodeOngoingDetails4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails4.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

                ivAgentStatusCodeOngoingDetails5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails5.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            4 -> {
                ivAgentStatusCodeOngoingDetails1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCodeOngoingDetails2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusBooked.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusBooked.text = getString(R.string.property_booked)

                ivAgentStatusCodeOngoingDetails3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails3.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusPayToken.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusPayToken.text = getString(R.string.reservation_payed)

                tvStatusRequested.isVisible = true
                btnRequestContract.isVisible = false

                ivAgentStatusCodeOngoingDetails4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails4.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                if (isPaymentDone) {
                    tvStatusPayment.text = getString(R.string.waiting_for_approval)
                    tvStatusPayment.isVisible = true
                    btnPayFullAmount.isVisible = false
                } else {
                    btnPayFullAmount.isVisible = true
                    tvStatusPayment.isVisible = false
                }

                tvStatusRequested.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusRequested.text = getString(R.string.success_request)

                ivAgentStatusCodeOngoingDetails5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisitedOngoingDetails5.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            5 -> {
                ivAgentStatusCodeOngoingDetails1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCodeOngoingDetails2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusBooked.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusBooked.text = getString(R.string.property_booked)

                ivAgentStatusCodeOngoingDetails3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails3.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusPayToken.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusPayToken.text = getString(R.string.reservation_payed)

                ivAgentStatusCodeOngoingDetails4.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails4.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusRequested.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusRequested.text = getString(R.string.success_request)

                ivAgentStatusCodeOngoingDetails5.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisitedOngoingDetails5.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )

                tvStatusPayment.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatusPayment.text = getString(R.string.tvStatus_payment_done)
            }
        }
    }

    override fun onClicks() {
        fragment_user_ongoing_CallTv.setOnClickListener {
            if (phoneNo.isNotBlank()) {
                CommonUtils.dailPhone(requireContext(), phoneNo)
            }
        }

        fragment_user_ongoing_whatsappTv.setOnClickListener {
            if (phoneNo.isNotBlank()) {
                CommonUtils.openWhatsAppEnquiry(requireContext(), phoneNo, getString(R.string.hai))
            }
        }

        btnRequestContract.setOnClickListener {
            userPropertyViewDetailedViewModel.agentRequestContract(userPropertyId, passedTourId)
        }

        btnOwnerContract.setOnClickListener {
            userPropertyViewDetailedViewModel.fetchAgentOwnerContract(propertyId)
        }

        btnUserContract.setOnClickListener {
            userPropertyViewDetailedViewModel.fetchAgentUserContract(propertyId, currentUserId)
        }

        btnPayFullAmount.setOnClickListener {
            val intent = Intent(context, PayFullPaymentActivity::class.java)
            intent.putExtra("USER_PROPERTY_ID", userPropertyId)
            intent.putExtra("PASSED_TOUR_ID", passedTourId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //if (isConnectionRestored) {
        userPropertyViewDetailedViewModel.agentUserPropertyViewDetails(passedTourId)
        // }
    }
}