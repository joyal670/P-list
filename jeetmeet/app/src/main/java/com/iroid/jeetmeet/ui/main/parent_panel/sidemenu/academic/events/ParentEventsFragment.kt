package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.events

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentEventsBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.events.new.ParentEventsNewData
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentEventsViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter


class ParentEventsFragment : BaseFragment() {
    private lateinit var binding: FragmentParentEventsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentEventsViewModel: ParentEventsViewModel
    private var eventsList = ArrayList<ParentEventsNewData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentEventsBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.events))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvParentEvent.layoutManager = LinearLayoutManager(context)

        setupSortObserver(0)
    }

    private fun setupSortObserver(sort: Int) {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* events api */
        parentEventsViewModel = ParentEventsViewModel()
        parentEventsViewModel.parentEvents(sort)
        parentEventsViewModel.parentEventsData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    eventsList.clear()
                    eventsList.addAll(it.data!!.data)
                    binding.rvParentEvent.adapter = ParentEventAdapter(eventsList)

                    if (eventsList.size == 0) {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }
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

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }

        /* filter */
        binding.filter.setOnClickListener {
            setupFilterDialog()
        }

        /* CSV btn */
        binding.tvCSV.setOnClickListener {
            if (eventsList.size == 0) {
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
            if (eventsList.size == 0) {
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
            if (eventsList.size == 0) {
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
            if (eventsList.size == 0) {
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_Events" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Name",
                    "Type",
                    "Date",
                    "Exp Date",
                    "Class & Division",
                    "Time"
                )
            )

            writer.writeAll(data)
            for (i in 0 until eventsList.size) {
                writer.writeNext(
                    arrayOf(
                        eventsList[i].title,
                        eventsList[i].type.toString(),
                        eventsList[i].date,
                        eventsList[i].ex_date,
                        eventsList[i].`class`.toString() + eventsList[i].divisionName.toString(),
                        eventsList[i].time
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
        eventsList.forEach {
            tempString =
                tempString + "Name----" + it.title + " , " + "Type----" + it.type + " , " + "Date----" + it.date + " , " + "Exp Date----" + it.ex_date + " , " + "Class & Division----" + it.`class`.toString() + it.divisionName.toString() + " , " + "Time----" + it.time + "\n"
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
            Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_Events" + System.currentTimeMillis() + ".pdf"
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
            eventsList.forEach {
                val mDataChunk =
                    Chunk("Name----" + it.title + " , " + "Type----" + it.type + " , " + "Date----" + it.date + " , " + "Exp Date----" + it.ex_date + " , " + "Class & Division----" + it.`class`.toString() + it.divisionName.toString() + " , " + "Time----" + it.time)
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_Events" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Name",
                    "Type",
                    "Date",
                    "Exp Date",
                    "Class & Division",
                    "Time"
                )
            )

            writer.writeAll(data)
            for (i in 0 until eventsList.size) {
                writer.writeNext(
                    arrayOf(
                        eventsList[i].title,
                        eventsList[i].type.toString(),
                        eventsList[i].date,
                        eventsList[i].ex_date,
                        eventsList[i].`class`.toString() + eventsList[i].divisionName.toString(),
                        eventsList[i].time
                    )
                )
            }

            writer.close()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Success")
                .setMessage("CSV File successfully saved to internal storage")
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
                setupSortObserver(which)
            }
            .show()
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