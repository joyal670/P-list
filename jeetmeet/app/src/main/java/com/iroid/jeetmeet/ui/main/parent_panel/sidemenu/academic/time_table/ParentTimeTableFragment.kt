package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.time_table

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.tabs.TabLayoutMediator
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentTimeTableBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.other.DayModelObject
import com.iroid.jeetmeet.modal.other.DaysModel
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListData
import com.iroid.jeetmeet.modal.parent.time_table.ParentTimeTable
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentStudentsListViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentTimeTableViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter


class ParentTimeTableFragment : BaseFragment() {

    private lateinit var binding: FragmentParentTimeTableBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var timeTablePageAdaptor: TimeTablePageAdaptor
    private lateinit var parentStudentsListViewModel: ParentStudentsListViewModel
    private var studentList = ArrayList<ParentStudentsListData>()
    private lateinit var parentTimeTableViewModel: ParentTimeTableViewModel
    private var timetableList = ArrayList<ParentTimeTable>()

    var studentNameList = ArrayList<String>()
    var studentIdList = ArrayList<Int>()

    private var selectedId = 0
    private var selectedName: String = ""

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentTimeTableFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentTimeTableBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.time_table))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {

        /* Viewpager setup */
        binding.timeTableViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position > 0 && positionOffset == 0.0f && positionOffsetPixels == 0) {


                    binding.timeTableViewPager.layoutParams.height =
                        binding.timeTableViewPager.getChildAt(0).height


                }
            }
        })

        setupObserver()

    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        parentStudentsListViewModel = ParentStudentsListViewModel()
        parentStudentsListViewModel.parentStudentsList()
        parentStudentsListViewModel.parentStudentsListData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    studentList.clear()
                    studentNameList.clear()
                    studentIdList.clear()

                    studentList.addAll(it.data!!.data)

                    it.data.data.forEach {
                        studentNameList.addAll(listOf(it.first_name))
                        studentIdList.addAll(listOf(it.id))
                    }

                    binding.studentSpinner.clearSelectedItem()
                    binding.studentSpinner.setItems(studentNameList)
                    if (studentIdList.size > 0) {
                        binding.studentSpinner.selectItemByIndex(0)
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

    override fun onClicks() {

        /* spinner listener */
        binding.studentSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedId = 0
            selectedId = studentIdList[newIndex]
            selectedName = newText

            Log.e("TAG", "onClicks: selectedId$selectedId----selectedName$selectedName")

            studentList.forEach {
                if (selectedId == it.id) {
                    binding.tvStudentName.text = it.first_name
                    Glide.with(requireContext()).load(it.profile_image_url)
                        .into(binding.circleImageView)
                }
            }

            setUpTimeTableObserver()
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if (timetableList.size == 0) {
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
            if (timetableList.size == 0) {
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
            if (timetableList.size == 0) {
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
            if (timetableList.size == 0) {
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

    private fun setUpTimeTableObserver() {
        Log.e("TAG", "setUpCalenderObserver: $selectedId")

        parentTimeTableViewModel = ParentTimeTableViewModel()
        parentTimeTableViewModel.parentTimeTable(selectedId)
        parentTimeTableViewModel.parentTimeTableData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    timeTablePageAdaptor =
                        TimeTablePageAdaptor(requireActivity().supportFragmentManager, lifecycle)

                    timetableList.clear()

                    for (key in it.data!!.days!!.keys) {
                        if (it.data.data!!.keys.contains(key)) {

                            it.data.data.values.forEach { v ->
                                timetableList.addAll(v)
                            }

                            timeTablePageAdaptor.addFragment(
                                DynamicTimeTableFragment.newInstance(it.data.data[key])
                            )
                        } else {
                            val timeTableList = ArrayList<ParentTimeTable>()
                            timeTablePageAdaptor.addFragment(
                                DynamicTimeTableFragment.newInstance(
                                    timeTableList
                                )
                            )
                        }
                    }

                    val dayModel: List<DaysModel> = DayModelObject.daysModel

                    binding.timeTableViewPager.adapter = timeTablePageAdaptor

                    TabLayoutMediator(
                        binding.dayTabLayout,
                        binding.timeTableViewPager
                    ) { tab, position ->
                        tab.text = dayModel[position].day
                    }.attach()
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
            }
        })
    }

    /* Create excel */
    private fun createExcel() {
        var writer: CSVWriter? = null
        try {
            val csv =
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_TimeTable" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Day",
                    "Teacher",
                    "Subject",
                    "Slot",
                    "Room"
                )
            )

            writer.writeAll(data)
            for (i in 0 until timetableList.size) {
                writer.writeNext(
                    arrayOf(
                        timetableList[i].day,
                        timetableList[i].teachername.first_name + timetableList[i].teachername.last_name,
                        timetableList[i].subjectname.name,
                        timetableList[i].slot.toString(),
                        timetableList[i].roomname.name
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
        timetableList.forEach {
            tempString =
                tempString + "Day----" + it.day + " , " + "Teacher----" + it.teachername.first_name + it.teachername.last_name + " , " + "Subject----" + it.subjectname.name + " , " + "Slot----" + it.slot + " , " + "Room----" + it.roomname.name + "\n"
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
            Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_TimeTable" + System.currentTimeMillis() + ".pdf"
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
            timetableList.forEach {
                val mDataChunk =
                    Chunk("Day----" + it.day + " , " + "Teacher----" + it.teachername.first_name + it.teachername.last_name + " , " + "Subject----" + it.subjectname.name + " , " + "Slot----" + it.slot + " , " + "Room----" + it.roomname.name)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_TimeTable" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Day",
                    "Teacher",
                    "Subject",
                    "Slot",
                    "Room"
                )
            )

            writer.writeAll(data)
            for (i in 0 until timetableList.size) {
                writer.writeNext(
                    arrayOf(
                        timetableList[i].day,
                        timetableList[i].teachername.first_name + timetableList[i].teachername.last_name,
                        timetableList[i].subjectname.name,
                        timetableList[i].slot.toString(),
                        timetableList[i].roomname.name
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

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }

}