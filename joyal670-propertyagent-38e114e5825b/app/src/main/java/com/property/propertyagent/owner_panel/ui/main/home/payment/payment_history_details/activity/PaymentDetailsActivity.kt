package com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history_details.activity

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import android.widget.Toast
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history_details.viewmodel.PaymentHistoryDetailsViewModel
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_payment_details_owner.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class PaymentDetailsActivity : BaseActivity() {
    private var selectedPaymentId: Int? = null
    private var selectedPaymentHistoryId: Int? = null
    private lateinit var paymentHistoryDetailsViewModel: PaymentHistoryDetailsViewModel

    override val layoutId: Int
        get() = R.layout.activity_payment_details_owner
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = "\t\t" + getString(R.string.payment_history) + "\t"
        supportActionBar?.setIcon(R.drawable.ic_credit_card__2_)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        selectedPaymentHistoryId =
            intent.getIntExtra(Constants.SELECTED_PAYMENT_HISTORY_LIST_ID, 0)
        selectedPaymentId = intent.getIntExtra(Constants.SELECTED_PAYMENT_ID, 0)

        paymentHistoryDetailsViewModel.paymentHistoryDetails(
            selectedPaymentHistoryId!!,
            selectedPaymentId!!
        )
    }

    override fun setupUI() {
        payment_details_activity_service.paintFlags =
            payment_details_activity_service.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    override fun setupViewModel() {
        paymentHistoryDetailsViewModel = PaymentHistoryDetailsViewModel()
    }

    override fun setupObserver() {

        /* payment details api */
        paymentHistoryDetailsViewModel.getPaymentHistoryDetailsResponse()
            .observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()

                        tvPaymentDate.text = it.data!!.payment_data.payment_details.date
                        val serviceDetailsData = it.data.payment_data.service_details
                        if (serviceDetailsData.id == 0) {
                            service_layout1.visibility = View.GONE
                            service_layout2.visibility = View.GONE

                            tvMonthRent.text = it.data.payment_data.payment_details.amount
                        } else {
                            month_layout.visibility = View.GONE

                            tvServiceRent.text = it.data.payment_data.payment_details.amount
                            tvServiceName.text =
                                it.data.payment_data.service_details.service_related.service
                            tvServiceAmount.text = it.data.payment_data.payment_details.amount
                            tvServiceDescription.text =
                                it.data.payment_data.service_details.description
                        }

                        tvTotalPrice.text = it.data.payment_data.payment_details.amount

                        val paymentReceivedData =
                            it.data.payment_data.payment_details.payment_received
                        if (paymentReceivedData.id == 0) {
                            paymentBtn.text = getString(R.string.payment_received)
                        } else {
                            paymentBtn.text = getString(R.string.received)
                            paymentBtn.isEnabled = false
                            paymentBtn.isActivated = false
                            paymentBtn.isClickable = false
                        }

                    }
                    Status.LOADING -> {
                        showProgressOwner()
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })

        /* payment receive api */
        paymentHistoryDetailsViewModel.getPaymentReceivedResponse()
            .observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()

                        Toaster.showToast(
                            this,
                            it.data!!.response,
                            Toaster.State.SUCCESS,
                            Toast.LENGTH_LONG
                        )

                        initData()
                    }
                    Status.LOADING -> {
                        showProgressOwner()
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })
    }

    override fun onClicks() {

        /* payment btn */
        paymentBtn.setOnClickListener {
            paymentHistoryDetailsViewModel.paymentReceived(
                selectedPaymentId!!,
                selectedPaymentHistoryId!!
            )
        }
    }
}