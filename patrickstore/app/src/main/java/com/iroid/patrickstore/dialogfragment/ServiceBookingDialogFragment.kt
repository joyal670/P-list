package com.iroid.patrickstore.dialogfragment

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iroid.patrickstore.R
import com.iroid.patrickstore.databinding.DialogInfoBinding
import com.iroid.patrickstore.databinding.FragmentDialogServiceDateTimeBinding
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Toaster
import com.iroid.patrickstore.utils.getCurrentDateOtherFormat
import java.text.SimpleDateFormat
import java.util.*

class ServiceBookingDialogFragment : DialogFragment() {
    private var checkDate: Boolean = false
    private var checkTime: Boolean = false
    private var date: String = ""
    private lateinit var binding:FragmentDialogServiceDateTimeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogServiceDateTimeBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object{
        fun newInstance()= ServiceBookingDialogFragment().apply {
            arguments= bundleOf()

        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setFragmentResultListener(REQUEST_KEY_COUPON){
//                requestKey, bundle ->
//            val couponList:List<ItemCoupon> = bundle.getParcelableArrayList<ItemCoupon>(Constants.BUNDLE_KEY_COUPON)!!
//            val couponAdapter = CouponAdapter(requireContext(),couponList) {coupon_code->
//                setFragmentResult(REQUEST_KEY_CODE, bundleOf(Constants.BUNDLE_KEY_CODE to coupon_code) )
//                dismiss()
//
//            }
//
//
//        }
        binding.btnContinue.setOnClickListener {
            actionData()
        }
        binding.tvSelectDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val calendarConstraintBuilder = CalendarConstraints.Builder()
            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(calendarConstraintBuilder.build())
            val picker = builder.build()
            activity?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val formatter =
                        SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH) // modify format
                    date = formatter.format(Date(it))
                    binding.tvSelectDate.text = date
                    binding.tvSelectDate.setTextColor(Color.BLACK)
                    checkDate = true
                    binding.tvSelectTime.isEnabled = true
                    checkTime = false
                    binding.tvSelectTime.text = getString(R.string.spTimeSlotHint)
                    binding.tvSelectTime.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_gray2
                        )
                    )
                }

            }
        }
        binding.tvSelectTime.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText("Select Time")
                    .setHour(12)
                    .setMinute(0)
                    .build()
            activity?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
            picker.addOnPositiveButtonClickListener {
                // call back code
                var newHour: String = picker.hour.toString()
                var newMinute: String = picker.minute.toString()
                val temp = Calendar.getInstance()
                temp[Calendar.HOUR_OF_DAY] = newHour.toInt()
                temp[Calendar.MINUTE] = newMinute.toInt()
                if (date == requireContext().getCurrentDateOtherFormat()) {
                    if (temp.after(GregorianCalendar.getInstance())) {
                        if (newHour.length == 1) {
                            newHour = "0$newHour"
                        }
                        if (newMinute.length == 1) {
                            newMinute = "0$newMinute"
                        }
                        Log.e("time", "$newHour:$newMinute")
                        checkTime = true
                        binding.tvSelectTime.text = "$newHour:$newMinute"
                        binding.tvSelectTime.setTextColor(Color.BLACK)
                    } else {
                        Toaster.showToast(requireContext(), getString(R.string.cannot_select_past_time), Toaster.State.WARNING, Toast.LENGTH_SHORT)

                        checkTime = false
                        binding.tvSelectTime.text = getString(R.string.spTimeSlotHint)
                        binding.tvSelectTime.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_gray2
                            )
                        )
                    }
                } else {
                    if (newHour.length == 1) {
                        newHour = "0$newHour"
                    }
                    if (newMinute.length == 1) {
                        newMinute = "0$newMinute"
                    }
                    Log.e("time", "$newHour:$newMinute")
                    checkTime = true
                    binding.tvSelectTime.text = "$newHour:$newMinute"
                    binding.tvSelectTime.setTextColor(Color.BLACK)
                }
            }
        }

    }

    private fun actionData() {
        if (!checkDate) {
            Toaster.showToast(
                requireContext(), getString(R.string.date_required),
                Toaster.State.ERROR, Toast.LENGTH_LONG
            )
        } else if (!checkTime) {
            Toaster.showToast(
                requireContext(), getString(R.string.time_required),
                Toaster.State.ERROR, Toast.LENGTH_LONG
            )
        } else {
            dismiss()
            val bundleData=Bundle()
            bundleData.putString("date",date)
            bundleData.putString("time",  binding.tvSelectTime.text.toString())
            bundleData.putString("comments",binding.tvComments.text.toString())
            setFragmentResult(Constants.SERVICE_DATA, bundleOf(Constants.BUNDLE_KEY_CODE to bundleData) )
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window: Window = dialog.window!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }




}
