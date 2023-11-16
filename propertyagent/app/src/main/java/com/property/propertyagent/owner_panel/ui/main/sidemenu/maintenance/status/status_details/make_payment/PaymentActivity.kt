package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status_details.make_payment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.utils.AppPreferences
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class PaymentActivity : BaseActivity() {

    private lateinit var paymentViewModel : PaymentViewModel

    private var passRequestId = ""
    private var passedType = ""
    private var documentStatus : Int? = null
    private var selectedFile : String? = null

    override val layoutId : Int
        get() = R.layout.activity_payment
    override val setToolbar : Boolean
        get() = false
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(owner_toolbar)
        window.statusBarColor = ContextCompat.getColor(this , R.color.color_accent_blue_statusbar)
        supportActionBar?.title = getString(R.string.payment)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        passRequestId = intent.getStringExtra("request_id").toString()
        passedType = intent.getStringExtra("passed_type").toString()

        Log.e("passe values" , "$passRequestId--$passedType")
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        paymentViewModel = PaymentViewModel()
    }

    override fun setupObserver() {
        paymentViewModel.getOwnerServicePaymentBillResponse()
            .observe(this , androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showProgressOwner()
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        Log.e("onPaymentFileResponse" , Gson().toJson(it))
                        Toaster.showToast(this , it.data!!.response ,
                            Toaster.State.SUCCESS , Toast.LENGTH_LONG)
                        AppPreferences.reload_service_status_list_for_payment = true
                        finish()
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(this ,
                            getString(R.string.data_empty) ,
                            Toaster.State.ERROR ,
                            Toast.LENGTH_LONG)
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong) ,
                                Toaster.State.ERROR , Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager , "TAG")
                        }
                    }
                }
            })
    }

    override fun onClicks() {
        btnSendFile.setOnClickListener {
            Log.e("service_payment" , passedType)
            if (!(tvUploadPaymentfile.text == getString(R.string.uploaded) ||
                        tvUploadCashfile.text == getString(R.string.uploaded))
            ) {
                Toaster.showToast(this , getString(R.string.upload_file) ,
                    Toaster.State.WARNING , Toast.LENGTH_LONG)
            } else {
                if (passedType == "service_payment") {
                    paymentViewModel.ownerServicePaymentBillDetails(this ,
                        passRequestId ,
                        documentStatus ,
                        selectedFile)
                }

            }
        }
        checkboxBank.setOnClickListener {
            if (checkboxBank.isChecked) {
                documentStatus = 1

                tvUploadPaymentfile.isClickable = true
                tvUploadPaymentfile.isEnabled = true
                tvUploadCashfile.isClickable = false
                tvUploadCashfile.isEnabled = false
                tvUploadPaymentfile.background =
                    ContextCompat.getDrawable(this , R.drawable.bg_round_border_blue_s)
                tvUploadCashfile.background =
                    ContextCompat.getDrawable(this , R.drawable.bg_round_border_gray)
                checkboxCash.isChecked = false
            }
        }
        checkboxCash.setOnClickListener {
            if (checkboxCash.isChecked) {
                documentStatus = 2

                tvUploadPaymentfile.isClickable = false
                tvUploadPaymentfile.isEnabled = false
                tvUploadCashfile.isClickable = true
                tvUploadCashfile.isEnabled = true
                tvUploadPaymentfile.background =
                    ContextCompat.getDrawable(this , R.drawable.bg_round_border_gray)
                tvUploadCashfile.background =
                    ContextCompat.getDrawable(this , R.drawable.bg_round_border_blue_s)
                checkboxBank.isChecked = false
            }
        }
        tvUploadPaymentfile.setOnClickListener {
            checkPermission()
        }
        tvUploadCashfile.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        permissionsBuilder(
            Manifest.permission.READ_EXTERNAL_STORAGE ,
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE , false)
        intent.action = Intent.ACTION_PICK
        startForResult.launch(Intent.createChooser(intent , "Select Image(s)"))
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result : ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data

                val imageUri = intent?.data
                selectedFile = CommonUtils.getImageRealPath(imageUri , this)
                Log.e("selectedfile" , selectedFile.toString())
                if (documentStatus == 1) {
                    tvUploadPaymentfile.text = getString(R.string.uploaded)
                    tvUploadCashfile.text = getString(R.string.tvUploadCashfile)
                }
                if (documentStatus == 2) {
                    tvUploadCashfile.text = getString(R.string.uploaded)
                    tvUploadPaymentfile.text = getString(R.string.tvUploadPaymentfile)
                }
                Log.e("imageuri" , selectedFile.toString())
            }
        }
}