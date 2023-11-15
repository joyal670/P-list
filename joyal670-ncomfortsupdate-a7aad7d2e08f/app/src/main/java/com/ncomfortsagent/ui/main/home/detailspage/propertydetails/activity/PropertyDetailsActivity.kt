package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityPropertyDetailsBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsAmenityCategory
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsDocument
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsList
import com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.adapter.ImageUploadAdapter
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters.PaymentHistoryAdapter
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters.PropertyDetailedAmenitiesAdapter
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters.PropertyDetailsAdapter
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters.PropertyImageSliderAdapter
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.viewmodel.PropertyDetailsViewModel
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.CommonUtils.Companion.shareProperty
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar
import com.ncomfortsagent.utils.Constants.PROPERTY_ID
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected
import java.util.*

class PropertyDetailsActivity : BaseActivity<ActivityPropertyDetailsBinding>() {

    private var selectedPropertyId: Int? = null
    private lateinit var propertyDetailsViewModel: PropertyDetailsViewModel
    private var propertyImages = ArrayList<AgentPropertyDetailsDocument>()

    //private var amenitiesData = ArrayList<Int>()
    private var amenitiesCategory = ArrayList<AgentPropertyDetailsAmenityCategory>()
    private var propertyShareImage = ""

    private lateinit var paymentHistoryAdapter: PaymentHistoryAdapter

    var attachment: TextView? = null
    private lateinit var imageAdapter: ImageUploadAdapter
    private var images: ArrayList<String?>? = null
    private var imagesList: ArrayList<Uri?>? = null


    override val layoutId: Int
        get() = R.layout.activity_property_details
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityPropertyDetailsBinding =
        ActivityPropertyDetailsBinding.inflate(layoutInflater)

