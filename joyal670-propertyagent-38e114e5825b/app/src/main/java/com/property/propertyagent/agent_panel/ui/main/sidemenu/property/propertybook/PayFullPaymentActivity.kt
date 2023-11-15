package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertybook

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_pay_full_payment.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*

@Suppress("PrivatePropertyName")
class PayFullPaymentActivity : BaseActivity() {

    private lateinit var propertyBookViewModel: PropertyBookViewModel

    private val PICK_IMAGES_CODE = 0
    private var images: String = ""
    private var uploadfilecheck: Boolean = false
    private var paymentStatus: Int = -1
    private var imagesUpload: ArrayList<String?>? = null
    val payments =
        arrayOf("Choose a Payment", "If Net Banking / Banking", "If Cash")

    private var tourId = ""
    private var userPropertyId = ""
    private var pendingAmount = ""

    override val layoutId: Int
        get() = R.layout.activity_pay_full_payment
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.payment)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)

        tourId = intent.getStringExtra("PASSED_TOUR_ID").toString()
        userPropertyId = intent.getStringExtra("USER_PROPERTY_ID").toString()

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, payments)
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        bookappoinment_paymentSpinner.adapter = adapter1
        bookappoinment_paymentSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    when (position) {
                        0 -> {

                        }
                        1 -> {
                            netBankingLayout.visibility = View.VISIBLE
                            choosePaymentText.text = getString(R.string.if_net_banking_banking)
                            paymentStatus = 1

                        }
                        2 -> {
                            netBankingLayout.visibility = View.VISIBLE
                            choosePaymentText.text = getString(R.string.if_cash)
                            paymentStatus = 2
                        }
                    }
                }
            }
        imagesUpload = ArrayList()
        propertyBookViewModel.agentFetchPendingAmount(userPropertyId, tourId)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        propertyBookViewModel = PropertyBookViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        propertyBookViewModel.getAgentPendingAmountResponse()
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data?.data != null) {
                            tvAgentPendingAmountValue.text =
                                getString(R.string.sar) + " " + it.data.data.pending_amount
                            pendingAmount = it.data.data.pending_amount
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

        propertyBookViewModel.getAgentPayFullAmountResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (it.data?.response != null) {
                        Toaster.showToast(
                            this,
                            it.data.response,
                            Toaster.State.SUCCESS,
                            Toast.LENGTH_LONG
                        )
                        onBackPressed()
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)

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
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.LOADING -> {
                    showProgress()
                }
            }
        }
    }

    override fun onClicks() {
        etUploadbill.setOnClickListener {
            checkPermission()
        }

        btnPayNowAgent.setOnClickListener {
            if (!((paymentStatus == 1) || (paymentStatus == 2))) {
                Toaster.showToast(
                    this, getString(R.string.upload_file),
                    Toaster.State.WARNING, Toast.LENGTH_LONG
                )
            } else {
                propertyBookViewModel.agentPayFullAmount(
                    this,
                    pendingAmount,
                    paymentStatus,
                    tourId,
                    images
                )
            }
        }
    }

    private fun checkPermission() {
        permissionsBuilder(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).build().send { result ->
            if (result.allGranted()) {
                openImageFileChooser()
            }
        }
    }

    private fun openImageFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val imageUri = data.clipData!!.getItemAt(0).uri
                    images = CommonUtils.getImageRealPath(imageUri, this).toString()
                    etUploadbill.setText(getString(R.string.uploaded))
                    uploadfilecheck = true
                } else {
                    val imageUri = data.data
                    images = CommonUtils.getImageRealPath(imageUri, this).toString()
                    etUploadbill.setText(getString(R.string.uploaded))
                    uploadfilecheck = true

                }
            }
        }
    }

}