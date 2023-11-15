package com.property.propertyuser.ui.main.payment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityPaymentBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.home.dashboard.DashboardActivity
import com.property.propertyuser.ui.main.rating.RatingActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : BaseActivity<ActivityPaymentBinding>(), ActivityListener {
    private lateinit var paymentViewModel: PaymentViewModel
    private var selectedFile: String? = null
    private var documentStatus: Int? = null
    private var passedBookingOrPropertyId = ""
    private var passedRequestedId = ""
    private var passedType = ""
    private var passedUserPropertyIdRental = ""
    private var passedEventBookingId = ""
    private var passedPropertyIdRating = ""
    override fun getViewBinging(): ActivityPaymentBinding =
        ActivityPaymentBinding.inflate(layoutInflater)

    override val layoutId: Int
        get() = R.layout.activity_payment
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle("PAY NOW")
        passedType = intent.getStringExtra("passed_type").toString()
        passedPropertyIdRating = intent.getStringExtra("passedPropertyIdRating").toString()
        passedBookingOrPropertyId =
            intent.getStringExtra("passed_booking_id_or_property_id").toString()
        passedRequestedId = intent.getStringExtra("request_id").toString()
        passedUserPropertyIdRental = intent.getStringExtra("user_property_id_passed").toString()
        passedEventBookingId = intent.getStringExtra("event_booking_id").toString()

        if (passedType == "service_payment") {
            binding.chkOwner.visibility = View.VISIBLE
        } else {
            binding.chkOwner.visibility = View.GONE
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        paymentViewModel = PaymentViewModel()
    }

    override fun setupObserver() {
        paymentViewModel.getPaymentBillResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    Log.e("onPaymentFileResponse", Gson().toJson(it))
                    Toaster.showToast(
                        this, it.data!!.response,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG
                    )
                    val intent = Intent(this, RatingActivity::class.java)
                    intent.putExtra("passedPropertyIdRating", passedPropertyIdRating)
                    startActivity(intent)
                    finish()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this,
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this, getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
        paymentViewModel.getUserBookedPaymentBillResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> {
                        showLoader()
                    }
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("onPaymentFileResponse", Gson().toJson(it))
                        Toaster.showToast(
                            this, it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finishAffinity()
                    }
                    Status.DATA_EMPTY -> {
                        Log.e("TAG", "setupObserver: data")
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            it.data!!.response,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            Toaster.showToast(
                                this, getString(R.string.no_internet),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        }
                    }
                    Status.ERROR -> {
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            it.data!!.response,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }

                }
            })
        paymentViewModel.getUserServicePaymentBillResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showLoader()
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("onPaymentFileResponse", Gson().toJson(it))
                        Toaster.showToast(
                            this, it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        AppPreferences.reload_service_status_list_for_payment = true
                        finish()
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            Toaster.showToast(
                                this, getString(R.string.no_internet),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        }
                    }

                }
            })
        paymentViewModel.getUserRentalPaymentBillResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> {
                        Log.e("TAG", "setupObserver: LOADING")
                        showLoader()
                    }
                    Status.SUCCESS -> {
                        Log.e("TAG", "setupObserver: SUCCESS")
                        dismissLoader()
                        Log.e("onPaymentFileResponse", Gson().toJson(it))
                        Toaster.showToast(
                            this, it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        finish()
                    }
                    Status.DATA_EMPTY -> {
                        Log.e("TAG", "setupObserver: DATA_EMPTY")
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            it.data!!.response,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        Log.e("TAG", "setupObserver: NO_INTERNET")
                        dismissLoader()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            Toaster.showToast(
                                this, getString(R.string.no_internet),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        }
                    }
                    else -> {
                        Log.e("TAG", "setupObserver: else")
                        dismissLoader()
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }

                }
            })
        paymentViewModel.getUserEventBookPackagePaymentBillResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showLoader()
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("onPaymentFileResponse", Gson().toJson(it))
                        Toaster.showToast(
                            this, it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finishAffinity()
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            Toaster.showToast(
                                this, getString(R.string.no_internet),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        }
                    }

                }
            })
    }

    override fun onClicks() {
        btnSendFile.setOnClickListener {
            if (!(tvUploadPaymentfile.text == getString(R.string.uploaded) ||
                        tvUploadCashFile.text == getString(R.string.uploaded))
            ) {
                Toaster.showToast(
                    this, getString(R.string.upload_file),
                    Toaster.State.WARNING, Toast.LENGTH_LONG
                )
            } else {
                if (passedType == "book_a_property") {
                    paymentViewModel.uploadPaymentBillDetails(
                        this,
                        passedBookingOrPropertyId,
                        documentStatus,
                        selectedFile
                    )
                } else if (passedType == "booked_view_details") {
                    paymentViewModel.uploadUserBookedPaymentBillDetails(
                        this,
                        passedBookingOrPropertyId,
                        documentStatus,
                        selectedFile
                    )
                } else if (passedType == "service_payment") {
                    if (binding.chkOwner.isChecked) {
                        paymentViewModel.uploadUserServicePaymentBillDetails(
                            this,
                            passedRequestedId,
                            documentStatus,
                            selectedFile,
                            "1"
                        )
                    } else {
                        paymentViewModel.uploadUserServicePaymentBillDetails(
                            this,
                            passedRequestedId,
                            documentStatus,
                            selectedFile,
                            "0"
                        )
                    }

                } else if (passedType == "rental_view_details_pay") {
                    paymentViewModel.uploadUserRentalPaymentBillDetails(
                        this,
                        passedUserPropertyIdRental,
                        documentStatus,
                        selectedFile
                    )
                } else if (passedType == "event_payment") {
                    paymentViewModel.uploadUserEventBookPackagePaymentBillDetails(
                        this, passedEventBookingId, documentStatus, selectedFile
                    )
                }
            }
        }
        checkboxBank.setOnClickListener {
            if (checkboxBank.isChecked) {
                documentStatus = 1

                tvUploadPaymentfile.isClickable = true
                tvUploadPaymentfile.isEnabled = true
                tvUploadCashFile.isClickable = false
                tvUploadCashFile.isEnabled = false
                tvUploadPaymentfile.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_round_border_green)
                tvUploadCashFile.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_round_border_gray)
                checkboxCash.isChecked = false
            }
        }
        checkboxCash.setOnClickListener {
            if (checkboxCash.isChecked) {
                documentStatus = 2

                tvUploadPaymentfile.isClickable = false
                tvUploadPaymentfile.isEnabled = false
                tvUploadCashFile.isClickable = true
                tvUploadCashFile.isEnabled = true
                tvUploadPaymentfile.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_round_border_gray)
                tvUploadCashFile.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_round_border_green)
                checkboxBank.isChecked = false
            }
        }
        tvUploadPaymentfile.setOnClickListener {
            checkPermission()
        }
        tvUploadCashFile.setOnClickListener {
            checkPermission()
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
        //val intent = Intent()
        //intent.type = "image/*"
        /*intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE , false)
        intent.action = Intent.ACTION_PICK
        startForResult.launch(Intent.createChooser(intent , "Select Image(s)") )*/
        permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
            if (result.allGranted()) {
                ImagePicker.with(this)
                    .crop()
                    .start()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            selectedFile = if (data!!.clipData != null) {
                val imageUri = data.clipData!!.getItemAt(0).uri
                imageUri?.path.toString()
            } else {
                val imageUri = data.data
                imageUri?.path.toString()
            }
            if (documentStatus == 1) {
                tvUploadPaymentfile.text = getString(R.string.uploaded)
                tvUploadCashFile.text = getString(R.string.tvUploadCashfile)
            }
            if (documentStatus == 2) {
                tvUploadCashFile.text = getString(R.string.uploaded)
                tvUploadPaymentfile.text = getString(R.string.tvUploadPaymentfile)
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    /*  private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
              result: ActivityResult ->
          if (result.resultCode == Activity.RESULT_OK) {
              val intent = result.data

              val imageUri = intent?.data
              selectedFile=CommonMethods.getImageRealPath(imageUri , this)
              Log.e("selectedfile",selectedFile.toString())
              if(documentStatus==1){
                  tvUploadPaymentfile.text=getString(R.string.uploaded)
                  tvUploadCashfile.text=getString(R.string.tvUploadCashfile)
              }
              if(documentStatus==2){
                  tvUploadCashfile.text=getString(R.string.uploaded)
                  tvUploadPaymentfile.text=getString(R.string.tvUploadPaymentfile)
              }
              Log.e("imageuri",selectedFile.toString())
          }
      }
  */
    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}