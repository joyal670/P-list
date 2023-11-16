package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertybook

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.myclient.users.details.UsersPropertyViewDetailedActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertybook.adapter.UploadDocumentsAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.PropertyViewDetailsActivity
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.utils.AppPreferences.is_sale
import com.property.propertyagent.utils.AppPreferences.is_user_property_booked
import com.property.propertyagent.utils.AppPreferences.selected_package_id
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_appointmentbook.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("PrivatePropertyName")
class PropertyBookActivity : BaseActivity() {
    private lateinit var propertyBookViewModel: PropertyBookViewModel
    private val PICK_IMAGES_CODE = 0
    private var images: String? = null
    private var uploadfilecheck: Boolean = false
    private var checkIn = false
    private var checkOut = false
    private lateinit var checkInDate: Date
    private lateinit var checkOutDate: Date
    private var paymentStatus: Int = -1
    val payments =
        arrayOf("Choose a Payment", "If Net Banking / Banking", "If Cash")
    private var passedTourId = ""
    private var userId = ""
    private var propertyId = ""
    private var userPropertyId = ""
    private lateinit var imageAdapter: UploadDocumentsAdapter
    private lateinit var imagesUpload: ArrayList<String>
    private var imagesList: ArrayList<Uri?>? = null
    private var position = 0

    private var packageNameList = ArrayList<String>()
    private var packageIdList = ArrayList<Int>()

    private var isLoadingMain: Boolean = false

