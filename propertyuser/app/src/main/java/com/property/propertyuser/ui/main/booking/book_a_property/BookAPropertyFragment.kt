package com.property.propertyuser.ui.main.booking.book_a_property

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentBookAPropertyBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.booking.BookingActivity
import com.property.propertyuser.ui.main.payment.PaymentActivity
import com.property.propertyuser.ui.main.property_details.packages2.PackagesActivity
import com.property.propertyuser.ui.main.side_menu.rewards.RewardsFragment
import com.property.propertyuser.utils.*
import it.sephiroth.android.library.xtooltip.Tooltip
import kotlinx.android.synthetic.main.fragment_book_a_property.*
import kotlinx.android.synthetic.main.layout_no_network.*
import java.text.SimpleDateFormat
import java.util.*

class BookAPropertyFragment : BaseFragment() {
    private lateinit var bookAPropertyViewModel: BookAPropertyViewModel
    private var checkIn = false
    private var checkOut = false
    private lateinit var checkInDate: Date
    private lateinit var checkOutDate: Date
    private var propertyIdPassed = ""
    private var packageAmountSelected = ""
    private var selectedPackageAmount = ""
    private var selectedPackageID = ""
    private var selectedPackageName = ""
    var couponLiveData: MutableLiveData<String>? = null


    init {
        couponLiveData = MutableLiveData()
    }

    companion object {
        const val ARG_PROPERTY_ID = "property_id"
        const val ARG_PACKAGE_AMOUNT = "package_amount"

        fun newInstance(property_id: String, package_amount: String): BookAPropertyFragment {
            val fragment = BookAPropertyFragment()
            val bundle = Bundle().apply {
                putString(ARG_PROPERTY_ID, property_id)
                putString(ARG_PACKAGE_AMOUNT, package_amount)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentBookAPropertyBinding
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookAPropertyBinding.inflate(inflater!!, container, false)
        val view = binding.root
        propertyIdPassed = arguments?.getString(ARG_PROPERTY_ID).toString()
        arguments?.getString(ARG_PACKAGE_AMOUNT)?.let {
            packageAmountSelected = arguments?.getString(ARG_PACKAGE_AMOUNT).toString()
        }
        return view
        /* return inflater?.inflate(R.layout.fragment_book_a_property,container,false)*/
    }

    override fun initData() {
        bookAPropertyViewModel.bookPropertyDetail(propertyIdPassed)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as BookingActivity

    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.book_a_property_title))
    }

    override fun setupViewModel() {
        bookAPropertyViewModel = BookAPropertyViewModel()
    }

