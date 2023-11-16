package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.leave_application.leave_apply

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentAssignedLeaveApplyBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.edit_leave.StudentEditLeaveCategory
import com.iroid.jeetmeet.modal.student.leave_apply.StudentLeaveApplyData
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentLeaveApplyViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.opencsv.CSVWriter
import com.skydoves.powerspinner.PowerSpinnerView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StudentLeaveApplyFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentStudentAssignedLeaveApplyBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentLeaveApplyAdapter: StudentLeaveApplyAdapter

    private lateinit var studentLeaveApplyViewModel: StudentLeaveApplyViewModel
    private var leaveList = ArrayList<StudentLeaveApplyData>()
    private var categoryList = ArrayList<String>()
    private var categoryIDList = ArrayList<Int>()
    private var selectedId = 0
    private var selectedName: String = ""

    var categorySpinner: PowerSpinnerView? = null
    var ivClose: ImageView? = null
    var tvStartDate: EditText? = null
    var tvStartTime: EditText? = null
    var tvEndDate: EditText? = null
    var tvEndTime: EditText? = null
    var tvReason: EditText? = null
    var tvAttachment: EditText? = null
    var submitBtn: MaterialButton? = null
    var ImageChip: Chip? = null
    private var attachmentFile: File? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentAssignedLeaveApplyBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* *//* setup toolbar  *//*
        fragmentTransInterface = activity as StudentLeaveActivity
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)*/
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvStudentLeaveApply.layoutManager = LinearLayoutManager(context)

        setupSortObserver(12)
    }

    private fun setupSortObserver(sort: Int) {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call  */
        studentLeaveApplyViewModel = StudentLeaveApplyViewModel(requireContext())
        studentLeaveApplyViewModel.studentLeaveApply(sort)
        studentLeaveApplyViewModel.studentLeaveApplyData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    leaveList.clear()
                    leaveList.addAll(it.data!!.data)
                    studentLeaveApplyAdapter =
                        StudentLeaveApplyAdapter(leaveList, { editLeave(it) }, { deleteLeave(it) })
                    binding.rvStudentLeaveApply.adapter = studentLeaveApplyAdapter

                    if (leaveList.size == 0) {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.LOADING -> {
                    showProgress()
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
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

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    /* delete leave */
    private fun deleteLeave(id: Int) {

        studentLeaveApplyViewModel = StudentLeaveApplyViewModel(requireContext())
        studentLeaveApplyViewModel.studentDeleteLeave(id)
        studentLeaveApplyViewModel.studentDeleteLeaveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.message,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                    setupSortObserver(0)
                }
                Status.LOADING -> {
                    showProgress()
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
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

    /* edit leave */
    private fun editLeave(pos: Int) {
        /* api call */
        studentLeaveApplyViewModel = StudentLeaveApplyViewModel(requireContext())
        studentLeaveApplyViewModel.studentEditLeave(pos)
        studentLeaveApplyViewModel.studentEditLeaveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val leave_id = it.data!!.leave_applied.id
                    val leave_category = it.data.leave_applied.leave_category
                    val from_date = it.data.leave_applied.from_date
                    val to_date = it.data.leave_applied.to_date
                    val from_time = it.data.leave_applied.from_time
                    val to_time = it.data.leave_applied.to_time
                    val reason = it.data.leave_applied.reason
                    val attachment = it.data.leave_applied.attachment
                    val attachment_url = it.data.leave_applied.attachment_url
                    val leave_categoryname = it.data.leave_applied.leave_categoryname.name
                    val leave_categoryid = it.data.leave_applied.leave_categoryname.id

                    val category_list_name = ArrayList<String>()
                    val category_list_id = ArrayList<Int>()
                    category_list_name.clear()
                    category_list_id.clear()

                    it.data.category_list.forEach {
                        category_list_name.addAll(listOf(it.name))
                        category_list_id.addAll(listOf(it.id))
                    }

                    val cList = ArrayList<StudentEditLeaveCategory>()
                    cList.addAll(it.data.category_list)

                    setUpEditLeaveDialog(
                        leave_id,
                        leave_category,
                        from_date,
                        to_date,
                        from_time,
                        to_time,
                        reason,
                        attachment,
                        attachment_url,
                        leave_categoryname,
                        leave_categoryid,
                        category_list_name,
                        category_list_id,
                        cList
                    )

                }
                Status.LOADING -> {
                    showProgress()
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
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

    /* edit leave dialog */
    private fun setUpEditLeaveDialog(
        leaveId: Int,
        leaveCategory: Int,
        fromDate: String,
        toDate: String,
        fromTime: String,
        toTime: String,
        reason: String,
        attachment: String,
        attachmentUrl: String,
        leaveCategoryname: String,
        leaveCategoryid: Int,
        categoryListName: ArrayList<String>,
        categoryListId: ArrayList<Int>,
        cList: ArrayList<StudentEditLeaveCategory>
    ) {

        val dialog = context?.let { it1 -> Dialog(it1) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.add_leave_application_layout)

        categorySpinner = dialog!!.findViewById(R.id.category_spinner) as PowerSpinnerView
        ivClose = dialog.findViewById(R.id.ivClose) as ImageView
        tvStartDate = dialog.findViewById(R.id.tvStartDate) as EditText
        tvStartTime = dialog.findViewById(R.id.tvStartTime) as EditText
        tvEndDate = dialog.findViewById(R.id.tvEndDate) as EditText
        tvEndTime = dialog.findViewById(R.id.tvEndTime) as EditText
        tvReason = dialog.findViewById(R.id.tvReason) as EditText
        tvAttachment = dialog.findViewById(R.id.tvAttachment) as EditText
        submitBtn = dialog.findViewById(R.id.submitBtn) as MaterialButton
        ImageChip = dialog.findViewById(R.id.ImageChip) as Chip

        val sam1 = dialog.findViewById<TextInputLayout>(R.id.sample1)
        val sam2 = dialog.findViewById<TextInputLayout>(R.id.sample2)
        val sam3 = dialog.findViewById<TextInputLayout>(R.id.sample3)
        val sam4 = dialog.findViewById<TextInputLayout>(R.id.sample4)
        val sam5 = dialog.findViewById<TextInputLayout>(R.id.sample5)
        val sam6 = dialog.findViewById<TextInputLayout>(R.id.sample6)

        categorySpinner!!.clearSelectedItem()
        categorySpinner!!.setItems(categoryListName)
        //categorySpinner!!.selectItemByIndex()

        var p = 0
        cList.forEach {
            if (it.id == leaveCategoryid) {
                categorySpinner!!.selectItemByIndex(p)
            }
            p += 1
        }

        val outputFormat: DateFormat = SimpleDateFormat("d-MM-yyyy", Locale.getDefault())
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.getDefault())

        val startDate: Date? = inputFormat.parse(fromDate)
        val endDate: Date? = inputFormat.parse(toDate)
        val outputStartDate: String? = outputFormat.format(startDate)
        val outputEndDate: String? = outputFormat.format(endDate)

        tvStartDate!!.setText(outputStartDate)
        tvStartTime!!.setText(fromTime)
        tvEndDate!!.setText(outputEndDate)
        tvEndTime!!.setText(toTime)
        tvReason!!.setText(reason)
        tvAttachment!!.setText(attachment)

        ivClose!!.setOnClickListener {
            dialog.dismiss()
        }

        var selectedId = leaveCategory
        var selectedName = ""
        categorySpinner!!.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            selectedId = 0
            selectedId = categoryListId[newIndex]
            selectedName = newItem

        }

        tvStartDate!!.setOnClickListener {
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
                tvStartDate!!.setText(tim)

                /* val timeZoneUTC = TimeZone.getDefault()
                 val offsetFromUTC = timeZoneUTC.getOffset(Date().time) * -1
                 val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                 val date = Date(it + offsetFromUTC) */

            }
            datePicker.show(parentFragmentManager, "MyTAG")

        }

        tvEndDate!!.setOnClickListener {
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
                tvEndDate!!.setText(tim)
            }
            datePicker.show(parentFragmentManager, "MyTAG")

        }

        tvStartTime!!.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

            picker.addOnPositiveButtonClickListener {
                var time: String = ""
                time = picker.hour.toString() + ":" + picker.minute.toString()

                val inputFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val outputFormat: DateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())

                val inputDate: Date? = inputFormat.parse(time)
                val outputDate: String = outputFormat.format(inputDate)

                tvStartTime!!.setText(outputDate)
            }

            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }

            picker.show(parentFragmentManager, "tag")
        }

        tvEndTime!!.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

            picker.addOnPositiveButtonClickListener {
                var time: String = ""
                time = picker.hour.toString() + ":" + picker.minute.toString()

                val inputFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val outputFormat: DateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())

                val inputDate: Date? = inputFormat.parse(time)
                val outputDate: String = outputFormat.format(inputDate)

                tvEndTime!!.setText(outputDate)
            }

            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }

            picker.show(parentFragmentManager, "tag")
        }

        tvAttachment!!.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(
                            1080,
                            1080
                        )
                        .start()
                }
            }
        }

        ImageChip!!.setOnClickListener {
            ImageChip!!.visibility = View.GONE
            tvAttachment!!.setText("")
            attachmentFile = null
            tvAttachment!!.hint = getString(R.string.attachment)
        }


        submitBtn!!.setOnClickListener {
            var tempCategory: String = ""
            var tempStartDate: String = ""
            var tempStartTime: String = ""
            var tempEndDate: String = ""
            var tempEndTime: String = ""
            var tempReason: String = ""
            var tempAttachment: String = ""

            tempCategory = selectedName
            tempStartDate = tvStartDate!!.text.toString()
            tempStartTime = tvStartTime!!.text.toString()
            tempEndDate = tvEndDate!!.text.toString()
            tempEndTime = tvEndTime!!.text.toString()
            tempReason = tvReason!!.text.toString()
            tempAttachment = tvAttachment!!.text.toString()

            if (selectedId == 0 || tempStartDate.isBlank() || tempStartTime.isBlank() || tempEndDate.isBlank() || tempEndTime.isBlank() || tempReason.isBlank() || tempAttachment.isBlank()) {
                if (selectedId == 0) {
                    categorySpinner!!.error = "Required"
                }
                if (tempStartDate.isBlank()) {
                    sam1!!.error = "Required"
                }
                if (tempStartTime.isBlank()) {
                    sam2!!.error = "Required"
                }
                if (tempEndDate.isBlank()) {
                    sam3!!.error = "Required"
                }
                if (tempEndTime.isBlank()) {
                    sam4!!.error = "Required"
                }
                if (tempReason.isBlank()) {
                    sam5!!.error = "Required"
                }
                if (tempAttachment.isBlank()) {
                    sam6!!.error = "Required"
                }
            } else {
                if (attachmentFile == null) {
                    /* String */
                    setUpEditLeaveApplicationObserver(
                        leaveId,
                        selectedId,
                        tempStartDate,
                        tempStartTime,
                        tempEndDate,
                        tempEndTime,
                        tempReason,
                        attachmentFile
                    )
                } else {
                    /*  with file */
                    setUpEditLeaveApplicationObserver(
                        leaveId,
                        selectedId,
                        tempStartDate,
                        tempStartTime,
                        tempEndDate,
                        tempEndTime,
                        tempReason,
                        attachmentFile,
                    )
                }
                dialog.dismiss()
            }
        }

        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
    }


    private fun setUpEditLeaveApplicationObserver(
        leaveId: Int,
        selectedId: Int,
        tempStartDate: String,
        tempStartTime: String,
        tempEndDate: String,
        tempEndTime: String,
        tempReason: String,
        attachmentFile: File?
    ) {

        /* api call  */
        studentLeaveApplyViewModel = StudentLeaveApplyViewModel(requireContext())
        studentLeaveApplyViewModel.studentUpdateLeave(
            leaveId,
            selectedId,
            tempStartDate,
            tempStartTime,
            tempEndDate,
            tempEndTime,
            tempReason,
            attachmentFile
        )
        studentLeaveApplyViewModel.studentUpdateLeaveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.message,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                    setupSortObserver(0)
                }
                Status.LOADING -> {
                    showProgress()
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
            }
        })
    }

    override fun onClicks() {

        /* add leave application btn */
        binding.addLeaveApplicationBtn.setOnClickListener {
            setupLeaveApplicationDialog()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupSortObserver(12)
        }

        /* Filter */
        binding.filter.setOnClickListener {
            setupFilterDialog()
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if (leaveList.size == 0) {
                Toaster.showToast(
                    requireContext(),
                    "No Data",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                permissionsBuilder(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).build().send { result ->
                    if (result.allGranted()) {
                        createCSV()
                    }
                }
            }
        }

        /* PDF btn */
        binding.tvPDF.setOnClickListener {
            if (leaveList.size == 0) {
                Toaster.showToast(
                    requireContext(),
                    "No Data",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                permissionsBuilder(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).build().send { result ->
                    if (result.allGranted()) {
                        createPDF()
                    }
                }
            }
        }

        /* Copy btn */
        binding.tvCopy.setOnClickListener {
            if (leaveList.size == 0) {
                Toaster.showToast(
                    requireContext(),
                    "No Data",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                copyData()
            }
        }

        /* Excel btn  */
        binding.tvExcel.setOnClickListener {
            if (leaveList.size == 0) {
                Toaster.showToast(
                    requireContext(),
                    "No Data",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                permissionsBuilder(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).build().send { result ->
                    if (result.allGranted()) {
                        createExcel()
                    }
                }
            }
        }
    }

    /* Create excel */
    private fun createExcel() {
        var writer: CSVWriter? = null
        try {
            val csv =
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_Leaves" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Applicant Name",
                    "Category",
                    "Leave days",
                    "Reason",
                    "From Date",
                    "To Date"
                )
            )

            writer.writeAll(data)
            for (i in 0 until leaveList.size) {
                writer.writeNext(
                    arrayOf(
                        leaveList[i].studentname.first_name + " " + leaveList[i].studentname.middle_name + " " + leaveList[i].studentname.last_name,
                        leaveList[i].leave_categoryname.name,
                        leaveList[i].leave_days.toString(),
                        leaveList[i].reason,
                        leaveList[i].from_date,
                        leaveList[i].to_date
                    )
                )
            }

            writer.close()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Success")
                .setMessage("Excel File successfully saved to internal storage")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, whichButton: Int ->
                }.show()
        } catch (ex: Exception) {
            Toaster.showToast(
                requireContext(),
                "Something went wrong",
                Toaster.State.WARNING,
                Toast.LENGTH_LONG
            )
        }
    }

    /* Copy Data */
    private fun copyData() {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        var clip: ClipData? = null
        var tempString = ""
        leaveList.forEach {
            tempString =
                tempString + "Applicant Name----" + it.studentname.first_name + " " + it.studentname.middle_name + " " + it.studentname.last_name + " , " + "Category----" + it.leave_categoryname.name + " , " + "Leave days----" + it.leave_days + "Reason----" + it.reason + "From Date----" + it.from_date + "To Date----" + it.to_date + "\n"
        }
        clip = ClipData.newPlainText(android.R.attr.label.toString(), tempString)
        clipboard!!.setPrimaryClip(clip!!)

        Toaster.showToast(
            requireContext(),
            "Copied to clipboard",
            Toaster.State.SUCCESS,
            Toast.LENGTH_LONG
        )
    }

    /* Create pdf */
    private fun createPDF() {
        val document = Document(PageSize.A4)
        val path: String =
            Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_Leaves" + System.currentTimeMillis() + ".pdf"
        try {
            PdfWriter.getInstance(document, FileOutputStream(path))
        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        document.open()

        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)

        val mTitleChunk = Chunk("Details")
        val mParagraphChunk = Paragraph(mTitleChunk)
        mParagraphChunk.alignment = Element.ALIGN_CENTER
        document.add(mParagraphChunk)
        document.add(Chunk(lineSeparator))

        try {
            leaveList.forEach {
                val mDataChunk =
                    Chunk("Applicant Name----" + it.studentname.first_name + " " + it.studentname.middle_name + " " + it.studentname.last_name + " , " + "Category----" + it.leave_categoryname.name + " , " + "Leave days----" + it.leave_days + " , " + "Reason----" + it.reason + " , " + "From Date----" + it.from_date + " , " + "To Date----" + it.to_date)
                val mDetailsChunk = Paragraph(mDataChunk)
                document.add(mDetailsChunk)
                document.add(Paragraph(""))
                if (!document.isOpen) {
                    document.open()
                }
            }
        } catch (ex: Exception) {
        }
        if (document.isOpen) {
            document.close()
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Success")
            .setMessage("PDF File successfully saved to internal storage")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, whichButton: Int ->
            }.show()
    }

    /* Create csv sheet */
    private fun createCSV() {
        var writer: CSVWriter? = null
        try {
            val csv =
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_Leaves" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Applicant Name",
                    "Category",
                    "Leave days",
                    "Reason",
                    "From Date",
                    "To Date"
                )
            )

            writer.writeAll(data)
            for (i in 0 until leaveList.size) {
                writer.writeNext(
                    arrayOf(
                        leaveList[i].studentname.first_name + " " + leaveList[i].studentname.middle_name + " " + leaveList[i].studentname.last_name,
                        leaveList[i].leave_categoryname.name,
                        leaveList[i].leave_days.toString(),
                        leaveList[i].reason,
                        leaveList[i].from_date,
                        leaveList[i].to_date
                    )
                )
            }

            writer.close()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Success")
                .setMessage("CSV  File successfully saved to internal storage")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, whichButton: Int ->
                }.show()
        } catch (ex: Exception) {
            Toaster.showToast(
                requireContext(),
                "Something went wrong",
                Toaster.State.WARNING,
                Toast.LENGTH_LONG
            )
        }
    }

    /* add leave application dialog api call for category*/
    private fun setupLeaveApplicationDialog() {

        /* api call */
        studentLeaveApplyViewModel = StudentLeaveApplyViewModel(requireContext())
        studentLeaveApplyViewModel.studentLeaveApplyCategory()
        studentLeaveApplyViewModel.studentLeaveApplyCategoryData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    categoryList.clear()
                    categoryIDList.clear()
                    it.data!!.data.forEach {
                        categoryList.addAll(listOf(it.name))
                        categoryIDList.addAll(listOf(it.id))
                    }


                    if (categoryList.size != 0) {
                        setUpDialog(categoryList, categoryIDList)
                    }
                }
                Status.LOADING -> {
                    showProgress()
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
            }
        })
    }

    /* add leave application dialog */
    private fun setUpDialog(categoryList: ArrayList<String>, categoryIDList: ArrayList<Int>) {
        val dialog = context?.let { it1 -> Dialog(it1) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.add_leave_application_layout)

        categorySpinner = dialog!!.findViewById(R.id.category_spinner) as PowerSpinnerView
        ivClose = dialog.findViewById(R.id.ivClose) as ImageView
        tvStartDate = dialog.findViewById(R.id.tvStartDate) as EditText
        tvStartTime = dialog.findViewById(R.id.tvStartTime) as EditText
        tvEndDate = dialog.findViewById(R.id.tvEndDate) as EditText
        tvEndTime = dialog.findViewById(R.id.tvEndTime) as EditText
        tvReason = dialog.findViewById(R.id.tvReason) as EditText
        tvAttachment = dialog.findViewById(R.id.tvAttachment) as EditText
        submitBtn = dialog.findViewById(R.id.submitBtn) as MaterialButton
        ImageChip = dialog.findViewById(R.id.ImageChip) as Chip

        val sam1 = dialog.findViewById<TextInputLayout>(R.id.sample1)
        val sam2 = dialog.findViewById<TextInputLayout>(R.id.sample2)
        val sam3 = dialog.findViewById<TextInputLayout>(R.id.sample3)
        val sam4 = dialog.findViewById<TextInputLayout>(R.id.sample4)
        val sam5 = dialog.findViewById<TextInputLayout>(R.id.sample5)
        val sam6 = dialog.findViewById<TextInputLayout>(R.id.sample6)

        categorySpinner!!.clearSelectedItem()
        categorySpinner!!.setItems(categoryList)

        categorySpinner!!.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            selectedId = 0
            selectedId = categoryIDList[newIndex]
            selectedName = newItem

            Log.e("TAG", "setUpDialog: selectedId---$selectedId-------selectedName---$selectedName")
        }

        ivClose!!.setOnClickListener {
            dialog.dismiss()
        }

        tvStartDate!!.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
            val constraintsBuilder =
                CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(constraintsBuilder.build())

            val datePicker = builder.build()
            datePicker.isCancelable = false
            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                val tim =
                    "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                        calendar.get(Calendar.YEAR)
                    }"
                tvStartDate!!.setText(tim)
            }
            datePicker.show(parentFragmentManager, "MyTAG")

        }

        tvEndDate!!.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
            val today = MaterialDatePicker.todayInUtcMilliseconds() + 1000
            val constraintsBuilder =
                CalendarConstraints.Builder().setValidator(DateValidatorPointForward.from(today))
            builder.setCalendarConstraints(constraintsBuilder.build())

            val datePicker = builder.build()
            datePicker.isCancelable = false
            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                val tim =
                    "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                        calendar.get(Calendar.YEAR)
                    }"
                tvEndDate!!.setText(tim)
            }
            datePicker.show(parentFragmentManager, "MyTAG")

        }

        tvStartTime!!.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

            picker.addOnPositiveButtonClickListener {
                var time: String = ""
                time = picker.hour.toString() + ":" + picker.minute.toString()

                var t = ""
                var ho = picker.hour
                val min = picker.minute
                if (ho in 0..11) {
                    t = "$ho:$min AM"
                } else {
                    if (ho == 12) {
                        t = "$ho:$min PM"
                    } else {
                        ho -= 12
                        t = "$ho:$min PM"
                    }
                }

                Log.e("TAG", "setUpDialog: $t")

                val inputFormat: DateFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
                val outputFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                val inputDate: Date? = inputFormat.parse(t)
                val outputDate: String = outputFormat.format(inputDate)

                Log.e("TAG", "setUpDialog: $outputDate")

                tvStartTime!!.setText(outputDate)
            }

            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }

            picker.show(parentFragmentManager, "tag")
        }

        tvEndTime!!.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

            picker.addOnPositiveButtonClickListener {
                var time: String = ""
                time = picker.hour.toString() + ":" + picker.minute.toString()

                var t = ""
                var ho = picker.hour
                val min = picker.minute
                if (ho in 0..11) {
                    t = "$ho:$min AM"
                } else {
                    if (ho == 12) {
                        t = "$ho:$min PM"
                    } else {
                        ho -= 12
                        t = "$ho:$min PM"
                    }
                }

                Log.e("TAG", "setUpDialog: $t")

                val inputFormat: DateFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
                val outputFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                val inputDate: Date? = inputFormat.parse(t)
                val outputDate: String = outputFormat.format(inputDate)

                Log.e("TAG", "setUpDialog: $outputDate")

                tvEndTime!!.setText(outputDate)
            }

            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }

            picker.show(parentFragmentManager, "tag")
        }


        tvAttachment!!.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(
                            1080,
                            1080
                        )
                        .start()
                }
            }
        }

        ImageChip!!.setOnClickListener {
            ImageChip!!.visibility = View.INVISIBLE
            tvAttachment!!.setText("")
            attachmentFile = null
            tvAttachment!!.hint = getString(R.string.attachment)
        }

        submitBtn!!.setOnClickListener {
            var tempCategory: String = ""
            var tempStartDate: String = ""
            var tempStartTime: String = ""
            var tempEndDate: String = ""
            var tempEndTime: String = ""
            var tempReason: String = ""

            tempCategory = selectedName
            tempStartDate = tvStartDate!!.text.toString()
            tempStartTime = tvStartTime!!.text.toString()
            tempEndDate = tvEndDate!!.text.toString()
            tempEndTime = tvEndTime!!.text.toString()
            tempReason = tvReason!!.text.toString()

            if (selectedId == 0 || tempStartDate.isBlank() || tempStartTime.isBlank() || tempEndDate.isBlank() || tempEndTime.isBlank() || tempReason.isBlank() || attachmentFile == null) {
                if (selectedId == 0) {
                    categorySpinner!!.error = "Required"
                }
                if (tempStartDate.isBlank()) {
                    sam1!!.error = "Required"
                }
                if (tempStartTime.isBlank()) {
                    sam2!!.error = "Required"
                }
                if (tempEndDate.isBlank()) {
                    sam3!!.error = "Required"
                }
                if (tempEndTime.isBlank()) {
                    sam4!!.error = "Required"
                }
                if (tempReason.isBlank()) {
                    sam5!!.error = "Required"
                }
                if (attachmentFile == null) {
                    sam6!!.error = "Required"
                }
            } else {
                setUpAddLeaveApplicationObserver(
                    selectedId,
                    tempStartDate,
                    tempStartTime,
                    tempEndDate,
                    tempEndTime,
                    tempReason,
                    attachmentFile!!
                )

                dialog.dismiss()
                Log.e(
                    "TAG",
                    "setUpDialog: " + "selectedId----" + selectedId + "tempStartDate-----" + tempStartDate + "tempStartTime-----" + tempStartTime + "tempEndDate-----" + tempEndDate + "tempEndTime-----" + tempEndTime + "tempReason------" + tempReason + "attachmentFile" + attachmentFile
                )
            }
        }


        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes = layoutParams
    }

    /* add leave api */
    private fun setUpAddLeaveApplicationObserver(
        tempCategory: Int,
        tempStartDate: String,
        tempStartTime: String,
        tempEndDate: String,
        tempEndTime: String,
        tempReason: String,
        attachmentFileUrl: File
    ) {

        /* api call  */
        studentLeaveApplyViewModel = StudentLeaveApplyViewModel(requireContext())
        studentLeaveApplyViewModel.studentSubmitLeave(
            tempCategory,
            tempStartDate,
            tempStartTime,
            tempEndDate,
            tempEndTime,
            tempReason,
            attachmentFileUrl
        )
        studentLeaveApplyViewModel.studentSubmitLeaveApplicationData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        "Successfully submitted",
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                    attachmentFile = null
                    setupSortObserver(0)
                }
                Status.LOADING -> {
                    showProgress()
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
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

    /* setup filter dialog */
    private fun setupFilterDialog() {
        val singleItems = arrayOf("Pending", "Approved", "Rejected")
        val checkedItem = 0
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.sortby))

            /* Single-choice items (initialized with checked item) */
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                /* Respond to item chosen*/
                dialog.dismiss()
                setupSortObserver(which)
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            //for uri
            val fileUri = data?.data
            ImageChip!!.visibility = View.VISIBLE
            ImageChip!!.text = fileUri.toString()
            tvAttachment!!.setText(fileUri.toString())

            //for file object
            attachmentFile = null
            attachmentFile = ImagePicker.getFile(data)!!

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_menu, menu)

        val searchItem = menu.findItem(R.id.customtoolbar_search)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search leaves"
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = true
        searchView.isIconifiedByDefault = true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        studentLeaveApplyAdapter.filter.filter(newText)
        return false
    }
}