    override val layoutId: Int
        get() = R.layout.activity_appointmentbook
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.bookproperty)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        passedTourId = intent.getStringExtra("tour_id").toString()
        propertyBookViewModel.agentProceedBookDetails(passedTourId)

        if (is_sale) {
            tvCheckoutHead.isVisible = false
            checkoutContainer.isVisible = false
        }

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
        imagesList = ArrayList()
        if (imagesList!!.size <= 0) {
            btnUpload.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupUI() {
        val gridLayoutManager = GridLayoutManager(this, 4)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvAttachFileLists.layoutManager = gridLayoutManager
        imageAdapter = UploadDocumentsAdapter(imagesList!!) {
            imagesUpload.removeAt(it)
            imagesList!!.removeAt(it)
            imageAdapter.notifyDataSetChanged()
            if (imagesList!!.size <= 0) {
                btnUpload.visibility = View.GONE
            } else {
                btnUpload.visibility = View.VISIBLE
            }
        }
        rvAttachFileLists.adapter = imageAdapter
    }

    override fun setupViewModel() {
        propertyBookViewModel = PropertyBookViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        propertyBookViewModel.getAgentProceedBookDetailsResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.response != null) {
                        if (!it.data.response.equals(null)) {
                            tvAgentPendingAmountValue.text =
                                getString(R.string.sar) + " " + it.data.response.pending_amount
                            tvAgentTokenAmountValue.text =
                                getString(R.string.sar) + " " + it.data.response.token_amount
                            propertyId = it.data.response.property_id.toString()
                            userId = it.data.response.user_id.toString()
                            userPropertyId = it.data.response.user_property_id.toString()

                            setUpUserPropertyTask()

                            propertyBookViewModel.agentPackageList(propertyId.toInt())
                        }
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
                    if (!isLoadingMain) {
                        showProgress()
                        isLoadingMain = true
                    }
                }
            }
        }
        propertyBookViewModel.getBookPropertyResponse().observe(this) {
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
                        is_user_property_booked = true
                        val intent = Intent(this, UsersPropertyViewDetailedActivity::class.java)
                        intent.putExtra("tour_id", passedTourId)
                        startActivity(intent)
                        finishAffinity()
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
        propertyBookViewModel.getAgentUploadPropertyDocumentsResponse().observe(this) {
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
        propertyBookViewModel.getAgentRequestContractDetailsResponse().observe(this) {
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

        propertyBookViewModel.getAgentPackageListResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    isLoadingMain = false
                    if (it.data?.packages != null) {
                        packageIdList.clear()
                        packageNameList.clear()
                        it.data.packages.forEach { it1 ->
                            packageIdList.addAll(listOf(it1.id))
                            packageNameList.addAll(listOf(it1.offer_package_name))
                        }
                        setUpPackageList()
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
                    if (!isLoadingMain) {
                        showProgress()
                    }
                }
            }
        }
    }

    private fun setUpPackageList() {

        bookappoinment_packagesSpinner.clearSelectedItem()
        bookappoinment_packagesSpinner.setItems(packageNameList)
    }

    private fun setUpUserPropertyTask() {
        if (userPropertyId == "0") {
            llUserPropertyUploadActions.isVisible = false
            tvMonthHeading.isVisible = false
            lluserPropertyAction.isVisible = false
            btnRequestContract.isVisible = false
        } else {
            tvMonthHeading.isVisible = true
            llUserPropertyUploadActions.isVisible = true
            lluserPropertyAction.isVisible = true
            btnRequestContract.isVisible = true
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
                    images = CommonUtils.getImageRealPath(imageUri, this)
                    etUploadbill.setText(getString(R.string.uploaded))
                    uploadfilecheck = true
                } else {
                    val imageUri = data.data
                    images = CommonUtils.getImageRealPath(imageUri, this)
                    etUploadbill.setText(getString(R.string.uploaded))
                    uploadfilecheck = true
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onClicks() {
        bookAppoinmentCheckInDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
                .also {
                    title = getString(R.string.pick_date)
                }
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val formatter =
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // modify format
                    val date = formatter.format(Date(it))

                    bookAppoinmentCheckInDate.setText(date.toString())
                    checkIn = true
                    checkInDate = Date(it)
                }
            }

            datePicker.show(supportFragmentManager, "MyTAG")
        }

        bookAppoinmentCheckOutDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
                .also {
                    title = getString(R.string.pick_date)
                }
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val formatter =
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // modify format
                    val date = formatter.format(Date(it))

                    bookAppoinmentCheckOutDate.setText(date.toString())
                    checkOut = true
                    checkOutDate = Date(it)
                }
            }

            datePicker.show(supportFragmentManager, "MyTAG")
        }
        etUploadbill.setOnClickListener {
            checkPermission()
        }
        btnAttachFile.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openImageFileChooserForDocument()
                }
            }
        }
        btnUpload.setOnClickListener {
            if (userPropertyId.isNotBlank()) {
                propertyBookViewModel.agentUploadUserPropertyDocumentsDetails(
                    this,
                    userPropertyId,
                    imagesUpload
                )
            }
        }
        btnRequestContract.setOnClickListener {
            if (userPropertyId.isNotBlank()) {
                propertyBookViewModel.agentRequestContract(userPropertyId, passedTourId)
            }
        }
        btnPayNowAgent.setOnClickListener {
            if (!is_sale) {
                if (!checkIn) {
                    Toaster.showToast(
                        this, getString(R.string.date_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                } else if (!checkOut) {
                    Toaster.showToast(
                        this, getString(R.string.date_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                } else if (!((checkInDate < checkOutDate) && (checkOutDate > checkInDate))) {
                    Toaster.showToast(
                        this, getString(R.string.checkindate_lessthan_checkout),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                } else if (!((paymentStatus == 1) || (paymentStatus == 2))) {
                    Toaster.showToast(
                        this, getString(R.string.upload_file),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                } else if (etNofoPayments.text.trim().toString().equals(null)) {
                    Toaster.showToast(
                        this, getString(R.string.add_no_of_payments),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                } else {
                    val startDate: String = bookAppoinmentCheckInDate.text.trim().toString()
                    val endDate: Any
                    endDate = if (is_sale) {
                        "00-00-0000"
                    } else {
                        bookAppoinmentCheckOutDate.text.trim().toString()
                    }
                    propertyBookViewModel.uploadBookPropertyDetails(
                        this,
                        propertyId,
                        userId,
                        startDate,
                        endDate,
                        paymentStatus,
                        etNofoPayments.text.trim().toString(),
                        passedTourId,
                        images.toString()
                    )
                }
            } else {
                if (!checkIn) {
                    Toaster.showToast(
                        this, getString(R.string.date_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                } else if (!((paymentStatus == 1) || (paymentStatus == 2))) {
                    Toaster.showToast(
                        this, getString(R.string.upload_file),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                } else if (etNofoPayments.text.trim().toString().equals(null)) {
                    Toaster.showToast(
                        this, getString(R.string.add_no_of_payments),
                        Toaster.State.WARNING, Toast.LENGTH_LONG
                    )
                } else {
                    val startDate: String = bookAppoinmentCheckInDate.text.trim().toString()
                    val endDate: Any
                    endDate = if (is_sale) {
                        "00-00-0000"
                    } else {
                        bookAppoinmentCheckOutDate.text.trim().toString()
                    }
                    propertyBookViewModel.uploadBookPropertyDetails(
                        this,
                        propertyId,
                        userId,
                        startDate,
                        endDate,
                        paymentStatus,
                        etNofoPayments.text.trim().toString(),
                        passedTourId,
                        images.toString()
                    )
                }
            }
        }

        btnPropertyDetails.setOnClickListener {
            if (propertyId.isNotBlank()) {
                val intent = Intent(this, PropertyViewDetailsActivity::class.java)
                intent.putExtra("property_id", propertyId)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.empty_property_id), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        bookappoinment_packagesSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.i("TAG", oldIndex.toString() + oldItem + newText)
            selected_package_id = packageIdList[newIndex].toString()

        }
    }

    private fun openImageFileChooserForDocument() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_PICK
        startForResult.launch(Intent.createChooser(intent, "Select Image(s)"))
    }

    @SuppressLint("NotifyDataSetChanged")
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent!!.clipData != null) {
                    val count = intent.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = intent.clipData!!.getItemAt(i).uri
                        imagesUpload.add(CommonUtils.getImageRealPath(imageUri, this).toString())
                        imagesList!!.add(imageUri)
                        imageAdapter.notifyDataSetChanged()
                    }
                    position = 0
                } else {
                    val imageUri = intent.data
                    imagesUpload.add(CommonUtils.getImageRealPath(imageUri, this).toString())
                    imagesList!!.add(imageUri)
                    imageAdapter.notifyDataSetChanged()
                    position = 0
                }
                btnUpload.visibility = View.VISIBLE
            }
        }
}