    override fun setupObserver() {
        bookAPropertyViewModel.getBookPropertyDetailLiveData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data?.property_details != null) {
                        includeNoInternetBookProperty.visibility = View.GONE
                        linearNoDataFoundBookProperty.visibility = View.GONE
                        mainConstraintBookProperty.visibility = View.VISIBLE
                        binding.tvPropertyName.text = it.data.property_details.property_name
                        binding.tvPropertyCode.text =
                            getString(R.string.tvPropertyCode) + " " + it.data.property_details.property_reg_no
                        if (it.data.property_details.property_to == 0) {
                            binding.tvPropertyAmount.text =
                                getString(R.string.sar) + " " + it.data.property_details.rent
                        } else {
                            binding.tvPropertyAmount.text =
                                getString(R.string.sar) + " " + it.data.property_details.selling_price
                        }
                        binding.tvPropertyLocation.text = it.data.property_details.location
                        binding.tvTokenAmount.text =
                            getString(R.string.sar) + " " + it.data.property_details.token_amount
                        if (it.data.property_details.property_priority_image != null) {
                            binding.roundedPropertyImage.loadImagesWithGlideExt(it.data.property_details.property_priority_image.document)
                        }
                        if (!(packageAmountSelected.isNullOrEmpty())) {
                            binding.tvPropertyAmount.text =
                                getString(R.string.sar) + " " + packageAmountSelected
                        }

                        binding.tvSubTotal.text =
                            getString(R.string.sar) + " " + it.data.property_details.token_amount.toString()

                        binding.tvPackage.text = it.data.property_details.frequency.toString()
                    } else {
                        linearNoDataFoundBookProperty.visibility = View.VISIBLE
                        mainConstraintBookProperty.visibility = View.GONE
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        it.status.toString(),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternetBookProperty.visibility = View.VISIBLE
                        linearNoDataFoundBookProperty.visibility = View.GONE
                        mainConstraintBookProperty.visibility = View.GONE
                    }
                }

            }
        })
        bookAPropertyViewModel.getBookPropertyLiveData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.response,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                    var intent = Intent(requireContext(), PaymentActivity::class.java)
                    intent.putExtra("passed_type", "book_a_property")
                    intent.putExtra("passed_booking_id_or_property_id", it.data.booking_id)
                    intent.putExtra("passedPropertyIdRating", propertyIdPassed)
                    startActivity(intent)
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            requireContext(), getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })

        couponLiveData?.observe(this) { value ->
            clearText.isVisible = !value.isNullOrEmpty()

        }
    }

    override fun onClicks() {
        btnBookNow.setOnClickListener {
            if (!checkIn) {
                Toaster.showToast(
                    requireContext(), getString(R.string.tvCheckIn),
                    Toaster.State.WARNING, Toast.LENGTH_LONG
                )
            } else if (!checkOut) {
                Toaster.showToast(
                    requireContext(),
                    getString(R.string.tvCheckOut),
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else if (!((checkInDate < checkOutDate) && (checkOutDate > checkInDate))) {
                Toaster.showToast(
                    requireContext(), getString(R.string.checkindate_lessthan_checkout),
                    Toaster.State.WARNING, Toast.LENGTH_LONG
                )
            } else {
                bookAPropertyViewModel.bookProperty(
                    propertyIdPassed,
                    tvSelectCheckInDate.text.trim().toString(),
                    tvSelectCheckOutDate.text.trim().toString(), etCouponCode.text.trim().toString()
                )
            }
        }
        tvSelectCheckInDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val picker = builder.build()
            activity?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val formatter =
                        SimpleDateFormat("dd-MM-yyyy") // modify format
                    val date = formatter.format(Date(it))
                    tvSelectCheckInDate.text = date.toString()
                    tvSelectCheckInDate.setTextColor(Color.BLACK)
                    checkIn = true
                    checkInDate = Date(it)
                }

            }
        }
        tvSelectCheckOutDate.setOnClickListener {
            //val dateValidator: DateValidator = DateValidatorPointForward.from(checkInDate.time)
            val builder = MaterialDatePicker.Builder.datePicker()
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val picker = builder.build()
            activity?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val formatter =
                        SimpleDateFormat("dd-MM-yyyy") // modify format
                    val date = formatter.format(Date(it))
                    tvSelectCheckOutDate.text = date.toString()
                    tvSelectCheckOutDate.setTextColor(Color.BLACK)
                    checkOut = true
                    checkOutDate = Date(it)
                }

            }

        }
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternetBookProperty.visibility = View.GONE
                bookAPropertyViewModel.bookPropertyDetail(propertyIdPassed)
            }
        }

        tvViewCoupons.setOnClickListener {
            /* pass value 2 for attach rewards fragment in booking activity */
            appCompatActivity!!.replaceFragment(
                fragment = RewardsFragment.newInstance("2"),
                addToBackStack = true
            )
        }

        btnOtherPackage.setOnClickListener {
            val intent = Intent(requireContext(), PackagesActivity::class.java)
            intent.putExtra("property_id", propertyIdPassed)
            startActivityForResult(intent, 11)

        }

        etCouponCode.doOnTextChanged { text, start, before, count ->
            couponLiveData?.value = text.toString()
        }

        clearText.setOnClickListener {
            etCouponCode.setText("")
        }

        btnApply.setOnClickListener {
            val text = etCouponCode.text.trim().toString()
            if (TextUtils.isEmpty(text)) {
                Log.e("TAG", "onClicks: empty")
                val tooltip = Tooltip.Builder(requireContext())
                    .anchor(frameMain, 0, 0, true)
                    .text(getString(R.string.CouponCodeRequired))
                    .styleId(R.style.ToolTipAltStyle)
                    .arrow(true)
                    .floatingAnimation(Tooltip.Animation.DEFAULT)
                    .showDuration(1500)
                    .overlay(true)
                    .create()

                tooltip
                    .doOnHidden { }
                    .doOnFailure { }
                    .doOnShown { }
                    .show(frameMain, Tooltip.Gravity.TOP, true)
            } else {
                Log.e("TAG", "onClicks: found")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                selectedPackageAmount = data!!.getStringExtra("discountAmount").toString()
                selectedPackageID = data.getStringExtra("selectedId").toString()
                selectedPackageName = data.getStringExtra("selectedPackageName").toString()
                Log.e("result2", selectedPackageAmount + selectedPackageID + selectedPackageName)

                tvPackage.text = selectedPackageName
            }
        }
    }
}