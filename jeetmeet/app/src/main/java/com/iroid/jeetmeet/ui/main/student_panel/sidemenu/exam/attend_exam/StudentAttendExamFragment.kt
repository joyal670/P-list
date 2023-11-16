package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.attend_exam

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentAttendExamBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.attend_exam.StudentAttendExamsAttended
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentAttendExamViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter


class StudentAttendExamFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentStudentAttendExamBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentAttendExamViewModel: StudentAttendExamViewModel
    private lateinit var studentAttendExamAdapter: StudentAttendExamAdapter
    private var examList = ArrayList<StudentAttendExamsAttended>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentAttendExamBinding.inflate(inflater!!,container,false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.attend_exam))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        binding.rvStudentAttendExam.layoutManager = LinearLayoutManager(context)

        setupSortObserver(0)
    }

    private fun setupSortObserver(sort: Int) {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* Student attend exam api */
        studentAttendExamViewModel = StudentAttendExamViewModel()
        studentAttendExamViewModel.studentAttendExam(sort)
        studentAttendExamViewModel.studentAttendData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    examList.clear()
                    examList.addAll(it.data!!.exams_attended)
                    studentAttendExamAdapter = StudentAttendExamAdapter(examList)
                    binding.rvStudentAttendExam.adapter = studentAttendExamAdapter

                    if(examList.size == 0)
                    {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.LOADING ->{
                    showProgress()
                }
                Status.ERROR ->{
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY ->{
                    dismissProgress()
                }
                Status.NO_INTERNET ->{
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

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupSortObserver(0)
        }

        /* filter */
        binding.filter.setOnClickListener {
            setupFilterDialog()
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if(examList.size == 0)
            {
                Toaster.showToast(requireContext(), "No Data", Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else
            {
                permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).build().send { result ->
                    if(result.allGranted())
                    {
                        createCSV()
                    }
                }
            }
        }

        /* PDF btn */
        binding.tvPDF.setOnClickListener {
            if(examList.size == 0)
            {
                Toaster.showToast(requireContext(), "No Data", Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else
            {
                permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).build().send { result ->
                    if(result.allGranted())
                    {
                        createPDF()
                    }
                }
            }
        }

        /* Copy btn */
        binding.tvCopy.setOnClickListener {
            if(examList.size == 0)
            {
                Toaster.showToast(requireContext(), "No Data", Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else
            {
                copyData()
            }
        }

        /* Excel btn  */
        binding.tvExcel.setOnClickListener {
            if(examList.size == 0)
            {
                Toaster.showToast(requireContext(), "No Data", Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else
            {
                permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).build().send { result ->
                    if(result.allGranted())
                    {
                        createExcel()
                    }
                }
            }
        }
    }

    /* Create excel */
    private fun createExcel() {
        var writer: CSVWriter? = null
        try{
            val csv =
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_ExamAttend" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Name",
                "Exam Category",
                "Class & Division",
                "Subject",
                "Room",
                "Instructions"
            )
            )

            writer.writeAll(data)
            for (i in 0 until examList.size)
            {
                writer.writeNext(arrayOf(examList[i].name , examList[i].exams_category.name , examList[i].classes.name + examList[i].divisions.name, examList[i].subjects.name, examList[i].rooms.name, examList[i].instructions.title ))
            }

            writer.close()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Success")
                .setMessage("Excel File successfully saved to internal storage")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, whichButton: Int ->
                }.show()
        }
        catch (ex : Exception)
        {
            Toaster.showToast(requireContext(), "Something went wrong", Toaster.State.WARNING, Toast.LENGTH_LONG)
            Log.e("TAG", "createExcel: $ex" )
        }
    }

    /* Copy Data */
    private fun copyData() {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        var clip : ClipData? = null
        var tempString = ""
        examList.forEach {
            tempString = tempString + "Name----" + it.name + " , " + "Exam Category----" + it.category + " , " + "Class & Division----" + it.classes.name + it.divisions.name + " , " + "Subject----" + it.subjects.name + " , " + "Room----" + it.rooms.name + " , " + "Instructions----" + it.instructions.title + "\n"
        }
        clip = ClipData.newPlainText(android.R.attr.label.toString(), tempString)
        clipboard!!.setPrimaryClip(clip!!)

        Toaster.showToast(requireContext(), "Copied to clipboard" , Toaster.State.SUCCESS, Toast.LENGTH_LONG)
    }

    /* Create pdf */
    private fun createPDF() {
        val document = Document(PageSize.A4)
        val path: String = Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_ExamAttend" + System.currentTimeMillis() + ".pdf"
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
            examList.forEach {
                val mDataChunk = Chunk("Name----" + it.name + " , " + "Exam Category----" + it.category + " , " + "Class & Division----" + it.classes.name + it.divisions.name + " , " + "Subject----" + it.subjects.name + " , " + "Room----" + it.rooms.name + " , " + "Instructions----" + it.instructions.title)
                val mDetailsChunk = Paragraph(mDataChunk)
                document.add(mDetailsChunk)
                document.add(Paragraph(""))
                if (!document.isOpen) {
                    document.open()
                }
            }
        }catch (ex : Exception)
        {
            Log.e("TAG", "createPDF: $ex" )
        }
        if (document.isOpen) {
            document.close();
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
        try{
            val csv =
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_ExamAttend" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Name",
                "Exam Category",
                "Class & Division",
                "Subject",
                "Room",
                "Instructions"
            )
            )

            writer.writeAll(data)
            for (i in 0 until examList.size)
            {
                writer.writeNext(arrayOf(examList[i].name , examList[i].exams_category.name , examList[i].classes.name + examList[i].divisions.name, examList[i].subjects.name, examList[i].rooms.name, examList[i].instructions.title ))
            }

            writer.close()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Success")
                .setMessage("CSV  File successfully saved to internal storage")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, whichButton: Int ->
                }.show()
        }
        catch (ex : Exception)
        {
            Toaster.showToast(requireContext(), "Something went wrong", Toaster.State.WARNING, Toast.LENGTH_LONG)
            Log.e("TAG", "createExcel: $ex" )
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
                setupSortObserver(which)
            }
            .show()
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_menu, menu)

        val searchItem = menu.findItem(R.id.customtoolbar_search)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Exams"
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = true
        searchView.isIconifiedByDefault = true
    }

    /* option menu  */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_search -> {

            true
        }
        R.id.customtoolbar_chat -> {
            val intent= Intent(requireContext(), StudentChatActivity::class.java)
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
        studentAttendExamAdapter.filter.filter(newText)
        return false
    }

}