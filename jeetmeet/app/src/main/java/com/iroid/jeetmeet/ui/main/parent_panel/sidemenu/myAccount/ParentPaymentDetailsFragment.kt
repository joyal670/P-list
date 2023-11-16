package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.myAccount

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.textview.MaterialTextView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentPaymentDetailsBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.my_account_debit_from_advance.ParentMyaccountDebitFromAdvanceResponse
import com.iroid.jeetmeet.modal.parent.my_account_partial_pay.ParentMyAccountPartialPayResponse
import com.iroid.jeetmeet.ui.main.parent_panel.home.activity.ParentDashboardActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import java.io.FileNotFoundException
import java.io.FileOutputStream


class ParentPaymentDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentParentPaymentDetailsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentMyAccountViewModel: ParentMyAccountViewModel
    private val intList = ArrayList<Int>()
    private var studentCodeApi = ""
    private var advanceAmount = ""
    private var depositAmount = ""
    private var advanceBalance = ""
    private var total = ""
    private var subTotal = ""

    companion object {
        private var studentCode: String? = null

        @JvmStatic
        fun newInstance(id: String) =
            ParentPaymentDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            studentCode = it.getString("id")
        }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentPaymentDetailsBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.myAccount))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        binding.rvPayment.layoutManager = LinearLayoutManager(context)
        setupObserver()

    }

    override fun setupViewModel() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        parentMyAccountViewModel = ParentMyAccountViewModel()
        parentMyAccountViewModel.parentMyAccountDetails(studentCode!!)
        parentMyAccountViewModel.parentMyAccountDetailsData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvAdvance).text =
                        ":" + " " + it.data!!.advance
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvDeposit).text =
                        ":" + " " + it.data.deposit.toString()
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvAdvanceBalance).text =
                        it.data.advance_balance.toString()
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvTotal).text =
                        it.data.total.toString()
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvSubTotal).text =
                        it.data.sub_total.toString()

                    binding.rvPayment.adapter =
                        ParentPaymentDetailsAdapter(requireActivity(), it.data.fee)

                    if (it.data.fee.isEmpty()) {
                        binding.payNowBtn.visibility = View.GONE
                        binding.payLater.visibility = View.GONE
                    } else {
                        binding.payNowBtn.visibility = View.VISIBLE
                        binding.payLater.visibility = View.VISIBLE
                    }
                    /* Button's
                     1 - pay now,
                     2 - debit from advance
                     3 - pay now, pay later*/

                    when (it.data.button_key) {
                        1 -> {
                            binding.payNowBtn.visibility = View.VISIBLE
                            binding.payLater.visibility = View.GONE
                            binding.payFromAdvanceBtn.visibility = View.GONE
                        }
                        2 -> {
                            binding.payNowBtn.visibility = View.GONE
                            binding.payLater.visibility = View.GONE
                            binding.payFromAdvanceBtn.visibility = View.VISIBLE
                        }
                        3 -> {
                            binding.payNowBtn.visibility = View.VISIBLE
                            binding.payLater.visibility = View.VISIBLE
                            binding.payFromAdvanceBtn.visibility = View.GONE

                            binding.payLater.text = "Pay later"
                        }
                    }

                    intList.clear()
                    it.data.posting_ids.forEach { data ->
                        intList.add(data)
                    }

                    studentCodeApi = it.data.student_code
                    advanceAmount = it.data.advance
                    depositAmount = it.data.deposit.toString()
                    advanceBalance = it.data.advance_balance.toString()
                    total = it.data.total.toString()
                    subTotal = it.data.sub_total.toString()


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

    override fun onClicks() {
        /* expand and collapse */
        var clicked = false
        binding.expandable.parentLayout.setOnClickListener {
            if (!clicked) {
                binding.expandable.expand()
                clicked = true
            } else {
                binding.expandable.collapse()
                clicked = false
            }
        }

        /* pay now  */
        binding.payNowBtn.setOnClickListener {
            val amount =
                binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvSubTotal).text.toString()
            val intent = Intent(requireActivity(), PaymentActivity::class.java)
            intent.putExtra("amount", amount)
            intent.putExtra("student_code", studentCodeApi)
            intent.putExtra("advance", advanceAmount)
            intent.putExtra("advance_balance", advanceBalance)
            intent.putIntegerArrayListExtra("posting_id", intList)
            startActivity(intent)
        }

        /* Partially Pay */
        binding.payLater.setOnClickListener {
            setUpPayPartialObserver()
        }

        /* Debit from advance */
        binding.payFromAdvanceBtn.setOnClickListener {
            setUpDebitFromAdvanceObserver()
        }
    }

    /* My Account Debit from Advance */
    private fun setUpDebitFromAdvanceObserver() {
        val subTotal =
            binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvSubTotal).text.toString()
        val advance =
            binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvAdvance).text.toString()
        val advanceBalance =
            binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvAdvanceBalance).text.toString()
        val total =
            binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvTotal).text.toString()

        parentMyAccountViewModel = ParentMyAccountViewModel()
        parentMyAccountViewModel.parentMyAccountDebitFromAdvance(
            studentCodeApi,
            total,
            advanceAmount,
            advanceBalance,
            intList
        )

        parentMyAccountViewModel.parentMyAccountDebitFromAdvanceData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    permissionsBuilder(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).build().send { result ->
                        if (result.allGranted()) {
                            createPDFPayment(it.data)
                        }
                    }
                }
                Status.LOADING -> {
                    showProgress()
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

    private fun createPDFPayment(data: ParentMyaccountDebitFromAdvanceResponse?) {
        val document = Document(PageSize.A4)
        val path: String =
            Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_PaymentDetails" + System.currentTimeMillis() + ".pdf"
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

        val mTitleChunk1 = Chunk("Payment Details")
        val mParagraphChunk1 = Paragraph(mTitleChunk1)
        mParagraphChunk1.alignment = Element.ALIGN_CENTER
        document.add(mParagraphChunk1)
        document.add(Chunk(lineSeparator))

        val mTitleChunk2 = Chunk("Student Details")
        val mParagraphChunk2 = Paragraph(mTitleChunk2)
        mParagraphChunk2.alignment = Element.ALIGN_LEFT
        document.add(mParagraphChunk2)
        document.add(Paragraph(""))

        try {

            val mDataChunk1 =
                Chunk("Student Name----" + data!!.student.student_name + " , " + "Student Reg.No----" + data.student.reg_number)
            val mDetailsChunk1 = Paragraph(mDataChunk1)
            document.add(mDetailsChunk1)
            document.add(Paragraph(""))

            val mDataChunk2 =
                Chunk("Email----" + data.student.email + " , " + "Phone No----" + data.student.phone)
            val mDetailsChunk2 = Paragraph(mDataChunk2)
            document.add(mDetailsChunk2)
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val mTitleChunk3 = Chunk("Payment Details")
            val mParagraphChunk3 = Paragraph(mTitleChunk3)
            mParagraphChunk3.alignment = Element.ALIGN_LEFT
            document.add(mParagraphChunk3)

            val mDataChunk3 =
                Chunk("Amount----" + data.payment.amount + " , " + "Discount----" + data.payment.discount)
            val mDetailsChunk3 = Paragraph(mDataChunk3)
            document.add(mDetailsChunk3)
            document.add(Paragraph(""))

            val mDataChunk4 =
                Chunk("Advance deduction----" + data.payment.advance_deduction + " , " + "Paid amount----" + data.payment.paid_amount)
            val mDetailsChunk4 = Paragraph(mDataChunk4)
            document.add(mDetailsChunk4)
            document.add(Paragraph(""))

            val mDataChunk5 =
                Chunk("Transaction ID----" + data.payment.transaction_id)
            val mDetailsChunk5 = Paragraph(mDataChunk5)
            document.add(mDetailsChunk5)
            document.add(Paragraph(""))

            data.fee_details.forEach {
                val mDataChunk6 =
                    Chunk("Amount----" + it.amount + " , " + "Discount----" + it.discount + " , " + "Name----" + it.name)
                val mDetailsChunk6 = Paragraph(mDataChunk6)
                document.add(mDetailsChunk6)
                document.add(Paragraph(""))
            }

        } catch (ex: Exception) {

        }

        if (document.isOpen) {
            document.close()
        }

        /* val builder = AlertDialog.Builder(requireContext())
         builder.setTitle("Success")
             .setMessage("Payment details File successfully saved to internal storage")
             .setIcon(android.R.drawable.ic_dialog_alert)
             .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, whichButton: Int ->
             }
             .setPositiveButton(android.R.string.ok,dialog:DialogInterface{

             })
             .show()
         */

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Success")
        builder.setMessage("Payment details File successfully saved to internal storage")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
            }
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                startActivity(Intent(requireContext(), ParentDashboardActivity::class.java))
                requireActivity().finishAffinity()
            }
        val alert = builder.create()
        alert.show()


    }

    /* Pay Partially Observer */
    private fun setUpPayPartialObserver() {
        val subTotal =
            binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvSubTotal).text.toString()
        val total =
            binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvTotal).text.toString()
        val advanceBalance =
            binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvAdvanceBalance).text.toString()

        parentMyAccountViewModel = ParentMyAccountViewModel()
        parentMyAccountViewModel.parentMyAccountPayPartially(
            studentCodeApi,
            total,
            advanceAmount,
            advanceBalance,
            intList
        )
        parentMyAccountViewModel.parentMyAccountPayPartiallyData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    permissionsBuilder(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).build().send { result ->
                        if (result.allGranted()) {
                            createPDF(it.data)
                        }
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

    private fun createPDF(data: ParentMyAccountPartialPayResponse?) {
        val document = Document(PageSize.A4)
        val path: String =
            Environment.getExternalStorageDirectory().absolutePath + "/download/PDF_PaymentDetails" + System.currentTimeMillis() + ".pdf"
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

        val mTitleChunk1 = Chunk("Payment Details")
        val mParagraphChunk1 = Paragraph(mTitleChunk1)
        mParagraphChunk1.alignment = Element.ALIGN_CENTER
        document.add(mParagraphChunk1)
        document.add(Chunk(lineSeparator))

        val mTitleChunk2 = Chunk("Student Details")
        val mParagraphChunk2 = Paragraph(mTitleChunk2)
        mParagraphChunk2.alignment = Element.ALIGN_LEFT
        document.add(mParagraphChunk2)
        document.add(Paragraph(""))

        try {

            val mDataChunk1 =
                Chunk("Student Name----" + data!!.student.student_name + " , " + "Student Reg.No----" + data.student.reg_number)
            val mDetailsChunk1 = Paragraph(mDataChunk1)
            document.add(mDetailsChunk1)
            document.add(Paragraph(""))

            val mDataChunk2 =
                Chunk("Email----" + data.student.email + " , " + "Phone No----" + data.student.phone)
            val mDetailsChunk2 = Paragraph(mDataChunk2)
            document.add(mDetailsChunk2)
            document.add(Paragraph(""))

            val mTitleChunk3 = Chunk("Payment Details")
            val mParagraphChunk3 = Paragraph(mTitleChunk3)
            mParagraphChunk3.alignment = Element.ALIGN_LEFT
            document.add(mParagraphChunk3)

            val mDataChunk3 =
                Chunk("Amount----" + data.payment.amount + " , " + "Discount----" + data.payment.discount)
            val mDetailsChunk3 = Paragraph(mDataChunk3)
            document.add(mDetailsChunk3)
            document.add(Paragraph(""))

            val mDataChunk4 =
                Chunk("Advance deduction----" + data.payment.advance_deduction + " , " + "Paid amount----" + data.payment.paid_amount)
            val mDetailsChunk4 = Paragraph(mDataChunk4)
            document.add(mDetailsChunk4)
            document.add(Paragraph(""))

            val mDataChunk5 =
                Chunk("Transaction ID----" + data.payment.transaction_id + " , " + "Pending amount----" + data.payment.pending_amount)
            val mDetailsChunk5 = Paragraph(mDataChunk5)
            document.add(mDetailsChunk5)
            document.add(Paragraph(""))

            data.fee_details.forEach {
                val mDataChunk6 =
                    Chunk("Amount----" + it.amount + " , " + "Discount----" + it.discount + " , " + "Name----" + it.name)
                val mDetailsChunk6 = Paragraph(mDataChunk6)
                document.add(mDetailsChunk6)
                document.add(Paragraph(""))
            }

        } catch (ex: Exception) {

        }

        if (document.isOpen) {
            document.close()
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Success")
        builder.setMessage("Payment details File successfully saved to internal storage")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
            }
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                startActivity(Intent(requireContext(), ParentDashboardActivity::class.java))
                requireActivity().finishAffinity()
            }
        val alert = builder.create()
        alert.show()
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }


}