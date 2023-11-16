package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.my_feedback

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.google.android.material.datepicker.MaterialDatePicker
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentAddFeedbackBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentFeedbackViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ParentAddFeedbackFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentParentAddFeedbackBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentFeedbackViewModel: ParentFeedbackViewModel

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentAddFeedbackFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentAddFeedbackBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        /* set title */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.add_feedback))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {
    }

    override fun onClicks() {

        binding.textInputLayout.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
            val datePicker = builder.build()
            datePicker.isCancelable = false
            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                val tim =
                    "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                        calendar.get(Calendar.YEAR)
                    }"

                val inputFormat: DateFormat = SimpleDateFormat("d-MM-yyyy", Locale.getDefault())
                val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val startDate: Date? = inputFormat.parse(tim)
                val outputStartDate: String? = outputFormat.format(startDate)
                binding.textInputLayout.setText(outputStartDate)

            }
            datePicker.show(parentFragmentManager, "MyTAG")
        }

        binding.submitBtn.setOnClickListener {

            val tempDate = binding.textInputLayout.text.toString()
            val tempNote = binding.editText.text.toString()

            if (tempDate.isBlank() || tempNote.isBlank()) {
                if (tempDate.isBlank()) {
                    binding.textInputLayout.error = "Required"
                }
                if (tempNote.isBlank()) {
                    binding.editText.error = "Required"
                }
            } else {
                setupFeedbackSaveObserver(tempDate, tempNote)
            }
        }
    }

    /* save feedback */
    private fun setupFeedbackSaveObserver(tempDate: String, tempNote: String) {

        parentFeedbackViewModel = ParentFeedbackViewModel()
        parentFeedbackViewModel.parentFeedbackSave(tempDate, tempNote)
        parentFeedbackViewModel.parentFeedbackSaveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    Toaster.showToast(
                        requireContext(),
                        it.data!!.message,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                    binding.textInputLayout.setText(" ")
                    binding.editText.setText(" ")

                    appCompatActivity?.replaceFragment(
                        fragment = ParentFeedBackFragment(),
                        addToBackStack = false
                    )
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        /* back press */
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            appCompatActivity?.replaceFragment(
                fragment = ParentFeedBackFragment(),
                addToBackStack = false
            )
        }
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }

    fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.textInputLayout.setText("$year-${month + 1}-$dayOfMonth")
    }


}