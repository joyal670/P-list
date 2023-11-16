package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyappointment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.home.HomeActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertybook.PropertyBookActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.PropertyViewDetailsActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.viewpackages.PackageActivity
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.AppPreferences.is_sale
import kotlinx.android.synthetic.main.activity_appointmentproperty.*
import kotlinx.android.synthetic.main.toolbar_main.*

class PropertyAppointmentActivity : BaseActivity() {

    private lateinit var propertyAppointmentViewModel: PropertyAppointmentViewModel
    private var passedTourId: String = ""
    private var propertyId: String = ""
    private var userId: String = ""
    private var amount: String = ""
    private var tourId: String = ""

    private var profileName: String = ""
    private var profileNo: String = ""
    private var profileImage: String = ""

    private var isEdit: Boolean = false
    private var haveCommission: Boolean = false

    override val layoutId: Int
        get() = R.layout.activity_appointmentproperty
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.PropertyAppointment)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        passedTourId = intent.getStringExtra("tour_id")!!
        propertyAppointmentViewModel.agentPropertyAppointmentDetails(passedTourId)
        appoinmentfrg_prcing.paintFlags =
            appoinmentfrg_prcing.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        propertyAppointmentViewModel = PropertyAppointmentViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        propertyAppointmentViewModel.getAgentPropertyAppointmentDetailsResponse()
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data?.response != null) {
                            Log.e("response", Gson().toJson(it))
                            if (!it.data.response.property_details.equals(null)) {
                                if (!it.data.response.property_details.property_priority_image.equals(
                                        null
                                    )
                                ) {
                                    agentPropertyImage.loadImagesWithGlideExt(it.data.response.property_details.property_priority_image.document)
                                }
                                propertyId = it.data.response.property_details.id.toString()
                                userId = it.data.response.user_details.id.toString()
                                tourId = it.data.response.agent_tour.id.toString()

                                profileImage = it.data.response.user_details.profile_pic
                                profileName = it.data.response.user_details.name
                                profileNo = it.data.response.user_details.phone

                                tvAgentPropertyName.text =
                                    it.data.response.property_details.property_name
                                agentPropertyCode.text =
                                    it.data.response.property_details.property_reg_no
                                if (it.data.response.property_details.property_to == 0) {
                                    agentPropertyTo.text = getString(R.string.appartment_for_rent)
                                    agentRentAmount.text =
                                        getString(R.string.sar) + " " + it.data.response.property_details.rent
                                    is_sale = false
                                } else {
                                    agentPropertyTo.text = getString(R.string.appartment_for_sale)
                                    txtAmountHead.text = getString(R.string.amount)
                                    agentRentAmount.text =
                                        getString(R.string.sar) + " " + it.data.response.property_details.selling_price
                                    is_sale = true
                                    layoutSecurity.isVisible = false
                                }

                                agentSecurityDeposit.text =
                                    getString(R.string.sar) + " " + it.data.response.property_details.security_deposit
                                agentTokenAmount.text =
                                    getString(R.string.sar) + " " + it.data.response.property_details.token_amount
                                if (!it.data.response.user_details.equals(null)) {
                                    agentVisitorName.text = it.data.response.user_details.name
                                    agentVisitoryEmail.text = it.data.response.user_details.email
                                    agentVisitorImage.loadImagesWithGlideExt(it.data.response.user_details.profile_pic)
                                }
                                if (!it.data.response.agent_tour.equals(null)) {
                                    setupVisitedStatus(it.data.response.agent_tour.status)
                                }
                                if (it.data.response.agent_commission != null) {
                                    if (it.data.response.agent_commission.amount != null) {
                                        btnAddCommision.isVisible = false
                                        btnEditCommision.isVisible = true
                                        tvCommission.isVisible = true
                                        tvCommission.text = it.data.response.agent_commission.amount
                                    }
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.LOADING -> {
                        showProgress()
                    }
                }
            }
        propertyAppointmentViewModel.getAgentUpdateTourNotInterestedResponse()
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data?.response != null) {
                            Log.e("response", Gson().toJson(it))
                            Toaster.showToast(
                                this,
                                it.data.response,
                                Toaster.State.SUCCESS,
                                Toast.LENGTH_LONG
                            )

                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.LOADING -> {
                        showProgress()
                    }
                }
            }

        propertyAppointmentViewModel.getAgentAddCommissionResponse()
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data?.response != null) {
                            Log.e("response", Gson().toJson(it))
                            btnAddCommision.isVisible = false
                            btnEditCommision.isVisible = true
                            tvCommission.isVisible = true
                            tvCommission.text = amount
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.LOADING -> {
                        showProgress()
                    }
                }
            }

        propertyAppointmentViewModel.getAgentEditCommissionResponse()
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data?.response != null) {
                            Log.e("response", Gson().toJson(it))
                            btnAddCommision.isVisible = false
                            btnEditCommision.isVisible = true
                            tvCommission.isVisible = true
                            tvCommission.text = amount
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.LOADING -> {
                        showProgress()
                    }
                }
            }
    }

    private fun setupVisitedStatus(statusAgentTour: Int) {
        when (statusAgentTour) {
            0 -> {
                ivAgentStatusCode1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited1.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited2.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited3.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode4.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited4.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode5.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited5.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
            1 -> {
                layoutCommission.isVisible = false
                btnAddCommision.isVisible = false
                btnEditCommision.isVisible = false
                if (haveCommission) {
                    tvCommission.isVisible = true
                }

                ivAgentStatusCode1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited1.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited2.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited3.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode4.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited4.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode5.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited5.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
            2 -> {
                ivAgentStatusCode1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited1.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited2.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited3.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode4.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited4.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode5.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited5.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
            3 -> {
                ivAgentStatusCode1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited1.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited2.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited3.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode4.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited4.setTextColor(ContextCompat.getColor(this, R.color.black))

                ivAgentStatusCode5.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited5.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
            4 -> {
                ivAgentStatusCode1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited1.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited2.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited3.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode4.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited4.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode5.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_ellipse_yellow
                    )
                )
                tvAgentStatusVisited5.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
            5 -> {

                btnAddCommision.isVisible = false
                btnEditCommision.isVisible = false
                if (haveCommission) {
                    tvCommission.isVisible = true
                }

                ivAgentStatusCode1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited1.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited2.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited3.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode4.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited4.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )

                ivAgentStatusCode5.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_group_ellipse_green
                    )
                )
                tvAgentStatusVisited5.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_accent_green
                    )
                )
            }
        }
    }

    override fun onClicks() {
        appoinmentfrg_porcedtobookBtn.setOnClickListener {
            val intent = Intent(this, PropertyBookActivity::class.java)
            intent.putExtra("tour_id", passedTourId)
            startActivity(intent)
        }

        property_apponiment_viewProfileTv.setOnClickListener {

            CommonUtils.showUserProfile(
                this,
                getString(R.string.user),
                profileName,
                profileNo,
                profileImage
            )



            /*val bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
            val bottomSheet: View = this.layoutInflater.inflate(R.layout.profile_layout_bottom_sheet, null)
            val closeBtn = bottomSheet.findViewById<ImageButton>(R.id.ivClose)
            val imageView = bottomSheet.findViewById<CircleImageView>(R.id.civProfilePicRequestDialog)
            val name = bottomSheet.findViewById<TextView>(R.id.tvProfileNameRequestDialog)
            val userType = bottomSheet.findViewById<TextView>(R.id.tvUserTypeRequestDialog)
            val callBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivPhoneRequestDialog)
            val msgBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivWhatsappRequestDialog)

            closeBtn.setOnClickListener {
                bottom.dismiss()
            }
            imageView.loadImagesWithGlideExt(profileImage)
            name.text = profileName
            userType.text = getString(R.string.user)
            if (profileNo.isNotBlank()) {
                callBtn.setOnClickListener {
                    CommonUtils.dailPhone(this, profileNo)
                }
            }
            if (profileNo.isNotBlank()) {
                msgBtn.setOnClickListener {
                    CommonUtils.openWhatsAppEnquiry(this, profileNo, getString(R.string.hai))
                }
            }

            bottom.setContentView(bottomSheet)
            bottom.show()*/


          /*  val dialog = Dialog(this)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.profile_layout)
            val civProfilePicRequestDialog =
                dialog.findViewById(R.id.civProfilePicRequestDialog) as CircleImageView
            val tvProfileNameRequestDialog =
                dialog.findViewById(R.id.tvProfileNameRequestDialog) as TextView
            val tvUserTypeRequestDialog =
                dialog.findViewById(R.id.tvUserTypeRequestDialog) as TextView
            val ivPhoneRequestDialog =
                dialog.findViewById(R.id.ivPhoneRequestDialog) as ImageView
            val ivWhatsappRequestDialog =
                dialog.findViewById(R.id.ivWhatsappRequestDialog) as ImageView

            civProfilePicRequestDialog.loadImagesWithGlideExt(profileImage)
            tvProfileNameRequestDialog.text = profileName
            tvUserTypeRequestDialog.text = getString(R.string.user)
            if (profileNo.isNotBlank()) {
                ivPhoneRequestDialog.setOnClickListener {
                    CommonUtils.dailPhone(this, profileNo)
                }
            }
            if (profileNo.isNotBlank()) {
                ivWhatsappRequestDialog.setOnClickListener {
                    CommonUtils.openWhatsAppEnquiry(this, profileNo, getString(R.string.hai))
                }
            }

            dialog.show()
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams*/
        }

        property_apponiment_viewPropertyDetails.setOnClickListener {
            if (propertyId.isNotBlank()) {
                val intent = Intent(this, PropertyViewDetailsActivity::class.java)
                intent.putExtra("property_id", propertyId)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.empty_property_id), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        property_apponiment_viewPackages.setOnClickListener {
            if (propertyId.isNotBlank()) {
                val intent = Intent(this, PackageActivity::class.java)
                intent.putExtra("property_id", propertyId)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.empty_property_id), Toast.LENGTH_SHORT)
                    .show()
            }

        }
        btnNotInterested.setOnClickListener {
            propertyAppointmentViewModel.agentUpdateTourNotInterested(passedTourId)
        }

        btnAddCommision.setOnClickListener {
            isEdit = false
            openCommissionDialog()
        }

        btnEditCommision.setOnClickListener {
            isEdit = true
            openCommissionDialog()
        }
    }

    private fun openCommissionDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_commission)

        val btnAdd = dialog.findViewById(R.id.btnAdd) as MaterialButton
        val btnClose = dialog.findViewById(R.id.ivCloseDialog) as ImageView
        val etAmount = dialog.findViewById(R.id.etAmount) as EditText

        btnAdd.setOnClickListener {
            if (etAmount.text.isNotEmpty()) {
                amount = etAmount.text.toString()
                if (isEdit) {
                    propertyAppointmentViewModel.agentEditCommission(
                        tourId.toInt(),
                        amount.toInt()
                    )
                } else {
                    propertyAppointmentViewModel.agentAddCommission(
                        tourId.toInt(),
                        userId.toInt(),
                        propertyId.toInt(),
                        amount.toInt()
                    )
                }
                dialog.dismiss()
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            propertyAppointmentViewModel.agentPropertyAppointmentDetails(passedTourId)
        }
    }
}