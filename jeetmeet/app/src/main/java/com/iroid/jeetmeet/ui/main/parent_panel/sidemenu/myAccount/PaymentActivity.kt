package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.myAccount

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseActivity
import com.iroid.jeetmeet.databinding.ActivityParentDashboardBinding
import com.iroid.jeetmeet.modal.parent.my_account_payment.ParentMyAccountPaymentResponse
import com.iroid.jeetmeet.ui.main.parent_panel.home.activity.ParentDashboardActivity
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.toolbar_main.*
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.math.roundToInt

class PaymentActivity : BaseActivity<ActivityParentDashboardBinding>(), PaymentResultListener {

    private lateinit var parentMyAccountViewModel: ParentMyAccountViewModel
    private var amount = ""
    private var totalAmount = ""
    private var studentCodeApi = ""
    private var advanceAmount = ""
    private var advanceBalance = ""
    private var intList = ArrayList<Int>()


    override val layoutId: Int
        get() = R.layout.activity_parent_dashboard

    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityParentDashboardBinding =
        ActivityParentDashboardBinding.inflate(layoutInflater)

    override fun initData() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_grey)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        amount = intent.getStringExtra("amount")!!
        studentCodeApi = intent.getStringExtra("student_code")!!
        advanceAmount = intent.getStringExtra("advance")!!
        advanceBalance = intent.getStringExtra("advance_balance")!!
        intList = intent.getIntegerArrayListExtra("posting_id")!!

        Log.e("TAG", "initData: $amount")
        Log.e("TAG", "initData: $studentCodeApi")
        Log.e("TAG", "initData: $advanceAmount")
        Log.e("TAG", "initData: $advanceBalance")
        Log.e("TAG", "initData: $intList")

        /* convert amount to paisa */
        totalAmount = (amount.toFloat() * 100).roundToInt().toString()
        startPayment()


    }


    private fun startPayment() {
        val activity: Activity = this
        val co = Checkout()
        // rzp_test_4IatxTElDWxLLa
        co.setKeyID("rzp_test_4IatxTElDWxLLa")

        try {
            val options = JSONObject()
            options.put("name", "Razorpay Corp")
            options.put("description", "Total Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR")
            options.put("amount", totalAmount)//pass amount in currency subunits

            /* val prefill = JSONObject()
             prefill.put("email", "gaurav.kumar@example.com")
             prefill.put("contact", "9876543210")

             options.put("prefill", prefill)*/
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        parentMyAccountViewModel = ParentMyAccountViewModel()
        parentMyAccountViewModel.parentMyAccountPayment(
            studentCodeApi,
            amount,
            advanceAmount,
            advanceBalance,
            p0.toString(),
            intList
        )
        parentMyAccountViewModel.parentMyAccountPaymentData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        "Payment successful",
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                    createPDF(it.data)
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
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    private fun createPDF(data: ParentMyAccountPaymentResponse?) {
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

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("Payment details File successfully saved to internal storage")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
            }
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                Checkout.clearUserData(this)
                startActivity(Intent(this, ParentDashboardActivity::class.java))
                finishAffinity()
            }
        val alert = builder.create()
        alert.show()

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toaster.showToast(
            this,
            p1.toString(),
            Toaster.State.ERROR,
            Toast.LENGTH_LONG
        )
        finish()
        Checkout.clearUserData(this)
    }

    override fun fragmentLaunch() {
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {
    }

}