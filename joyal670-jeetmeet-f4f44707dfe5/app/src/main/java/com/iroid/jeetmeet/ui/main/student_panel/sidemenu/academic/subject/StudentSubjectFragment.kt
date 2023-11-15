package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.subject

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
import com.iroid.jeetmeet.databinding.FragmentStudentSubjectBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.subjects.StudentSubjectSubject
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentSubjectsViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter


class StudentSubjectFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentStudentSubjectBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentSubjectsViewModel: StudentSubjectsViewModel
    private lateinit var studentSubjectAdapter: StudentSubjectAdapter
    private var subjectsList = ArrayList<StudentSubjectSubject>()
    private var subjectsClass = ""
    private var subjectsDivision = ""

    var catagoryList = ArrayList<String>()
    var catagoryListId = ArrayList<Int>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentSubjectBinding.inflate(inflater!!,container,false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.subject))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        binding.rvStudentSubject.layoutManager = LinearLayoutManager(context)

        setupObserver()

    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call */
        studentSubjectsViewModel = StudentSubjectsViewModel()
        studentSubjectsViewModel.studentSubjects(0)
        studentSubjectsViewModel.studentSubjectData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    subjectsList.clear()
                    subjectsList.addAll(it.data!!.subjects)

                    subjectsClass = it.data.`class`.name
                    subjectsDivision = it.data.division.name
                    studentSubjectAdapter = StudentSubjectAdapter(subjectsList, subjectsClass, subjectsDivision)
                    binding.rvStudentSubject.adapter = studentSubjectAdapter

                    if(subjectsList.size == 0)
                    {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }

                    catagoryList.clear()
                    catagoryListId.clear()
                    subjectsList.forEach {
                        catagoryList.addAll(listOf(it.name))
                        catagoryListId.addAll(listOf(it.id))
                    }
                }
                Status.LOADING ->{
                    showProgress()
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
                Status.DATA_EMPTY ->{
                    dismissProgress()
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
            }
        })
    }

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }

        /* Filter */
        binding.tvFilter.setOnClickListener {
            setupFilterDialog()
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if(subjectsList.size == 0)
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
            if(subjectsList.size == 0)
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
            if(subjectsList.size == 0)
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
            if(subjectsList.size == 0)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_Subjects" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Code",
                "Name",
                "Teacher",
                "Author",
                "Class",
                "Pass Mark",
                "Final Mark",
                "Note"
            )
            )

            writer.writeAll(data)
            for (i in 0 until subjectsList.size)
            {
                writer.writeNext(arrayOf(subjectsList[i].subject_code , subjectsList[i].name, subjectsList[i].teachers , subjectsList[i].author, subjectsClass + subjectsDivision, subjectsList[i].pass_mark.toString(), subjectsList[i].total_mark.toString(), subjectsList[i].note))
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
        subjectsList.forEach {
            tempString = tempString + "Code----" + it.subject_code + " , " + "Name----" + it.name + " , " + "Note----" + it.note + "Teacher----"+it.teachers + "Author----"+it.author+ "Class----"+subjectsClass + subjectsDivision + "Pass Mark----" + it.pass_mark.toString() + "Final Mark----"+ it.total_mark.toString() + "Note----"+it.note + "\n"
        }
        clip = ClipData.newPlainText(android.R.attr.label.toString(), tempString)
        clipboard!!.setPrimaryClip(clip!!)

        Toaster.showToast(requireContext(), "Copied to clipboard" , Toaster.State.SUCCESS, Toast.LENGTH_LONG)
    }

    /* Create pdf */
    private fun createPDF() {
        val document = Document(PageSize.A4)
        val path: String = Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_Subjects" + System.currentTimeMillis() + ".pdf"
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
            subjectsList.forEach {
                val mDataChunk = Chunk("Code----" + it.subject_code + " , " + "Name----" + it.name + " , " + "Note----" + " , " + it.note + "Teacher----"+it.teachers + " , " + "Author----"+it.author + " , " + "Class----"+subjectsClass + subjectsDivision + " , " + "Pass Mark----" + it.pass_mark.toString() + " , " + "Final Mark----"+ it.total_mark.toString() + " , " + "Note----"+it.note)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_Subjects" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Code",
                "Name",
                "Teacher",
                "Author",
                "Class",
                "Pass Mark",
                "Final Mark",
                "Note"
            )
            )

            writer.writeAll(data)
            for (i in 0 until subjectsList.size)
            {
                writer.writeNext(arrayOf(subjectsList[i].subject_code , subjectsList[i].name, subjectsList[i].teachers , subjectsList[i].author, subjectsClass + subjectsDivision, subjectsList[i].pass_mark.toString(), subjectsList[i].total_mark.toString(), subjectsList[i].note))
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

    /* filter dialog */
    private fun setupFilterDialog() {
        val checkedItem = 0
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.select_subject))

            /* Single-choice items (initialized with checked item) */
            .setSingleChoiceItems(catagoryList.toTypedArray(), checkedItem) { dialog, which ->
                /* Respond to item chosen*/
                setUpSubjectObserver(catagoryListId[which])
                dialog.dismiss()

            }
            .show()
    }

    /* filter api */
    private fun setUpSubjectObserver(subjectID: Int) {
        Log.e("TAG", "setUpSubjectObserver: $subjectID" )
        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call */
        studentSubjectsViewModel = StudentSubjectsViewModel()
        studentSubjectsViewModel.studentSubjects(subjectID)
        studentSubjectsViewModel.studentSubjectData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    subjectsList.clear()
                    subjectsList.addAll(it.data!!.subjects)

                    subjectsClass = it.data.`class`.name
                    subjectsDivision = it.data.division.name
                    studentSubjectAdapter = StudentSubjectAdapter(subjectsList, subjectsClass, subjectsDivision)
                    binding.rvStudentSubject.adapter = studentSubjectAdapter

                    if(subjectsList.size == 0)
                    {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }

                    /*catagoryList.clear()
                    catagoryListId.clear()
                    subjectsList.forEach {
                        catagoryList.addAll(listOf(it.name))
                        catagoryListId.addAll(listOf(it.id))
                    }*/
                }
                Status.LOADING ->{
                    showProgress()
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
                Status.DATA_EMPTY ->{
                    dismissProgress()
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
            }
        })

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
        searchView.queryHint = "Search Subjects"
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
        studentSubjectAdapter.filter.filter(newText)
       return false
    }

}