package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.details

import android.Manifest
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
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.PropertyDetailsPage1Activity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.AppPreferences.is_builder
import com.property.propertyagent.utils.AppPreferences.is_completed
import kotlinx.android.synthetic.main.fragment_owner_ongoing_details.*

class OwnerPropertyViewDetailedFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var ownerPropertyViewDetailedViewModel: OwnerPropertyViewDetailedViewModel
    private var passedTourId: String = ""
    private var passedType: String = ""
    private var phoneNumber: String = ""
    private var propertyId: String = ""

    companion object {
        fun newInstance(tour_id: String, type: String) =
            OwnerPropertyViewDetailedFragment()
                .apply {
                    passedTourId = tour_id
                    passedType = type
                }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_owner_ongoing_details, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as OwnerPropertyViewDetailedActivity
        if (passedType == "tour") {
            fragmentTransInterface.setTitleFromFragment(getString(R.string.OngoingDeal))
            ownerPropertyViewDetailedViewModel.fetchAgentStartOwnerTourPropertyDetails(passedTourId)
        } else {
            if (is_completed) {
                is_completed = false
                fragmentTransInterface.setTitleFromFragment(getString(R.string.CompletedDeal))
                layoutOwnerContract.isVisible = true
                fragment_owner_ongoing_EditDetailsBtn.isVisible = false
            } else {
                fragmentTransInterface.setTitleFromFragment(getString(R.string.OngoingDeal))
            }
            ownerPropertyViewDetailedViewModel.fetchAgentStartOwnerTourPropertyDetails2(passedTourId)
        }

        fragment_owner_ongoing_featuresTv.paintFlags =
            fragment_owner_ongoing_featuresTv.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvPropertyAmount2.paintFlags = tvPropertyAmount2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        ownerPropertyViewDetailedViewModel = OwnerPropertyViewDetailedViewModel()
    }


    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        ownerPropertyViewDetailedViewModel.getAgentStartOwnerTourPropertyDetailsResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data != null) {
                            Log.e("response", Gson().toJson(it))
                            if (!it.data.response.equals(null)) {
                                if (!it.data.response.property_details.equals(null)) {
                                    clicked_property_id =
                                        it.data.response.property_details.id.toString()
                                    tvPropertyCodeOwnerOngoingDetails.text =
                                        it.data.response.property_details.property_reg_no

                                    if (it.data.response.property_details.mrp == "") {
                                        tvPropertyAmount1.isVisible = false
                                        tvPropertyAmount2.isVisible = false
                                        tvPropertyName.setMargins(0, 0, 0, 0)
                                    }

                                    if (it.data.response.property_details.is_builder != "") {
                                        is_builder =
                                            it.data.response.property_details.is_builder == "1"
                                    }

                                    if (is_builder) {
                                        layoutFeatures.isVisible = false
                                        fragment_owner_ongoing_featuresTv.isVisible = false
                                        tvPropertyAmount1.isVisible = false
                                    }

                                    propertyId = it.data.response.property_details.id.toString()
                                    tvPropertyAmount1.text = getString(R.string.sar) + " " +
                                            it.data.response.property_details.selling_price
                                    tvPropertyAmount2.text = getString(R.string.sar) + " " +
                                            it.data.response.property_details.mrp
                                    if (it.data.response.property_details.property_to == 0) {
                                        tvPropertyToOwnerOngoingDetails.text =
                                            getString(R.string.appartment_for_rent)
                                        tvPropertyAmount2.isVisible = false
                                        tvPropertyAmount1.text = getString(R.string.sar) + " " +
                                                it.data.response.property_details.rent
                                    } else {
                                        tvPropertyToOwnerOngoingDetails.text =
                                            getString(R.string.appartment_for_sale)
                                    }
                                    try {
                                        tvPropertyLocationOwnerOngoingDetails.text =
                                            CommonUtils.getAddress(
                                                it.data.response.property_details.latitude,
                                                it.data.response.property_details.longitude,
                                                requireContext()
                                            )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                    if (!it.data.response.property_details.property_priority_image.equals(
                                            null
                                        )
                                    ) {
                                        if (!it.data.response.property_details.property_priority_image.document.equals(
                                                null
                                            )
                                        ) {
                                            ivPropertyImageOngoingDetails.loadImagesWithGlideExt(it.data.response.property_details.property_priority_image.document)
                                        }
                                    }

                                    tvPropertyName.text =
                                        it.data.response.property_details.property_name
                                    tvContractStart.text =
                                        it.data.response.property_details.contract_start_date
                                    tvContractEnd.text =
                                        it.data.response.property_details.contract_end_date

                                    tvBedOwnerOngoingDetails.text =
                                        it.data.response.property_details.Beds.toString()
                                    tvBathOwnerOngoingDetails.text =
                                        it.data.response.property_details.Bathroom.toString()
                                    tvAreaOwnerOngoingDetails.text =
                                        it.data.response.property_details.area.toString() + " " + getString(
                                            R.string.sq_m
                                        )

                                    tvFloorOwnerOngoingDetails.text =
                                        it.data.response.property_details.floors.toString()
                                }
                                when (it.data.response.property_details.furnished) {
                                    "0" -> {
                                        tvFurnishedOwnerOngoingDetails.text =
                                            getString(R.string.not_furnished)
                                    }
                                    "1" -> {
                                        tvFurnishedOwnerOngoingDetails.text =
                                            getString(R.string.semifurnished)
                                    }
                                    "2" -> {
                                        tvFurnishedOwnerOngoingDetails.text =
                                            getString(R.string.fully)
                                    }
                                }
                                if (it.data.response.property_details.furnished == "") {
                                    layoutFurnished.isVisible = false
                                }
                                if (!it.data.response.owner_details.equals(null)) {
                                    ivProfileOwnerOngoingDetails.loadImagesWithGlideExt(it.data.response.owner_details.profile_image)
                                    tvOwnerNameOwnerOngoingDetails.text =
                                        it.data.response.owner_details.name
                                    tvOwnerEmailOwnerOngoingDetails.text =
                                        it.data.response.owner_details.email
                                    phoneNumber = it.data.response.owner_details.phone
                                }
                                if (!it.data.response.agent_tour.equals(null)) {
                                    setupStatus(it.data.response.agent_tour.status)
                                }
                            }
                        }
                        container.isVisible = true
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

        ownerPropertyViewDetailedViewModel.getAgentOwnerContractResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (!it.data!!.data.contract_file.equals(null)) {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(it.data.data.contract_file))
                            startActivity(browserIntent)
                        } else {
                            Toaster.showToast(
                                requireContext(),
                                getString(R.string.contact_not_found),
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

    }

    private fun setupStatus(status: Int) {
        when (status) {
            1 -> {
                ivGreenTick1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvStatusOne.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )
                ivGreenTick2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvStatusTwo.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                ivGreenTick3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvStatusThree.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
            }
            2 -> {

                fragment_owner_ongoing_EditDetailsBtn.isVisible = false
                ivGreenTick1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvStatusOne.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )
                ivGreenTick2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvStatusTwo.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )
                tvStatus2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatus2.text = getString(R.string.verified)
                ivGreenTick3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_ellipse_yellow
                    )
                )

            }
            3 -> {
                fragment_owner_ongoing_EditDetailsBtn.isVisible = false
                ivGreenTick1.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvStatusOne.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )
                ivGreenTick2.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvStatusTwo.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )
                tvStatus2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvStatus2.text = getString(R.string.verified)
                ivGreenTick3.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvStatusThree.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_green
                    )
                )
                tvFinalStatusTitle.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_accent_grey
                    )
                )
                tvFinalStatusTitle.text = getString(R.string.approved)
            }
        }
    }

    override fun onClicks() {

        btnGetOwnerContract.setOnClickListener {
            ownerPropertyViewDetailedViewModel.fetchAgentOwnerContract(propertyId)
        }

        fragment_owner_ongoing_CallBtn.setOnClickListener {
            if (phoneNumber.isNotEmpty()) {
                permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
                    if (result.allGranted()) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$phoneNumber")
                        startActivity(intent)
                    }
                }
            }
        }

        fragment_owner_ongoing_WhatsappBtn.setOnClickListener {

            if (phoneNumber.isNotEmpty()) {
                val message = "Hallo"
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            String.format(
                                "https://api.whatsapp.com/send?phone=%s&text=%s",
                                phoneNumber,
                                message
                            )
                        )
                    )
                )
            }
        }

        fragment_owner_ongoing_EditDetailsBtn.setOnClickListener {
            val intent = Intent(context, PropertyDetailsPage1Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            if (passedType == "tour") {
                ownerPropertyViewDetailedViewModel.fetchAgentStartOwnerTourPropertyDetails(
                    passedTourId
                )
            } else {
                ownerPropertyViewDetailedViewModel.fetchAgentStartOwnerTourPropertyDetails2(
                    passedTourId
                )
            }
        }
    }
}