package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.library.request_book

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
import com.iroid.jeetmeet.databinding.FragmentStudentRequestBookBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.request_book.StudentRequestBookData
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentRequestBookViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter


class StudentRequestBookFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentStudentRequestBookBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentRequestBookViewModel: StudentRequestBookViewModel
    private lateinit var studentRequestBookAdapter: StudentRequestBookAdapter
    private var bookList = ArrayList<StudentRequestBookData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentRequestBookBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.request_books))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvStudentRequestBook.layoutManager = LinearLayoutManager(context)

        setupSortObserver(0)

    }

    private fun setupSortObserver(i: Int) {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* api call */
        studentRequestBookViewModel = StudentRequestBookViewModel()
        studentRequestBookViewModel.studentRequestBook(i)
        studentRequestBookViewModel.studentRequestBookData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    bookList.clear()
                    bookList.addAll(it.data!!.data)
                    studentRequestBookAdapter = StudentRequestBookAdapter(
                        bookList,
                        { openBrowser(it) },
                        { requestBook(it) },
                        { cancelBook(it) })
                    binding.rvStudentRequestBook.adapter = studentRequestBookAdapter

                    if (bookList.size == 0) {
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

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    /* cancel book api */
    private fun cancelBook(req_id: Int) {
        studentRequestBookViewModel = StudentRequestBookViewModel()
        studentRequestBookViewModel.studentRequestBookCancel(req_id)
        studentRequestBookViewModel.studentRequestBookCancelData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.data!!.messsage,
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

    /* request book api */
    private fun requestBook(bookId: Int) {
        studentRequestBookViewModel = StudentRequestBookViewModel()
        studentRequestBookViewModel.studentRequestBookApply(bookId)
        studentRequestBookViewModel.studentRequestBookApplyData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        "Book requested successfully",
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

    /* open book in browser */
    private fun openBrowser(bookUrl: String) {
        val builder = CustomTabsIntent.Builder()
        val colorInt: Int = Color.parseColor("#EE2424")
        builder.setToolbarColor(colorInt)
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(bookUrl))
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
            if (bookList.size == 0) {
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
            if (bookList.size == 0) {
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
            if (bookList.size == 0) {
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
            if (bookList.size == 0) {
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Xlsx_RequestBook" + System.currentTimeMillis() + ".xls"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Name",
                    "Category",
                    "Author",
                    "Price",
                    "Subject",
                    "Rack No"
                )
            )

            writer.writeAll(data)
            for (i in 0 until bookList.size) {
                writer.writeNext(
                    arrayOf(
                        bookList[i].name,
                        bookList[i].category,
                        bookList[i].author,
                        bookList[i].price.toString(),
                        bookList[i].subject,
                        bookList[i].rack.toString()
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
        bookList.forEach {
            tempString =
                tempString + "Name----" + it.name + " , " + "Category----" + it.category + " , " + "Author----" + it.author + " , " + "Price----" + it.price.toString() + " , " + "Subject----" + it.subject + " , " + "Rack No----" + it.rack.toString() + "\n"
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
            Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_RequestBook" + System.currentTimeMillis() + ".pdf"
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
            bookList.forEach {
                val mDataChunk =
                    Chunk("Name----" + it.name + " , " + "Category----" + it.category + " , " + "Author----" + it.author + " , " + "Price----" + it.price.toString() + " , " + "Subject----" + it.subject + " , " + "Rack No----" + it.rack.toString())
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
                Environment.getExternalStorageDirectory().absolutePath + "/download/Csv_RequestBook" + System.currentTimeMillis() + ".csv"
            writer = CSVWriter(FileWriter(csv))

            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "Name",
                    "Category",
                    "Author",
                    "Price",
                    "Subject",
                    "Rack No"
                )
            )

            writer.writeAll(data)
            for (i in 0 until bookList.size) {
                writer.writeNext(
                    arrayOf(
                        bookList[i].name,
                        bookList[i].category,
                        bookList[i].author,
                        bookList[i].price.toString(),
                        bookList[i].subject,
                        bookList[i].rack.toString()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_menu, menu)

        val searchItem = menu.findItem(R.id.customtoolbar_search)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Books"
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
        studentRequestBookAdapter.filter.filter(newText)
        return false
    }

}