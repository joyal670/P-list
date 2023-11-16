package com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.furkanakdemir.surroundcardview.ReleaseListener
import com.furkanakdemir.surroundcardview.SurroundListener
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityEnquiryDetailsBinding
import com.ncomfortsagent.databinding.LayoutApproveBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.start_up.auth.activity.AuthActivity
import com.ncomfortsagent.ui.main.chat.ChatActivity
import com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.adapter.ImageUploadAdapter
import com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.viewmodel.EnquiryDetailsViewModel
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.activity.PropertyDetailsActivity
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters.PaymentHistoryAdapter
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters.PaymentSplitUpAdapter
import com.ncomfortsagent.utils.AppPreferences.prefEnquiryLoading
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class EnquiryDetailsActivity : BaseActivity<ActivityEnquiryDetailsBinding>() {
    private var selectedId = ""
    private var selectedType = ""
    private lateinit var enquiryDetailsViewModel: EnquiryDetailsViewModel
    private var propertyId = 0
    private var enquirystatus = ""
    private var selectedFile: File? = null

    private val PICK_IMAGES_CODE = 101

    private var radioInterested: RadioButton? = null
    var tokenInterested: EditText? = null
    var attachInterested: TextView? = null
    var radioCompleted: RadioButton? = null
    var payNowBtn: MaterialButton? = null

    private var userId = ""
    private var userName = ""
    private var userImage = ""

    var attachment: TextView? = null
    private lateinit var imageAdapter: ImageUploadAdapter
    private var images: ArrayList<String?>? = null
    private var imagesList: ArrayList<Uri?>? = null


    private var paymentSplitUpArrayList = ArrayList<String>()

    private lateinit var paymentSplitUpAdapter : PaymentSplitUpAdapter
    private lateinit var paymentHistoryAdapter: PaymentHistoryAdapter


    override val layoutId: Int
        get() = R.layout.activity_enquiry_details
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityEnquiryDetailsBinding =
        ActivityEnquiryDetailsBinding.inflate(layoutInflater)

    override fun initData() {
        imagesList = ArrayList()
        images = ArrayList()
        selectedId = intent.getStringExtra(Constants.ENQUIRY_ID)!!
        selectedType = intent.getStringExtra("enquiry_type")!!

        setSupportActionBar(binding.toolbarMain.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbarMain.tvToolbarTitle.text = getString(R.string.enquiries_details)

        enquiryDetailsViewModel.enquiryDetails(selectedId)


        paymentSplitUpArrayList.clear()
        paymentSplitUpAdapter = PaymentSplitUpAdapter(this,paymentSplitUpArrayList,{selectedPayment(it)})
        binding.rvPaymentSplitUp.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter =  paymentSplitUpAdapter
        }

        paymentHistoryAdapter = PaymentHistoryAdapter(this,{updateStatus(it)})
        binding.rvPaymentHistory.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter =  paymentHistoryAdapter
        }
    }

    private fun updateStatus(sId: String) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_change_status)

        val remarkSpinner = dialog.findViewById(R.id.remarkSpinner) as AutoCompleteTextView
        val pDate = dialog.findViewById(R.id.tvDate) as TextView
        attachment = dialog.findViewById(R.id.tvAttachmentUpdatePayment) as TextView
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
                if(result.allGranted()){
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
        imageAdapter = ImageUploadAdapter(this, imagesList!!, {removeFromList(it)}, {viewImage(it)})
        rvAmount.adapter = imageAdapter

        when (sId){
            "Decline/View" -> {
                paidBtn.text = getString(R.string.resubmit)
                declineBtn.text = getString(R.string.cancel)
            }

            "Paid/View" -> {
                declineBtn.visibility = View.GONE
                paidBtn.visibility = View.GONE
            }
        }


        declineBtn.setOnClickListener {
            when (declineBtn.text.trim().toString()){
                getString(R.string.decline) -> {
                    dialog.dismiss()
                    setUpRejectDialog()
                }

                getString(R.string.cancel) -> {
                    dialog.dismiss()
                }
            }

        }

        paidBtn.setOnClickListener {
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

    private fun selectedPayment(it: Int) {

    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {

        /* reset the card view stroke color */
        binding.cardView.setSurrounded(false)
        binding.cardView.setDuration(1000)
        binding.cardView.setSurroundStrokeColor(R.color.white)

        when (selectedType) {
            "0" -> {
                binding.contentPending.visibility = View.VISIBLE

                binding.lvPaymentSplitUp.visibility = View.GONE
                binding.lvPaymentHistory.visibility = View.GONE
                binding.lvPaymentStatus.visibility = View.GONE
                binding.lvViewContract.visibility = View.GONE

                binding.toolbarMain.tvToolbarTitle.text = getString(R.string.enquiry_pending)

            }
            "1" -> {
                binding.lvPaymentSplitUp.visibility = View.VISIBLE
                binding.lvPaymentStatus.visibility = View.VISIBLE

                binding.contentPending.visibility = View.GONE
                binding.lvPaymentHistory.visibility = View.GONE
                binding.lvViewContract.visibility = View.GONE

                binding.toolbarMain.tvToolbarTitle.text = getString(R.string.enquiry_approved)

            }

            "2" -> {
                binding.lvPaymentSplitUp.visibility = View.VISIBLE
                binding.lvPaymentStatus.visibility = View.VISIBLE
                binding.lvViewContract.visibility = View.VISIBLE

                binding.contentPending.visibility = View.GONE
                binding.lvPaymentHistory.visibility = View.GONE

                binding.toolbarMain.tvToolbarTitle.text = getString(R.string.Enquiry_interested)
            }

            "3" -> {
                binding.lvPaymentSplitUp.visibility = View.VISIBLE
                binding.lvPaymentStatus.visibility = View.VISIBLE
                binding.lvViewContract.visibility = View.VISIBLE

                binding.contentPending.visibility = View.GONE
                binding.lvPaymentHistory.visibility = View.GONE

                binding.toolbarMain.tvToolbarTitle.text = getString(R.string.enquiry_completed)
            }
        }
    }

    override fun setupViewModel() {
        enquiryDetailsViewModel = EnquiryDetailsViewModel(this)
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        enquiryDetailsViewModel.getAgentEnquiryDetailsResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {

                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.lv.visibility = View.VISIBLE

                    binding.tvEnquiryDate.text = it.data!!.response.data.date.take(10)

                    Glide.with(this).load(it.data.response.data.property_priority_image.document)
                        .placeholder(R.drawable.no_image).into(binding.imageView3)

                    /* property_to -> 0, take rent amount as price */
                    /* property_to -> 1, take mrp amount as price */
                    if (it.data.response.data.property_details.property_to == 0) {
                        binding.tvEnquiryPrice.text =
                            getString(R.string.sar) + it.data.response.data.property_details.rent
                        binding.tvEnquiryType.text = getString(R.string.rent)
                    } else {
                        binding.tvEnquiryPrice.text =
                            getString(R.string.sar) + it.data.response.data.property_details.mrp
                        binding.tvEnquiryType.text = getString(R.string.sale)
                    }

                    binding.tvEnquiryDetails.text = it.data.response.data.feature_details

                    propertyId = it.data.response.data.property_details.id

                    Glide.with(this).load(it.data.response.data.user_rel.profile_pic)
                        .placeholder(R.drawable.no_image).into(binding.ivUser)
                    binding.tvUserName.text = " " + it.data.response.data.user_rel.name
                    binding.tvUserEmail.text = " " + it.data.response.data.user_rel.email
                    binding.tvUserPhoneNo.text = " " + it.data.response.data.user_rel.phone
                    binding.tvUserLocation.text = " " + it.data.response.data.user_rel.location

                    userId = it.data.response.data.user_rel.id.toString()
                    userName = it.data.response.data.user_rel.name
                    userImage = it.data.response.data.user_rel.profile_pic

                    when (it.data.response.data.enquiry_status) {
                        "0" -> {
                            binding.textView2.text = " " + getString(R.string.pending)
                        }
                        "1" -> {
                            binding.textView2.text = " " + getString(R.string.approved)
                        }
                        "2" -> {
                            binding.textView2.text = " " + getString(R.string.interested)
                        }
                        "3" -> {
                            binding.textView2.text = " " + getString(R.string.completed)
                        }
                    }

                    enquirystatus = it.data.response.data.enquiry_status
                    selectedType = it.data.response.data.enquiry_status
                    //binding.container.visibility = View.VISIBLE

                }
                Status.LOADING -> {
                    binding.shimmerLayout.startShimmer()
                }
                Status.NO_INTERNET -> {
                    binding.shimmerLayout.stopShimmer()
                    if (this.isConnected) {
                        CommonUtils.showCookieBar(
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
                Status.DATA_EMPTY -> {
                    binding.shimmerLayout.stopShimmer()
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
                Status.ERROR -> {
                    binding.shimmerLayout.stopShimmer()
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
            }
        }

        enquiryDetailsViewModel.getAgentEnquiryChangeStatusResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.success),
                        it.data!!.response,
                        R.color.de_york
                    )

                    initData()

                    prefEnquiryLoading = true
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        CommonUtils.showCookieBar(
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
                    dismissProgress()
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        }
    }

    override fun onClicks() {

        binding.cardView.setOnLongClickListener {
            binding.cardView.setSurroundStrokeColor(R.color.primary_color)
            binding.cardView.switch()
            true
        }

        binding.cardView.setOnClickListener {
            binding.cardView.setSurroundStrokeColor(R.color.primary_color)
            binding.cardView.switch()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    val intent = Intent(this@EnquiryDetailsActivity, PropertyDetailsActivity::class.java)
                    intent.putExtra(Constants.PROPERTY_ID, propertyId)
                    startActivity(intent)
                }
            } , 1000)
        }


        binding.ivTwoWayChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("user_name", userName)
            intent.putExtra("user_image", userImage)
            startActivity(intent)
        }


        /* reject btn */
        binding.rejectBtn.setOnClickListener {
            setUpRejectDialog()
        }

        /* approve btn */
        binding.approveBtn.setOnClickListener {
            setUpApproveDialog()
        }

        /* payment split up layout */
        var clickedPaymentSplitUp = false
        binding.tvPaymentSplitUp.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_down, 0)
        binding.paymentCard.setOnClickListener {
            if(!clickedPaymentSplitUp){
                binding.rvPaymentSplitUp.visibility = View.VISIBLE
                binding.paymentSplitUpTable.visibility = View.VISIBLE
                binding.nes.post { binding.nes.smoothScrollBy(0, binding.nes.bottom) }
                clickedPaymentSplitUp = true
                binding.tvPaymentSplitUp.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_up, 0)
            }else{
                binding.rvPaymentSplitUp.visibility = View.GONE
                binding.paymentSplitUpTable.visibility = View.GONE
                clickedPaymentSplitUp = false
                binding.tvPaymentSplitUp.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_arrow_down, 0)

            }
        }

        /* payment history layout */
        var clickPaymentHistory = false
        binding.tvPaymentHistory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
        binding.paymentHistoryCard.setOnClickListener {
            if(!clickPaymentHistory){
                binding.rvPaymentHistory.visibility = View.VISIBLE
                binding.paymentHistoryTable.visibility = View.VISIBLE
                binding.nes.post { binding.nes.smoothScrollBy(0, binding.nes.bottom) }
                clickPaymentHistory = true
                binding.tvPaymentHistory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }else{
                binding.rvPaymentHistory.visibility = View.GONE
                binding.paymentHistoryTable.visibility = View.GONE
                binding.nes.post { binding.nes.smoothScrollBy( binding.nes.bottom,0) }
                clickPaymentHistory = false
                binding.tvPaymentHistory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            }
        }

        binding.updateStatusCard.setOnClickListener {
            setUpUpdatePaymentDialog()
        }

        /* view contract layout */
        var clickViewContract = false
        binding.tvViewContract.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
        binding.viewContractCard.setOnClickListener {
            if(!clickViewContract){
                binding.lvContract.visibility = View.VISIBLE
                binding.nes.post { binding.nes.smoothScrollBy(0, binding.nes.bottom) }
                clickViewContract = true
                binding.tvViewContract.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }else{
                binding.lvContract.visibility = View.GONE
                binding.nes.post { binding.nes.smoothScrollBy( binding.nes.bottom,0) }
                clickViewContract = false
                binding.tvViewContract.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            }
        }
    }

    /* approve dialog */
    private fun setUpApproveDialog() {

        val bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        val bindingBottomSheet : LayoutApproveBinding = LayoutApproveBinding.inflate(layoutInflater)

        bindingBottomSheet.materialButton1.setOnClickListener {
            bottom.dismiss()
        }

        bottom.setContentView(bindingBottomSheet.root)
        bottom.show()
    }

    private fun setUpViewPaymentDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_view_payment)

        val remarkSpinner = dialog.findViewById(R.id.remarkSpinner) as AutoCompleteTextView
        val pDate = dialog.findViewById(R.id.tvDate) as TextView
        attachment = dialog.findViewById(R.id.tvAttachment) as TextView
        val rvAmount = dialog.findViewById(R.id.rvUpload) as RecyclerView
        val declineBtn = dialog.findViewById(R.id.declineBtn) as MaterialButton

        val remarkList = arrayOf("Token", "Advance", "Rent", "Full Payment")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, remarkList)
        remarkSpinner.setAdapter(adapter)

        pDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Select Date")
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
                if(result.allGranted()){
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
        imageAdapter = ImageUploadAdapter(this, imagesList!!, {removeFromList(it)}, {viewImage(it)})
        rvAmount.adapter = imageAdapter

        declineBtn.setOnClickListener {
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
            builder.setTitleText("Select Date")
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

                   /* val intentq = Intent()
                    intent.putExtra("keyName", stringToPassBack)
                    setResult(RESULT_OK, intentq)
                    finish()*/
                }
            }
        }

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvAmount.layoutManager = gridLayoutManager
        imageAdapter =
            ImageUploadAdapter(this, imagesList!!, { removeFromList(it) }, { viewImage(it) })
        rvAmount.adapter = imageAdapter

        paidBtn.setOnClickListener {
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

    /* reject enquiry dialog */
    private fun setUpRejectDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_reject_enquiry)

        val remarkSpinner = dialog.findViewById(R.id.remarkSpinner) as AutoCompleteTextView
        val etRejectReason = dialog.findViewById(R.id.etRejectReason) as EditText
        val rejectBtn = dialog.findViewById(R.id.rejectBtn) as MaterialButton
        val cancelBtn = dialog.findViewById(R.id.cancelBtn) as MaterialButton
        val textInputRemark = dialog.findViewById(R.id.textInputRemark) as TextInputLayout
        val pName = dialog.findViewById(R.id.pName) as TextInputLayout


        val remarkList = arrayOf("Reason 1", "Reason 2", "Reason 3", "Reason 4")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, remarkList)
        remarkSpinner.setAdapter(adapter)

        var selectedReason = ""
        remarkSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedReason = ""
            selectedReason = remarkList[position]
            etRejectReason.text.clear()
            textInputRemark.isErrorEnabled = false
            pName.isErrorEnabled = false
        }


        etRejectReason.doOnTextChanged { text, start, before, count ->
            remarkSpinner.text.clear()
            selectedReason = ""
            selectedReason = etRejectReason.text.toString()
            textInputRemark.isErrorEnabled = false
            pName.isErrorEnabled = false
        }

        rejectBtn.setOnClickListener {
            if (selectedReason.isBlank()) {
                textInputRemark.error = getString(R.string.required)
                pName.error = getString(R.string.required)
                textInputRemark.isErrorEnabled = true
                pName.isErrorEnabled = true
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
            if (requestCode == PICK_IMAGES_CODE) {
                selectedFile = null
                selectedFile = ImagePicker.getFile(data)!!
                attachInterested!!.text = getString(R.string.uploaded)
                /*if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        Log.i(TAG, "onActivityResult: $imageUri")
                        try {
                            images!!.add(CommonUtils.getImageRealPath(imageUri, this))
                            attachInterested!!.text = getString(R.string.uploaded)
                        }catch (ex:NullPointerException){
                            ex.printStackTrace()
                        }
                    }
                } else {
                    val imageUri = data.data
                    images!!.add(CommonUtils.getImageRealPath(imageUri, this))

                }*/
            }
        }
    }
  /*  var launchSomeActivity = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // your operation...
        }
    }*/

   /* fun openYourActivity() {
        val intent = Intent(this, SomeActivity::class.java)
        launchSomeActivity.launch(intent)
    }*/


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