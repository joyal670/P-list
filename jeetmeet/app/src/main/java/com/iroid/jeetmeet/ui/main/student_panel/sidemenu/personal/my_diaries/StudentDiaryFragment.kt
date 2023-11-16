package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.my_diaries

import android.Manifest
import android.R.attr.label
import android.app.AlertDialog
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentDiaryBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.diaries.StudentDiariesData
import com.iroid.jeetmeet.modal.student.diaries_edit.StudentDiariesEditData
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentDiariesViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StudentDiaryFragment : BaseFragment(), SearchView.OnQueryTextListener
{
    private lateinit var binding: FragmentStudentDiaryBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentDiariesViewModel : StudentDiariesViewModel
    private var diariesList = ArrayList<StudentDiariesData>()
    private lateinit var studentDiaryAdapter : StudentDiaryAdapter

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentDiaryBinding.inflate(inflater!!,container,false)
        return binding.root    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.diary))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvStudentDiary.layoutManager = LinearLayoutManager(context)

        setupSortObserver(0)
    }

    private fun setupSortObserver(sort: Int) {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call */
        studentDiariesViewModel = StudentDiariesViewModel()
        studentDiariesViewModel.studentDiaries(sort)
        studentDiariesViewModel.studentDiariesData.observe(this, Observer {
            when(it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    diariesList.clear()
                    diariesList.addAll(it.data!!.data)
                    studentDiaryAdapter = StudentDiaryAdapter(diariesList ,{deleteDiaries(it)}, {updateDiaries(it)})
                    binding.rvStudentDiary.adapter = studentDiaryAdapter

                    if(diariesList.size == 0)
                    {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.LOADING ->{
                    showProgress()
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

    /* Update Diaries */
    private fun updateDiaries(id: Int) {

        studentDiariesViewModel = StudentDiariesViewModel()
        studentDiariesViewModel.studentDiariesEdit(id)
        studentDiariesViewModel.studentDiariesEditData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    setUpEditDiariesDialog(it.data!!.data)
                }
                Status.LOADING ->{
                    showProgress()
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

    /* edit diaries dialog */
    private fun setUpEditDiariesDialog(data: StudentDiariesEditData) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fragment_student_edit_diaries, null)
        builder.setView(dialogView)
        val diag: AlertDialog = builder.create()
        diag.show()
        diag.setCancelable(false)
        diag.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textView = dialogView.findViewById<MaterialTextView>(R.id.materialTextView4)
        val dateBtn = dialogView.findViewById<EditText>(R.id.textInputLayout)
        val noteEditText = dialogView.findViewById<EditText>(R.id.editText)
        val closeButton = dialogView.findViewById<ImageView>(R.id.closeImageView)
        val submitBtn = dialogView.findViewById<MaterialButton>(R.id.submitBtn)

        textView.text = "Edit Diaries"

        dateBtn.setText(data.date)
        noteEditText.setText(data.note)

        closeButton.setOnClickListener {
            diag.dismiss()
        }

        dateBtn.setOnClickListener {
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
                dateBtn.setText(outputStartDate)

            }
            datePicker.show(parentFragmentManager, "MyTAG")
        }

        submitBtn.setOnClickListener {
            val tempDate = dateBtn.text.toString()
            val tempNote = noteEditText.text.toString()

            if(tempDate.isBlank() || tempNote.isBlank())
            {
                Toaster.showToast(requireContext(), "All fields are required", Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else
            {
                setupUpDateDiariesObserver(data.id, tempDate, tempNote)
                diag.dismiss()
            }
        }
    }

    /* Update diaries api */
    private fun setupUpDateDiariesObserver(id : Int, tempDate: String, tempNote: String) {
        studentDiariesViewModel = StudentDiariesViewModel()
        studentDiariesViewModel.studentDiariesUpdate(id, tempNote, tempDate)
        studentDiariesViewModel.studentDiariesUpdateData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    Toaster.showToast(requireContext(), it.data!!.message, Toaster.State.SUCCESS, Toast.LENGTH_LONG)

                    setupSortObserver(0)
                }
                Status.LOADING ->{
                    showProgress()
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

    /* Delete diaries api */
    private fun deleteDiaries(id: Int) {
        studentDiariesViewModel = StudentDiariesViewModel()
        studentDiariesViewModel.studentDiariesDelete(id)
        studentDiariesViewModel.studentDiariesDeleteData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    Toaster.showToast(requireContext(), it.data!!.message, Toaster.State.SUCCESS, Toast.LENGTH_LONG)

                    setupSortObserver(0)
                }
                Status.LOADING ->{
                    showProgress()
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

    override fun onClicks() {

        /* filter option */
        binding.filter.setOnClickListener {
            setupFilterDialog()
        }

        /* add diaries */
        binding.btnAddDiaries.setOnClickListener {
            setUpAddDiariesDialog()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupSortObserver(0)
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if(diariesList.size == 0)
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
            if(diariesList.size == 0)
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
            if(diariesList.size == 0)
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
            if(diariesList.size == 0)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_Diaries" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Id",
                "Date",
                "Note"
            )
            )

            writer.writeAll(data)
            for (i in 0 until diariesList.size)
            {
                writer.writeNext(arrayOf(diariesList[i].id.toString(), diariesList[i].date, diariesList[i].note ))
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
        val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
        var clip : ClipData? = null
        var tempString = ""
        diariesList.forEach {
            tempString = tempString + "ID----" + it.id + " , " + "Date----" + it.date + " , " + "Note----" + it.note + "\n"
        }
        clip = ClipData.newPlainText(label.toString(), tempString)
        clipboard!!.setPrimaryClip(clip!!)

        Toaster.showToast(requireContext(), "Copied to clipboard" , Toaster.State.SUCCESS, Toast.LENGTH_LONG)
    }

    /* Create pdf */
    private fun createPDF() {
        val document = Document(PageSize.A4)
        val path: String = Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_Diaries" + System.currentTimeMillis() + ".pdf"
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
            diariesList.forEach {
                val mDataChunk = Chunk("ID-----"+ it.id + " , " + "Date-----" + it.date + " , " + "Note-----" + it.note)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_Diaries" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Id",
                "Date",
                "Note"
            )
            )

            writer.writeAll(data)
            for (i in 0 until diariesList.size)
            {
                writer.writeNext(arrayOf(diariesList[i].id.toString(), diariesList[i].date, diariesList[i].note ))
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

    /* Add diaries dialog */
    private fun setUpAddDiariesDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fragment_student_edit_diaries, null)
        builder.setView(dialogView)
        val diag: AlertDialog = builder.create()
        diag.show()
        diag.setCancelable(false)
        diag.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textView = dialogView.findViewById<MaterialTextView>(R.id.materialTextView4)
        val dateBtn = dialogView.findViewById<EditText>(R.id.textInputLayout)
        val noteEditText = dialogView.findViewById<EditText>(R.id.editText)
        val closeButton = dialogView.findViewById<ImageView>(R.id.closeImageView)
        val submitBtn = dialogView.findViewById<MaterialButton>(R.id.submitBtn)

        textView.text = "Add Diaries"

        closeButton.setOnClickListener {
            diag.dismiss()
        }

        dateBtn.setOnClickListener {
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
                dateBtn.setText(outputStartDate)

            }
            datePicker.show(parentFragmentManager, "MyTAG")
        }

        submitBtn.setOnClickListener {
            val tempDate = dateBtn.text.toString()
            val tempNote = noteEditText.text.toString()

            if(tempDate.isBlank() || tempNote.isBlank())
            {
                Toaster.showToast(requireContext(), "All fields are required", Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else
            {
                setupAddDiariesObserver(tempDate, tempNote)
                diag.dismiss()
            }
        }
    }

    /* Add diaries api */
    private fun setupAddDiariesObserver(tempDate: String, tempNote: String) {
        studentDiariesViewModel = StudentDiariesViewModel()
        studentDiariesViewModel.studentDiariesSave(tempNote, tempDate)
        studentDiariesViewModel.studentDiariesSaveData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    Toaster.showToast(requireContext(), it.data!!.message, Toaster.State.SUCCESS, Toast.LENGTH_LONG)

                    setupSortObserver(0)

                }
                Status.LOADING ->{
                    showProgress()
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

    /* setup filter dialog */
    private fun setupFilterDialog() {
        val singleItems = arrayOf("Early applied", "Last applied")
        val checkedItem = 0
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.select_subject))

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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_menu, menu)

        val searchItem = menu.findItem(R.id.customtoolbar_search)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Diary"
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = true
        searchView.isIconifiedByDefault = true
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu)
    {
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
        studentDiaryAdapter.filter.filter(newText.toString())
        return false
    }
}