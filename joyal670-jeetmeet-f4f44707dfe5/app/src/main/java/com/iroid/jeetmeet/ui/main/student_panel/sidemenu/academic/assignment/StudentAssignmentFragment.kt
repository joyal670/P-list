package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.assignment

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
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
import com.iroid.jeetmeet.databinding.FragmentStudentAssignemntBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.assignments.StudentAssignmentsAssignment
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentAssignmentsViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter


class StudentAssignmentFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentStudentAssignemntBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentAssignmentsViewModel: StudentAssignmentsViewModel
    private var assignmentsList = ArrayList<StudentAssignmentsAssignment>()
    private lateinit var studentAssignmentSideAdapter: StudentAssignmentSideAdapter

    var catagoryList = ArrayList<String>()
    var catagoryListId = ArrayList<Int>()


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentAssignemntBinding.inflate(inflater!!,container,false)
        return binding.root    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.assignment))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        binding.rvStudentAssignment.layoutManager = LinearLayoutManager(context)
        setupObserver()
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call */
        studentAssignmentsViewModel = StudentAssignmentsViewModel()
        studentAssignmentsViewModel.studentAssignment(0)
        studentAssignmentsViewModel.studentAssignmentData.observe(this, Observer {
            when(it.status)
            {
                Status.SUCCESS ->{

                    dismissProgress()

                    assignmentsList.clear()
                    assignmentsList.addAll(it.data!!.data.assignments)

                    studentAssignmentSideAdapter = StudentAssignmentSideAdapter(assignmentsList) {openBrowser(it)}
                    binding.rvStudentAssignment.adapter = studentAssignmentSideAdapter

                    if(assignmentsList.size == 0)
                    {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }

                    catagoryList.clear()
                    catagoryListId.clear()
                    it.data.data.all_subjects.forEach {
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
            }
        })
    }

    /* open attachment in browser */
    private fun openBrowser(attachmentUrl: String) {
        val builder = CustomTabsIntent.Builder()
        val colorInt: Int = Color.parseColor("#EE2424")
        builder.setToolbarColor(colorInt)
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(attachmentUrl))
    }

    override fun onClicks() {

        /* filter option */
        binding.filter.setOnClickListener {
            setupFilterDialog()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if(assignmentsList.size == 0)
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
            if(assignmentsList.size == 0)
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
            if(assignmentsList.size == 0)
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
            if(assignmentsList.size == 0)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_Assignments" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Name",
                "Subject",
                "Class",
                "Deadline",
                "Academic year",
                "Description"
            )
            )

            writer.writeAll(data)
            for (i in 0 until assignmentsList.size)
            {
                writer.writeNext(arrayOf(assignmentsList[i].name , assignmentsList[i].subjectname.name, assignmentsList[i].classname.name + assignmentsList[i].divisionname.name, assignmentsList[i].deadline_date, assignmentsList[i].academic_yearname.name, assignmentsList[i].description ))
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
        assignmentsList.forEach {
            tempString = tempString + "Name----" + it.name + " , " + "Subject----" + it.subjectname.name + " , " + "Class----" + it.classname.name + it.divisionname.name + "Deadline----"+it.deadline_date + "Academic year----"+it.academic_yearname.name + "Description----"+ it.description + "\n"
        }
        clip = ClipData.newPlainText(android.R.attr.label.toString(), tempString)
        clipboard!!.setPrimaryClip(clip!!)

        Toaster.showToast(requireContext(), "Copied to clipboard" , Toaster.State.SUCCESS, Toast.LENGTH_LONG)
    }

    /* Create pdf */
    private fun createPDF() {
        val document = Document(PageSize.A4)
        val path: String = Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_Assignments" + System.currentTimeMillis() + ".pdf"
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
            assignmentsList.forEach {
                val mDataChunk = Chunk("Name----" + it.name + " , " + "Subject----" + it.subjectname.name + " , " + "Class----" + it.classname.name + it.divisionname.name  + " , "+ "Deadline----"+it.deadline_date + " , " + "Academic year----"+it.academic_yearname.name + " , " + "Description----"+ it.description)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_Assignments" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Name",
                "Subject",
                "Class",
                "Deadline",
                "Academic year",
                "Description"
            )
            )

            writer.writeAll(data)
            for (i in 0 until assignmentsList.size)
            {
                writer.writeNext(arrayOf(assignmentsList[i].name , assignmentsList[i].subjectname.name, assignmentsList[i].classname.name + assignmentsList[i].divisionname.name, assignmentsList[i].deadline_date, assignmentsList[i].academic_yearname.name, assignmentsList[i].description ))
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
        val checkedItem = 0
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.select_subject))

            /* Single-choice items (initialized with checked item) */
            .setSingleChoiceItems(catagoryList.toTypedArray(), checkedItem) { dialog, which ->
                /* Respond to item chosen*/
                Log.e("TAG", "setupFilterDialog: "+catagoryList[which] )
                Log.e("TAG", "setupFilterDialog: "+catagoryListId[which] )

                setUpSubjectObserver(catagoryListId[which])
                dialog.dismiss()

            }
            .show()
    }

    /* Filter Observer */
    private fun setUpSubjectObserver(subjectID: Int)
    {
        studentAssignmentsViewModel = StudentAssignmentsViewModel()
        studentAssignmentsViewModel.studentAssignment(subjectID)
        studentAssignmentsViewModel.studentAssignmentData.observe(this, Observer {
            when(it.status)
            {
                Status.SUCCESS ->{

                    dismissProgress()


                    assignmentsList.clear()
                    assignmentsList.addAll(it.data!!.data.assignments)
                    studentAssignmentSideAdapter = StudentAssignmentSideAdapter(assignmentsList){openBrowser(it)}
                    binding.rvStudentAssignment.adapter = studentAssignmentSideAdapter

                    if(assignmentsList.size == 0)
                    {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }

                    catagoryList.clear()
                    catagoryListId.clear()
                    it.data.data.all_subjects.forEach {
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
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_menu, menu)

        val searchItem = menu.findItem(R.id.customtoolbar_search)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search assignments"
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
        studentAssignmentSideAdapter.filter.filter(newText)
        return false
    }

}