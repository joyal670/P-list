package com.property.propertyuser.dialogs.book_a_tour

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.property.propertyuser.R
import com.property.propertyuser.dialogs.CustomProgressDialog
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.booking.BookingActivity
import com.property.propertyuser.utils.*
import com.skydoves.powerspinner.SpinnerGravity
import kotlinx.android.synthetic.main.fragment_dialog_book_tour_date_time.*
import java.text.SimpleDateFormat
import java.util.*

class BookATourDateAndTimeDialogFragment(private var requireActivity: Activity) : DialogFragment() {

    private lateinit var bookATourViewModel: BookATourViewModel
    protected lateinit var customProgressDialog: CustomProgressDialog
    private var checkDate: Boolean = false
    private var checkTime: Boolean = false
    private var checkAgent: Boolean = false
    private var date: String = ""

    companion object {
        private var passedPropertyId: String = ""
        fun newInstance(property_id: String): BookATourDateAndTimeDialogFragment {
            val f = BookATourDateAndTimeDialogFragment(Activity())
            val args = Bundle()
            args.putString("property_id", property_id)
            f.arguments = args
            passedPropertyId = args.getString("property_id").toString()
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookATourViewModel = BookATourViewModel()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return inflater.inflate(R.layout.fragment_dialog_book_tour_date_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customProgressDialog = CustomProgressDialog()
        //addTimeSlotDropDown()
        if (AppPreferences.chooseLanguage == "arabic") {
            spTimeSlot.arrowGravity = SpinnerGravity.START
        } else {
            spTimeSlot.arrowGravity = SpinnerGravity.END
        }

        ivClose.setOnClickListener {
            dialog?.dismiss()
        }

        var selectedAgentId = 0
        val agentList = arrayOf("Agent 1", "Agent 2", "Agent 3")
        val adapter = ArrayAdapter(requireActivity, R.layout.spinner_item, agentList)
        selectAgentSpinner.setAdapter(adapter)

        selectAgentSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedAgentId = 0
            checkAgent = true
            val agentName = selectAgentSpinner.text.trim().toString()
            agentList.forEach {
                /* compare name */
                /* if (it.type.lowercase() == tempType.lowercase()) {
                     selectedTypesId = it.id
                     selectedTypeName = it.type

                 }*/
            }
        }


        tvSelectDate.setOnClickListener {
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
                    tvSelectDate.text = date
                    tvSelectDate.setTextColor(Color.BLACK)
                    checkDate = true
                    tvSelectTime.isEnabled = true
                    checkTime = false
                    tvSelectTime.text = getString(R.string.spTimeSlotHint)
                    tvSelectTime.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_gray2
                        )
                    )
                }

            }
        }
        tvSelectTime.setOnClickListener {
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
                        tvSelectTime.text = "$newHour:$newMinute"
                        tvSelectTime.setTextColor(Color.BLACK)
                    } else {
                        Toaster.showToast(requireContext(), getString(R.string.cannot_select_past_time), Toaster.State.WARNING, Toast.LENGTH_SHORT)

                        checkTime = false
                        tvSelectTime.text = getString(R.string.spTimeSlotHint)
                        tvSelectTime.setTextColor(
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
                    tvSelectTime.text = "$newHour:$newMinute"
                    tvSelectTime.setTextColor(Color.BLACK)
                }
            }
        }
        btnContinue.setOnClickListener {
            Log.e("property_id", passedPropertyId)
            Log.e("date", tvSelectDate.text.trim().toString())
            Log.e("time range", spTimeSlot.text.trim().toString())
            Log.e("agent Name", selectAgentSpinner.text.trim().toString())
            /*if (!checkAgent) {
                Toaster.showToast(
                    requireContext(), getString(R.string.select_agent),
                    Toaster.State.ERROR, Toast.LENGTH_LONG
                )
            } else*/
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
                bookATourViewModel.bookATourAddDateTime(
                    passedPropertyId, tvSelectDate.text.trim().toString(),
                    tvSelectTime.text.trim().toString()
                )
            }

        }
        bookATourViewModel.getBookedTourDataResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> customProgressDialog.show(requireContext())
                Status.SUCCESS -> {
                    customProgressDialog.dialog.dismiss()
                    val intent = Intent(context, BookingActivity::class.java)
                    intent.putExtra(Constants.TYPE_BOOKING, TYPE_BOOKING.TOUR.name)
                    intent.putExtra("property_id", it.data?.response_data?.property_id)
                    intent.putExtra("tour_id", it.data?.response_data?.tour_id.toString())
                    startActivity(intent)
                }
                Status.DATA_EMPTY -> {
                    customProgressDialog.dialog.dismiss()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    customProgressDialog.dialog.dismiss()
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
    }

    private fun addTimeSlotDropDown() {
        val timeSlots = listOf<String>(
            "11:00 AM - 12:00 PM",
            "12:00 PM- 01:00 PM",
            "01:00 PM - 02:00 PM",
            "02:00 PM - 3:00 PM",
            "3:00 PM - 04:00 PM",
            "04:00 PM- 05:00 PM",
            "05:00 PM - 06:00 PM",
            "06:00 PM - 7:00 PM"
        )

        val listPopupWindow = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindow.anchorView = tvSelectTime
        val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, timeSlots)
        listPopupWindow.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            tvSelectTime.setTextColor(Color.BLACK)
            tvSelectTime.text = timeSlots[position]
            checkTime = true
            // Dismiss popup.
            listPopupWindow.dismiss()
        }
        //tvSelectTime.setOnClickListener { v: View? -> listPopupWindow.show() }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}