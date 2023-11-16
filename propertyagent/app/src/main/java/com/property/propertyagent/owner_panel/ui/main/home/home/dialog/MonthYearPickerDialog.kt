package com.property.propertyagent.owner_panel.ui.main.home.home.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.property.propertyagent.databinding.DialogMonthYearPickerBinding
import java.util.*

class MonthYearPickerDialog() : DialogFragment() {
    val date: Date = Date()
    companion object {
        private const val MAX_YEAR = 2099
    }

    private lateinit var binding: DialogMonthYearPickerBinding

    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogMonthYearPickerBinding.inflate(requireActivity().layoutInflater)
        val cal: Calendar = Calendar.getInstance().apply { time = date }

        binding.pickerYear.run {
            val year = cal.get(Calendar.YEAR)
            minValue = 2000
            maxValue = MAX_YEAR
            value = year
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Please Select Year")
            .setView(binding.root)
            .setPositiveButton("Ok") { _, _ -> listener?.onDateSet(null, binding.pickerYear.value, binding.pickerMonth.value, 1) }
            .setNegativeButton("Cancel") { _, _ -> dialog?.cancel() }
            .create()
    }
}