package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_scheduled

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentTestScheduleBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.mock_test_list.StudentMockTestListTest
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentMockTestListViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StudentTestScheduleFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentStudentTestScheduleBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentMockTestListViewModel: StudentMockTestListViewModel
    private var mockList = ArrayList<StudentMockTestListTest>()
    private lateinit var studentTestScheduleAdapter: StudentTestScheduleAdapter

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentTestScheduleBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.test_scheduled))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvStudentTestSchedule.layoutManager = LinearLayoutManager(context)

        setupDateObserver("", 0)
    }

    /* Mock list api */
    private fun setupDateObserver(tdate: String, sort: Int) {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* mock test api */
        studentMockTestListViewModel = StudentMockTestListViewModel()
        studentMockTestListViewModel.studentMockTestList(tdate, sort)
        studentMockTestListViewModel.studentMockTestListData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    mockList.clear()
                    mockList.addAll(it.data!!.test_list)

                    studentTestScheduleAdapter = StudentTestScheduleAdapter(mockList) { testId(it) }
                    binding.rvStudentTestSchedule.adapter = studentTestScheduleAdapter

                    if (mockList.size == 0) {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
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

    /* Attend test */
    private fun testId(testId: Int) {
        appCompatActivity?.replaceFragment(
            fragment = StudentTestScheduleInstructionsFragment.newInstance(
                testId
            ), addToBackStack = true
        )
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {
    }

    override fun onClicks() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupDateObserver("", 0)
        }

        /* select date */
        binding.tvSelectDate.setOnClickListener {
            setUpSelectDateDialog()
        }

        /* submit date button */
        binding.submitDateBtn.setOnClickListener {
            val selectedDate = binding.tvSelectDate.text.toString()
            if (selectedDate.isBlank()) {
                Toaster.showToast(
                    requireContext(),
                    "Date Required",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                setupDateObserver(selectedDate, 0)
            }
        }

        /* filter btn */
        binding.filter.setOnClickListener {
            setupFilterDialog()
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if (mockList.size == 0) {
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
            if (mockList.size == 0) {
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
            if (mockList.size == 0) {
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
            if (mockList.size == 0) {
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_MockTest_Scheduled" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Id",
                    "Name",
                    "Date",
                    "Time",
                    "Subject",
                    "Class",
                    "Division",
                    "Instructions"
                )
            )

            writer.writeAll(data)
            for (i in 0 until mockList.size) {
                writer.writeNext(
                    arrayOf(
                        mockList[i].id.toString(),
                        mockList[i].name,
                        mockList[i].tdate,
                        mockList[i].testfrom + " - " + mockList[i].testto,
                        mockList[i].subjectname.name,
                        mockList[i].classname.name,
                        mockList[i].divisionname.name,
                        mockList[i].instructionname.title
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
            Log.e("TAG", "createExcel: $ex")
        }
    }

    /* Copy Data */
    private fun copyData() {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        var clip: ClipData? = null
        var tempString = ""
        mockList.forEach {
            tempString =
                tempString + "ID----" + it.id + " , " + "Name----" + it.name + " , " + "Date----" + it.tdate + " , " + "Time----" + it.testto + " - " + it.testto + " , " + "Subject----" + it.subjectname.name + " , " + "Class----" + it.classname.name + " , " + "Division----" + it.divisionname.name + " , " + "Instructions" + it.instructionname.title + "\n"
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
            Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_MockTest_Scheduled" + System.currentTimeMillis() + ".pdf"
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
            mockList.forEach {
                val mDataChunk =
                    Chunk("ID----" + it.id + " , " + "Name----" + it.name + " , " + "Date----" + it.tdate + " , " + "Time----" + it.testto + " - " + it.testto + " , " + "Subject----" + it.subjectname.name + " , " + "Class----" + it.classname.name + " , " + "Division----" + it.divisionname.name + " , " + "Instructions" + it.instructionname.title)
                val mDetailsChunk = Paragraph(mDataChunk)
                document.add(mDetailsChunk)
                document.add(Paragraph(""))
                if (!document.isOpen) {
                    document.open()
                }
            }
        } catch (ex: Exception) {
            Log.e("TAG", "createPDF: $ex")
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_MockTest_Scheduled" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Id",
                    "Name",
                    "Date",
                    "Time",
                    "Subject",
                    "Class",
                    "Division",
                    "Instructions"
                )
            )

            writer.writeAll(data)
            for (i in 0 until mockList.size) {
                writer.writeNext(
                    arrayOf(
                        mockList[i].id.toString(),
                        mockList[i].name,
                        mockList[i].tdate,
                        mockList[i].testfrom + " - " + mockList[i].testto,
                        mockList[i].subjectname.name,
                        mockList[i].classname.name,
                        mockList[i].divisionname.name,
                        mockList[i].instructionname.title
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
            Log.e("TAG", "createExcel: $ex")
        }
    }

    /* setup filter dialog */
    private fun setupFilterDialog() {
        val singleItems = arrayOf("Early applied", "Last applied")
        val checkedItem = 0
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.sortby))

            /* Single-choice items (initialized with checked item) */
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                /* Respond to item chosen*/
                dialog.dismiss()
                setupDateObserver("", which)
            }
            .show()
    }

    /* Date select dialog */
    private fun setUpSelectDateDialog() {
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
            val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDate: Date? = inputFormat.parse(tim)
            val outputStartDate: String? = outputFormat.format(startDate)
            binding.tvSelectDate.text = outputStartDate
        }
        datePicker.show(parentFragmentManager, "MyTAG")
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_menu, menu)

        val searchItem = menu.findItem(R.id.customtoolbar_search)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Test"
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = true
        searchView.isIconifiedByDefault = true
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = true

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }

    /* option menu  */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_search -> {

            true
        }
        R.id.customtoolbar_chat -> {
            val intent = Intent(requireContext(), StudentChatActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        studentTestScheduleAdapter.filter.filter(newText.toString())
        return false
    }

}