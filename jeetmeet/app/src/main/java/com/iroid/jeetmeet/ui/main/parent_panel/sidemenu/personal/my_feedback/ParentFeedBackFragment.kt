package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.my_feedback

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentFeedbackBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.feedbacks.ParentFeedbacksData
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentFeedbackViewModel
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


class ParentFeedBackFragment : BaseFragment() {
    private lateinit var binding: FragmentParentFeedbackBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentFeedbackViewModel: ParentFeedbackViewModel
    private var feedbackList = ArrayList<ParentFeedbacksData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentFeedbackBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.my_feedback))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvParentFeedBack.layoutManager = LinearLayoutManager(context)

        setupSortObserver(0)
    }

    private fun setupSortObserver(sort: Int) {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* feedback api */
        parentFeedbackViewModel = ParentFeedbackViewModel()
        parentFeedbackViewModel.parentFeedbacks(sort)
        parentFeedbackViewModel.parentFeedbackData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{

                    dismissProgress()

                    feedbackList.clear()
                    feedbackList.addAll(it.data!!.data)
                    binding.rvParentFeedBack.adapter = ParentFeedBackAdapter(feedbackList, {editFeedback(it)}, {deleteFeedback(it)})

                    if(feedbackList.size == 0)
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
        }
        )
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    /* edit feedback */
    private fun editFeedback(id: Int) {

        parentFeedbackViewModel = ParentFeedbackViewModel()
        parentFeedbackViewModel.parentFeedbackEdit(id)
        parentFeedbackViewModel.parentFeedbackEditData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{

                    dismissProgress()

                    setupFeedbackEditDialog(it.data!!.data.id, it.data.data.date, it.data.data.note)
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
            }
        })
    }

    /* edit feedback dialog */
    private fun setupFeedbackEditDialog(id: Int, date: String, note: String) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fragment_parent_edit_feedback, null)
        builder.setView(dialogView)
        val diag: AlertDialog = builder.create()
        diag.show()
        diag.setCancelable(false)
        diag.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvDate = dialogView.findViewById<EditText>(R.id.tvDate)
        val tvNote = dialogView.findViewById<EditText>(R.id.tvNote)
        val submitBtn = dialogView.findViewById<MaterialButton>(R.id.submitBtn)
        val closeButton = dialogView.findViewById<ImageView>(R.id.closeImageView)

        tvDate.setText(date)
        tvNote.setText(note)

        tvDate.setOnClickListener {
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
                tvDate.setText(outputStartDate)

            }
            datePicker.show(parentFragmentManager, "MyTAG")
        }

        closeButton.setOnClickListener {
            diag.dismiss()
        }

        submitBtn.setOnClickListener {
            val tempDate = tvDate.text.toString()
            val tempNote = tvNote.text.toString()

            if(tempNote.isBlank())
            {
                tvNote.error = "Required"
            }
            else
            {
                setupFeedbackSaveObserver(id, tempDate, tempNote)
                diag.dismiss()
            }
        }
    }

    /* save edited feedback */
    private fun setupFeedbackSaveObserver(id: Int, tempDate: String, tempNote: String) {

        parentFeedbackViewModel = ParentFeedbackViewModel()
        parentFeedbackViewModel.parentFeedbackUpdate(id,tempDate,tempNote)
        parentFeedbackViewModel.parentFeedbackUpdateData.observe(this, Observer {
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

    /* delete feedback */
    private fun deleteFeedback(id: Int) {

        parentFeedbackViewModel = ParentFeedbackViewModel()
        parentFeedbackViewModel.parentFeedbackDelete(id)
        parentFeedbackViewModel.parentFeedbackDeleteData.observe(this, Observer {
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

    override fun onClicks() {

        /* add feedback */
        binding.btnAddFeedBack.setOnClickListener {
            appCompatActivity?.replaceFragment(
                fragment = ParentAddFeedbackFragment(),
                addToBackStack = false
            )
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupSortObserver(0)
        }

        /* filter */
        binding.filter.setOnClickListener {
            setupFilterDialog()
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if(feedbackList.size == 0)
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
            if(feedbackList.size == 0)
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
            if(feedbackList.size == 0)
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
            if(feedbackList.size == 0)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_Feedback" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Date",
                "Note"
            )
            )

            writer.writeAll(data)
            for (i in 0 until feedbackList.size)
            {
                writer.writeNext(arrayOf(feedbackList[i].date, feedbackList[i].note ))
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
        feedbackList.forEach {
            tempString = tempString +  "Date----" + it.date + " , " + "Note----" + it.note + "\n"
        }
        clip = ClipData.newPlainText(android.R.attr.label.toString(), tempString)
        clipboard!!.setPrimaryClip(clip!!)

        Toaster.showToast(requireContext(), "Copied to clipboard" , Toaster.State.SUCCESS, Toast.LENGTH_LONG)
    }

    /* Create pdf */
    private fun createPDF() {
        val document = Document(PageSize.A4)
        val path: String = Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_Feedback" + System.currentTimeMillis() + ".pdf"
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
            feedbackList.forEach {
                val mDataChunk = Chunk( "Date-----" + it.date + " , " + "Note-----" + it.note)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_Feedback" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "Date",
                "Note"
            )
            )

            writer.writeAll(data)
            for (i in 0 until feedbackList.size)
            {
                writer.writeNext(arrayOf(feedbackList[i].date, feedbackList[i].note ))
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

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }


}