    override fun initData() {

        /* setup toolbar */
        imagesList = ArrayList()
        images = ArrayList()
        setSupportActionBar(binding.tool.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.tool.tvToolbarTitle.text = getString(R.string.property_details)


        /* get id's from intent */

        val appLinkIntent = intent
        val appLinkData = appLinkIntent.data
        val LINK = appLinkData.toString()
        if (LINK != "null") {
            val parts = LINK.split("/").toTypedArray()
            selectedPropertyId = parts[4].toInt()
        } else {
            selectedPropertyId = intent.getIntExtra(PROPERTY_ID, 0)
        }
        propertyDetailsViewModel.propertyDetails(selectedPropertyId!!)

        paymentHistoryAdapter = PaymentHistoryAdapter(this, { updateStatus(it) })
        binding.content.rvPaymentHistory.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = paymentHistoryAdapter
        }
    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {

        /* set strike and under line*/
        binding.content.tvPropertySubPrice.paintFlags =
            binding.content.tvPropertySubPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.content.tvAmenities.paintFlags =
            binding.content.tvAmenities.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        /* setup recyclerview */
        binding.content.rvDetailedAmenities.layoutManager = LinearLayoutManager(this)
    }

    override fun setupViewModel() {
        propertyDetailsViewModel = PropertyDetailsViewModel(this)
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        propertyDetailsViewModel.getAgentPropertyDetailsResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {

                    /* property images */
                    propertyImages.clear()
                    propertyImages.addAll(it.data!!.response.property_details.documents)
                    setUpPropertyImages()

                    binding.content.tvPropertyName.text =
                        it.data.response.property_details.property_name
                    binding.content.tvPropertyCode.text =
                        it.data.response.property_details.property_reg_no

                    /* property_to value
                    * 0 -> RENT
                    * 1 -> BUY */
                    /* Strike value (interchanging values) */
                    if (it.data.response.property_details.property_to == 0) {
                        binding.content.tvPropertyMainPrice.text =
                            getString(R.string.sar) + it.data.response.property_details.rent
                        binding.content.tvPropertySubPrice.text =
                            getString(R.string.sar) + it.data.response.property_details.selling_price
                        binding.content.tvPropertyType.text =
                            it.data.response.property_details.type_details.type + " " + getString(R.string.for1) + " " + getString(
                                R.string.rent
                            )
                        binding.content.tvPropertySubPrice.visibility = View.GONE
                        binding.content.view1.visibility = View.INVISIBLE
                    } else {
                        binding.content.tvPropertyMainPrice.text =
                            getString(R.string.sar) + it.data.response.property_details.selling_price
                        binding.content.tvPropertySubPrice.text =
                            getString(R.string.sar) + it.data.response.property_details.mrp
                        binding.content.tvPropertyType.text =
                            it.data.response.property_details.type_details.type + " " + getString(R.string.for1) + " " + getString(
                                R.string.sale
                            )
                    }

                    binding.content.tvPropertyBHK.text =
                        it.data.response.property_details.bhk.toString() + getString(R.string.bhk)

                    when {
                        it.data.response.property_details.bhk.toString() == "0" -> {
                            binding.content.tvPropertyBHK.visibility = View.GONE
                        }
                        it.data.response.property_details.bhk.toString() == "6" -> {
                            binding.content.tvPropertyBHK.text = getString(R.string.five_plus_bhk)
                        }
                        it.data.response.property_details.bhk.toString() >= "7" -> {
                            binding.content.tvPropertyBHK.text = getString(R.string.studio)
                        }
                    }

                    binding.content.tvLocation.text = it.data.response.property_details.location


                    /* amenities list */
                    binding.content.tvBedRoom.text =
                        it.data.response.property_details.Beds.toString()
                    binding.content.tvBathRoom.text =
                        it.data.response.property_details.Bathroom.toString()
                    binding.content.tvArea.text =
                        it.data.response.property_details.Area.toString() + getString(
                            R.string.sq_ft
                        )
                    when (it.data.response.property_details.furnished) {
                        0 -> {
                            binding.content.tvFurniture.text = getString(R.string.notFurnished)
                        }
                        1 -> {
                            binding.content.tvFurniture.text = getString(R.string.semiFurnished)
                        }
                        2 -> {
                            binding.content.tvFurniture.text = getString(R.string.fullyFurnished)
                        }
                    }

                    binding.content.tvHall.text =
                        it.data.response.property_details.Conference_Hall.toString()

                    binding.content.tvPropertyDescription.text =
                        it.data.response.property_details.description

                    if (it.data.response.property_details.description.isNullOrBlank()) {
                        binding.content.tvPropertyDescription.text =
                            getString(R.string.no_description_are_available)
                    }

                    /* amenities category */
                    amenitiesCategory.clear()
                    it.data.response.property_details.amenity_categories.let {
                        amenitiesCategory.addAll(it)
                        setUpAmenitiesCategoryRecyclerView()
                    }


                    if (propertyImages.size != 0) {
                        propertyShareImage = it.data.response.property_details.documents[0].document
                    }


                    when (it.data.response.property_details.occupied) {
                        0 -> {
                            binding.content.tvPropertyStatus.text = getString(R.string.vacant)
                            binding.content.tvPropertyStatus.setTextColor(Color.parseColor("#EABC6B"))

                            binding.content.tenantCard.visibility = View.GONE
                            binding.content.paymentHistoryCard.visibility = View.GONE
                            binding.content.updateStatusCard.visibility = View.GONE
                        }
                        1 -> {
                            binding.content.tvPropertyStatus.text = getString(R.string.occupied)
                            binding.content.tvPropertyStatus.setTextColor(Color.parseColor("#6AC58C"))

                            binding.content.paymentCard.visibility = View.GONE
                            binding.content.tenantCard.visibility = View.GONE
                            binding.content.paymentHistoryCard.visibility = View.GONE
                            binding.content.updateStatusCard.visibility = View.GONE
                        }
                    }

                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.content.lv1.visibility = View.VISIBLE
                    binding.content.lv1.visibility = View.VISIBLE
                    // binding.container.visibility = View.VISIBLE


                    /* payment split-up */
                    binding.content.tvTokenAmount.text =
                        it.data.response.property_details.token_amount
                    binding.content.tvSecurityAmount.text =
                        it.data.response.property_details.security_deposit
                    if (it.data.response.property_details.property_to == 0) {
                        binding.content.tvAmountType.text = getString(R.string.rent)
                        binding.content.tvTypeAmount.text = it.data.response.property_details.rent
                    } else {
                        binding.content.tvAmountType.text = getString(R.string.sale)
                        binding.content.tvTypeAmount.text =
                            it.data.response.property_details.selling_price
                    }

                    if (!TextUtils.isEmpty(it.data.response.property_details.frequency)) {
                        binding.content.tvFrequency.text =
                            "\t" + it.data.response.property_details.frequency
                    } else {
                        binding.content.lvFrequency.visibility = View.GONE
                    }

                    if (it.data.response.property_details.utility_status) {
                        binding.content.tvUtilityCharge.text =
                            "\t" + getString(R.string.sar) + "\t" + it.data.response.property_details.utility_amount + "\t" + getString(
                                R.string.included
                            )
                    } else {
                        binding.content.lvUtility.visibility = View.GONE
                    }

                    if (it.data.response.property_details.maintenance_status) {
                        binding.content.tvMaintenanceCharge.text =
                            "\t" + getString(R.string.sar) + "\t" + it.data.response.property_details.maintenance_amount + "\t" + getString(
                                R.string.included
                            )
                    } else {
                        binding.content.lvMaintenance.visibility = View.GONE
                    }

                    if (!TextUtils.isEmpty(it.data.response.property_details.token_amount)) {
                        binding.content.tvTokenAmt.text =
                            "\t" + getString(R.string.sar) + "\t" + it.data.response.property_details.token_amount
                    } else {
                        binding.content.lvToken.visibility = View.GONE
                    }

                    if (!TextUtils.isEmpty(it.data.response.property_details.security_deposit)) {
                        binding.content.tvSecurityAmt.text =
                            "\t" + getString(R.string.sar) + "\t" + it.data.response.property_details.security_deposit
                    } else {
                        binding.content.lvSecurity.visibility = View.GONE
                    }

                    when (it.data.response.property_details.utilization) {
                        0 -> {
                            binding.content.tvUtilization.text = "\t" + getString(R.string.Single)
                        }

                        1 -> {
                            binding.content.tvUtilization.text = "\t" + getString(R.string.Family)
                        }

                        else -> {
                            binding.content.lvUtilization.visibility = View.GONE
                        }
                    }

                    val detailsList = ArrayList<AgentPropertyDetailsList>()
                    it.data.response.property_details.property_details.forEach {
                        detailsList.add(it)
                    }
                    binding.content.rvDetails.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    binding.content.rvDetails.adapter = PropertyDetailsAdapter(detailsList)

                }
                Status.LOADING -> {
                    binding.shimmerLayout.startShimmer()
                }
                Status.NO_INTERNET -> {
                    binding.shimmerLayout.stopShimmer()
                    if (this.isConnected) {
                        showCookieBar(
                            this,
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    binding.shimmerLayout.stopShimmer()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    binding.shimmerLayout.stopShimmer()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        }
    }

    /* setup amenities category recyclerview */
    private fun setUpAmenitiesCategoryRecyclerView() {
        binding.content.rvDetailedAmenities.adapter =
            PropertyDetailedAmenitiesAdapter(this, amenitiesCategory)
    }

    /* property images viewpager */
    private fun setUpPropertyImages() {
        val vpPropertyImageSliderAdapter =
            PropertyImageSliderAdapter(supportFragmentManager, lifecycle, propertyImages)
        binding.vpPropertyDetailsImageSlider.adapter = vpPropertyImageSliderAdapter
        binding.dotsIndicator.setViewPager2(binding.vpPropertyDetailsImageSlider)
    }

    override fun onClicks() {

        /* share button */
        binding.content.shareBtn.setOnClickListener {
            setUpShareProperty()
        }

        /* payment split up layout */
        var clickedPaymentSplitUp = false
        binding.content.tvPaymentSplitUp.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_down,
            0
        )
        binding.content.paymentCard.setOnClickListener {
            if (!clickedPaymentSplitUp) {
                binding.content.paymentSplitUpTable.visibility = View.VISIBLE
                binding.content.paymentSplitUpTableValue.visibility = View.VISIBLE
                binding.nes.post { binding.nes.smoothScrollBy(0, binding.nes.bottom) }
                //ObjectAnimator.ofInt(binding.vb, "scrollY", binding.content.rvPaymentSplitUp.top).setDuration(1500).start()
                clickedPaymentSplitUp = true
                binding.content.tvPaymentSplitUp.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                )
            } else {
                binding.content.paymentSplitUpTable.visibility = View.GONE
                binding.content.paymentSplitUpTableValue.visibility = View.GONE
                clickedPaymentSplitUp = false
                binding.content.tvPaymentSplitUp.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                )

            }
        }

        /* tenant details layout */
        var clickTenantDetails = false
        binding.content.tvTenantDetails.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_down,
            0
        )
        binding.content.tenantCard.setOnClickListener {
            if (!clickTenantDetails) {
                binding.content.lvTenant.visibility = View.VISIBLE
                binding.content.lvNoTenant.visibility = View.VISIBLE
                binding.nes.post { binding.nes.smoothScrollBy(0, binding.nes.bottom) }
                clickTenantDetails = true
                binding.content.tvTenantDetails.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                )
            } else {
                binding.content.lvTenant.visibility = View.GONE
                binding.content.lvNoTenant.visibility = View.GONE
                binding.nes.post { binding.nes.smoothScrollBy(binding.nes.bottom, 0) }
                clickTenantDetails = false
                binding.content.tvTenantDetails.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                )
            }
        }

        /* payment history layout */
        var clickPaymentHistory = false
        binding.content.tvPaymentHistory.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_down,
            0
        )
        binding.content.paymentHistoryCard.setOnClickListener {
            if (!clickPaymentHistory) {
                binding.content.rvPaymentHistory.visibility = View.VISIBLE
                binding.content.paymentHistoryTable.visibility = View.VISIBLE
                binding.nes.post { binding.nes.smoothScrollBy(0, binding.nes.bottom) }
                clickPaymentHistory = true
                binding.content.tvPaymentHistory.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_up,
                    0
                )
            } else {
                binding.content.rvPaymentHistory.visibility = View.GONE
                binding.content.paymentHistoryTable.visibility = View.GONE
                binding.nes.post { binding.nes.smoothScrollBy(binding.nes.bottom, 0) }
                clickPaymentHistory = false
                binding.content.tvPaymentHistory.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_down,
                    0
                )
            }
        }

        binding.content.updateStatusCard.setOnClickListener {
            setUpUpdatePaymentDialog()
        }
    }

    /* update payment dialog */
    private fun setUpUpdatePaymentDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_update_payment)

        val remarkSpinner = dialog.findViewById(R.id.remarkSpinner) as AutoCompleteTextView
        val pDate = dialog.findViewById(R.id.tvDate) as TextView
        attachment = dialog.findViewById(R.id.tvAttachment) as TextView
        val rvAmount = dialog.findViewById(R.id.rvUpload) as RecyclerView
        val paidBtn = dialog.findViewById(R.id.paidBtn) as MaterialButton

        val remarkList = arrayOf("Token", "Advance", "Rent", "Full Payment")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, remarkList)
        remarkSpinner.setAdapter(adapter)

        pDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Select Card Expiry Date")
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                val tim =
                    "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                        calendar.get(Calendar.YEAR)
                    }"
                pDate.text = tim
            }
            datePicker.show(supportFragmentManager, "MyTAG")
        }

        attachment!!.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                    intent.action = Intent.ACTION_PICK
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 101)
                }
            }
        }

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvAmount.layoutManager = gridLayoutManager
        imageAdapter =
            ImageUploadAdapter(this, imagesList!!, { removeFromList(it) }, { viewImage(it) })
        rvAmount.adapter = imageAdapter

        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setUpShareProperty() {
        val stringBuilder = StringBuilder()
        stringBuilder.append(binding.content.tvPropertyType.text.toString() + " " + "in" + " " + binding.content.tvLocation.text.toString())
        stringBuilder.append("\nhttps://siaaha.com/property/$selectedPropertyId")
        shareProperty(stringBuilder.toString(), this, propertyShareImage)
    }

    private fun updateStatus(id: String) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_change_status)

        val remarkSpinner = dialog.findViewById(R.id.remarkSpinner) as AutoCompleteTextView
        val pDate = dialog.findViewById(R.id.tvDate) as TextView
        attachment = dialog.findViewById(R.id.tvAttachment) as TextView
        val rvAmount = dialog.findViewById(R.id.rvUpload) as RecyclerView
        val declineBtn = dialog.findViewById(R.id.declineBtn) as MaterialButton
        val paidBtn = dialog.findViewById(R.id.paidBtn) as MaterialButton

        val remarkList = arrayOf("Token", "Advance", "Rent", "Full Payment")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, remarkList)
        remarkSpinner.setAdapter(adapter)

        pDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Select Card Expiry Date")
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                val tim =
                    "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                        calendar.get(Calendar.YEAR)
                    }"
                pDate.text = tim
            }
            datePicker.show(supportFragmentManager, "MyTAG")
        }

        attachment!!.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                    intent.action = Intent.ACTION_PICK
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 101)
                }
            }
        }

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvAmount.layoutManager = gridLayoutManager
        imageAdapter =
            ImageUploadAdapter(this, imagesList!!, { removeFromList(it) }, { viewImage(it) })
        rvAmount.adapter = imageAdapter


        declineBtn.setOnClickListener {
            dialog.dismiss()
            setUpRejectDialog()
        }

        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    /* reject dialog */
    private fun setUpRejectDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_reject_enquiry)

        val remarkSpinner = dialog.findViewById(R.id.remarkSpinner) as AutoCompleteTextView
        val etRejectReason = dialog.findViewById(R.id.etRejectReason) as EditText
        val rejectBtn = dialog.findViewById(R.id.rejectBtn) as MaterialButton
        val cancelBtn = dialog.findViewById(R.id.cancelBtn) as MaterialButton


        val remarkList = arrayOf("Reason 1", "Reason 2", "Reason 3", "Reason 4")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, remarkList)
        remarkSpinner.setAdapter(adapter)

        var selectedReason = ""
        remarkSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedReason = ""
            selectedReason = remarkList[position]
            etRejectReason.text.clear()
        }


        etRejectReason.doOnTextChanged { text, start, before, count ->
            remarkSpinner.text.clear()
            selectedReason = ""
            selectedReason = etRejectReason.text.toString()
        }

        rejectBtn.setOnClickListener {
            if (selectedReason.isBlank()) {
                CommonUtils.showCookieBar(
                    this,
                    getString(R.string.error),
                    getString(R.string.please_specify_the_reason),
                    R.color.pomegranate
                )
            } else {
                Log.e("TAG", "setUpRejectDialog: $selectedReason")
                dialog.dismiss()
            }

        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()


        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                Log.e("resutl", data.toString())
                if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images!!.add(CommonUtils.getImageRealPath(imageUri, this))
                        imagesList!!.add(imageUri)
                        imageAdapter.notifyDataSetChanged()
                    }
                } else {
                    val imageUri = data.data
                    images!!.add(CommonUtils.getImageRealPath(imageUri, this))
                    imagesList!!.add(imageUri)
                    imageAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    /* remove image from list */
    private fun removeFromList(it: Int) {
        images!!.removeAt(it)
        imagesList!!.removeAt(it)
        imageAdapter.notifyDataSetChanged()
    }

    /* view image */
    private fun viewImage(it: String) {
        val builder = CustomTabsIntent.Builder()
        val colorInt: Int = Color.parseColor("#34626C")
        builder.setToolbarColor(colorInt)
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(it))
    }
}