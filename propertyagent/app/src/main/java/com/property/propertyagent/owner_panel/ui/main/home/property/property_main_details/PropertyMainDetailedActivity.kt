package com.property.propertyagent.owner_panel.ui.main.home.property.property_main_details

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.appbar.AppBarLayout
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_property_main_details.new.OwnerPropertyMainDetailsNewDocument
import com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history.activity.PaymentHistoryActivity
import com.property.propertyagent.owner_panel.ui.main.home.property.adapter.OwnerPropertyMainImageSliderAdapter
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import com.property.propertyagent.utils.AppPreferences.token
import kotlinx.android.synthetic.main.activity_building_details.*
import kotlinx.android.synthetic.main.content_scrolling_building.*

class PropertyMainDetailedActivity : BaseActivity() {

    private lateinit var propertyMainDetailViewModel: PropertyMainDetailViewModel

    private var agentPhone: String = ""
    private var agentName: String = ""
    private var agentImage: String = ""
    private var contractImage: String = ""
    private var propertyImages = ArrayList<String>()

    private lateinit var vpPropertyImageSliderAdapter: OwnerPropertyMainImageSliderAdapter

    override val layoutId: Int
        get() = R.layout.activity_building_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar_ownertwo)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_ownertwo.title = getString(R.string.details)
        Log.i("TAG", "initData: $token")

        app_bartwo.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                //  State Expanded
                verticalOffset == 0 -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_back_button)
                    toolbar_ownertwo.title = ""
                    green_flag.visibility = View.VISIBLE

                }
                //  State Collapsed
                Math.abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back)
                    toolbar_ownertwo.title = getString(R.string.property_images)
                    green_flag.visibility = View.GONE

                }
                else -> Log.e("onelse", "toolbar")
            }
        })

        propertyMainDetailViewModel.ownerPropertyDetails(clicked_property_id)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        propertyMainDetailViewModel = PropertyMainDetailViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        propertyMainDetailViewModel.getOwnerPropertyDetailsResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        if (it.data!!.data.property != null) {
                            if (it.data.data.property.is_featured == 0) {
                                green_flag.isVisible = false
                            }

                            if (!it.data.data.property.documents.isNullOrEmpty()) {
                                setPropertyImageSlider(it.data.data.property.documents)
                            }


                            it.data.data.property.documents.forEach {
                                propertyImages.add(it.document)
                            }

                            if (it.data.data.property.contract_file.toString().isNotBlank()) {
                                contractImage = it.data.data.property.contract_file.toString()
                            } else {
                                btnViewContract.isVisible = false
                                tvContractHead.isVisible = false
                            }

                            when (it.data.data.property.status) {
                                0 -> {
                                    tvStatus.text = getString(R.string.pendingForAproval)
                                    tvStatus.setTextColor(Color.parseColor("#F47458"))
                                    tvPropertyPricing.visibility = View.GONE
                                    tvRentLayout.visibility = View.GONE
                                    layoutRent.visibility = View.GONE
                                    managementLayout.visibility = View.GONE
                                    tvRevenue.visibility = View.GONE
                                    netWorthLayout.visibility = View.GONE
                                    incomeLayout.visibility = View.GONE
                                    outstandingLayout.visibility = View.GONE
                                    pendingLayout.visibility = View.GONE
                                    view1.visibility = View.GONE
                                    view2.visibility = View.GONE
                                    view3.visibility = View.GONE
                                }
                                1 -> {
                                    tvStatus.text = getString(R.string.propertyApproved)
                                    tvStatus.setTextColor(Color.parseColor("#6AC58C"))
                                }
                                2 -> {
                                    tvStatus.text = getString(R.string.propertyRejected)
                                    tvStatus.setTextColor(Color.parseColor("#FB0007"))
                                    tvPropertyPricing.visibility = View.GONE
                                    tvRentLayout.visibility = View.GONE
                                    layoutRent.visibility = View.GONE
                                    managementLayout.visibility = View.GONE
                                    tvRevenue.visibility = View.GONE
                                    netWorthLayout.visibility = View.GONE
                                    incomeLayout.visibility = View.GONE
                                    outstandingLayout.visibility = View.GONE
                                    pendingLayout.visibility = View.GONE
                                    view1.visibility = View.GONE
                                    view2.visibility = View.GONE
                                    view3.visibility = View.GONE
                                }
                            }

                            tvPropertyName.text = it.data.data.property.property_name.toString()
                            tvPropertyCode.text = it.data.data.property.property_reg_no.toString()

                            agentName = it.data.data.property.property_agent.name
                            agentPhone = it.data.data.property.property_agent.phone
                            agentImage = it.data.data.property.property_agent.image
                            tvAgentName.text = it.data.data.property.property_agent.name
                            if (it.data.data.property.property_agent != null) {
                                if (!it.data.data.property.property_agent.image.isNullOrBlank()) {
                                    ivAgentImage.loadImagesWithGlideExtByFitCenter(it.data.data.property.property_agent.image)
                                } else {
                                    layoutpropertyAgentDetails.isVisible = false
                                }
                            } else {
                                layoutpropertyAgentDetails.isVisible = false
                            }

                            /* property_to -> 1 = Sale */
                            /* property_to -> 0 = Rent */
                            try {
                                if (it.data.data.property.property_to == 1) {
                                    tvAmountTitle.text = getString(R.string.amount)
                                    tvRentAmount.text =
                                        it.data.data.property.selling_price.toString()
                                    layoutRent.visibility = View.GONE
                                    layoutRevenue.visibility = View.GONE
                                    tvManagementCharge.text =
                                        it.data.data.property.management_charge.toString()
                                } else {
                                    tvRentAmount.text = it.data.data.property.rent.toString()
                                    tvRentalFrequency.text =
                                        it.data.data.property.property_rent_frequency.type.toString()
                                    tvManagementCharge.text =
                                        it.data.data.property.management_charge.toString()
                                }
                            } catch (ex: Exception) {

                            }

                            tvNetWorth.text =
                                getString(R.string.sar) + " " + it.data.data.property.net_worth.toString()
                            tvIncome.text =
                                getString(R.string.sar) + " " + it.data.data.property.income.toString()
                            tvOutStandingDue.text =
                                getString(R.string.sar) + " " + it.data.data.property.outstanding_due.toString()
                            tvPending.text =
                                getString(R.string.sar) + " " + it.data.data.property.pending.toString()

                            container.isVisible = true
                        }
                    }
                    Status.LOADING -> {
                        showProgressOwner()
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
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
                        dismissProgressOwner()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })

        propertyMainDetailViewModel.getOwnerPropertyReportResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        if (it.data!!.data.pdf.isNotBlank()) {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(it.data.data.pdf))
                            startActivity(browserIntent)
                        }
                    }
                    Status.LOADING -> {
                        showProgressOwner()
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
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
                        dismissProgressOwner()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })
    }

    private fun setPropertyImageSlider(documents: List<OwnerPropertyMainDetailsNewDocument>) {
        vpPropertyImageSliderAdapter = OwnerPropertyMainImageSliderAdapter(
            supportFragmentManager,
            lifecycle,
            documents as ArrayList<OwnerPropertyMainDetailsNewDocument>
        )
        vpPropertyDetailsImageSlidertwo.adapter = vpPropertyImageSliderAdapter
        dotsIndicatorDetailstwo.setViewPager2(vpPropertyDetailsImageSlidertwo)
    }

    override fun onClicks() {
        building_activity_payment_historyBtn.setOnClickListener {
            val id: Int = clicked_property_id.toInt()
            val intent = Intent(this, PaymentHistoryActivity::class.java)
            intent.putExtra(Constants.SELECTED_PAYMENT_ID, id)
            startActivity(intent)
        }

        building_activity_other_expenseBtn.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.other_expense_layout)

            val saveBtn = dialog.findViewById(R.id.other_expese_saveBtn) as Button

            saveBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams

        }

        building_activity_WhatsappBtn.setOnClickListener {
            CommonUtils.openWhatsAppEnquiry(this, agentPhone, getString(R.string.hai))
        }

        building_activity_CallBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
                if (result.allGranted()) {
                    CommonUtils.dailPhone(this, agentPhone)
                }
            }
        }

        btnReport.setOnClickListener {
            propertyMainDetailViewModel.ownerPropertyReport(clicked_property_id)
        }

        btnViewContract.setOnClickListener {
            if (!contractImage.isNullOrBlank()) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(contractImage))
                startActivity(browserIntent)
            }
        }

        building_activity_ViewProfileBtn.setOnClickListener {


            CommonUtils.showUserProfile(
                this,
                getString(R.string.agent),
                agentName,
                agentPhone,
                agentImage
            )


            /*   val bottom = a(this, R.style.ThemeOverlay_App_BottomSheetDialog)
               val bottomSheet: View = this.layoutInflater.inflate(R.layout.profile_layout_bottom_sheet, null)
               val closeBtn = bottomSheet.findViewById<ImageButton>(R.id.ivClose)
               val imageView = bottomSheet.findViewById<CircleImageView>(R.id.civProfilePicRequestDialog)
               val name = bottomSheet.findViewById<TextView>(R.id.tvProfileNameRequestDialog)
               val userType = bottomSheet.findViewById<TextView>(R.id.tvUserTypeRequestDialog)
               val callBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivPhoneRequestDialog)
               val msgBtn = bottomSheet.findViewById<MaterialButton>(R.id.ivWhatsappRequestDialog)

               callBtn.setBackgroundColor(Color.parseColor("#0E63D7"))
               msgBtn.setBackgroundColor(Color.parseColor("#0E63D7"))

               closeBtn.setOnClickListener {
                   bottom.dismiss()
               }
               imageView.loadImagesWithGlideExt(agentImage)
               name.text = agentName
               userType.text = getString(R.string.agent)
               callBtn.setOnClickListener {
                   if (agentPhone.isNotBlank()) {
                       CommonUtils.dailPhone(this, agentPhone)
                   }

               }
               msgBtn.setOnClickListener {
                   if (agentPhone.isNotBlank()) {
                       CommonUtils.openWhatsAppEnquiry(this, agentPhone, getString(R.string.hai))
                   }
               }


               bottom.setContentView(bottomSheet)
               bottom.show()*/


            /*  val dialog = this.let { it1 -> Dialog(it1) }
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

              if (agentImage.isNotBlank()) {
                  civProfilePicRequestDialog.loadImagesWithGlideExt(agentImage)
              }
              tvProfileNameRequestDialog.text = agentName
              tvUserTypeRequestDialog.text = "Agent"
              if (agentPhone.isNotBlank()) {
                  ivPhoneRequestDialog.setOnClickListener {
                      CommonUtils.dailPhone(this, agentPhone)
                  }
                  ivWhatsappRequestDialog.setOnClickListener {
                      CommonUtils.openWhatsAppEnquiry(
                          this,
                          agentPhone,
                          getString(R.string.hai)
                      )
                  }
              }

              dialog.show()
              val layoutParams = WindowManager.LayoutParams()
              layoutParams.copyFrom(dialog.window?.attributes)
              layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
              layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
              dialog.window?.attributes = layoutParams
  */
        }

    }
}