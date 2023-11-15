package com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_payment_history_list.OwnerPaymentHistoryListPayment
import com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history.adapter.IndividualPaymentHistoryOwnerAdapter
import com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history.viewmodel.PropertyPaymentDetailsViewModel
import com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history_details.activity.PaymentDetailsActivity
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.Constants.SELECTED_PAYMENT_ID
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_payment_history.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class PaymentHistoryActivity : BaseActivity() {
    private var selectedPaymentId: Int? = null
    private lateinit var propertyPaymentDetailsViewModel: PropertyPaymentDetailsViewModel
    private lateinit var individualPaymentHistoryOwnerAdapter: IndividualPaymentHistoryOwnerAdapter
    private var paymentHistoryList = ArrayList<OwnerPaymentHistoryListPayment>()
    private var page: Int = 1
    private var totalPages: Int = 0

    private var isLoading = false

    override val layoutId: Int
        get() = R.layout.activity_payment_history
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = "\t\t" + getString(R.string.payment) + "\t"
        supportActionBar?.setIcon(R.drawable.ic_credit_card__2_)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        selectedPaymentId = intent.getIntExtra(SELECTED_PAYMENT_ID, 0)

        propertyPaymentDetailsViewModel.paymentListOfProperties(selectedPaymentId!!)
    }

    private fun setRecyclerView() {
        indivigual_payment_history_list_item_recycerview.layoutManager = LinearLayoutManager(this)
        individualPaymentHistoryOwnerAdapter =
            IndividualPaymentHistoryOwnerAdapter(this, paymentHistoryList) { selectedID(it) }
        indivigual_payment_history_list_item_recycerview.adapter =
            individualPaymentHistoryOwnerAdapter

        indivigual_payment_history_list_item_recycerview.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    private fun selectedID(id: Int) {
        val intent = Intent(this, PaymentDetailsActivity::class.java)
        intent.putExtra(Constants.SELECTED_PAYMENT_HISTORY_LIST_ID, id)
        intent.putExtra(Constants.SELECTED_PAYMENT_ID, selectedPaymentId)
        startActivity(intent)
    }

    fun loadData() {
        if (page <= totalPages) {
            paymentHistoryList.add(OwnerPaymentHistoryListPayment("", -1, "", -1, -1))
            individualPaymentHistoryOwnerAdapter.notifyItemInserted(paymentHistoryList.size - 1)
            propertyPaymentDetailsViewModel.paymentHistoryListProperties(
                selectedPaymentId!!,
                page.toString()
            )
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        propertyPaymentDetailsViewModel = PropertyPaymentDetailsViewModel()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun setupObserver() {

        propertyPaymentDetailsViewModel.getOwnerPaymentResponse()
            .observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data!!.payment_data.property_details != null) {
                            Glide.with(this)
                                .load(it.data.payment_data.property_details.property_priority_image.document)
                                .into(ivPropertyImage)
                            tvPropertyName.text =
                                it.data.payment_data.property_details.property_name
                            tvPropertyCode.text =
                                it.data.payment_data.property_details.property_reg_no

                            try {
                                tvPropertyLocation.text =
                                    CommonUtils.getAddress(
                                        it.data.payment_data.property_details.latitude,
                                        it.data.payment_data.property_details.longitude,
                                        this
                                    )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }


                            if (it.data.payment_data.property_details.property_to == 0) {
                                tvPropertyType.text = getString(R.string.apartment_rent)
                                tvPropertyAmount.text =
                                    getString(R.string.sarValue) + " " + it.data.payment_data.property_details.rent
                                tvPropertyPeriod.text =
                                    it.data.payment_data.property_details.property_rent_frequency.type
                            } else {
                                tvPropertyType.text = getString(R.string.apartment_sale)
                                tvPropertyAmount.text =
                                    getString(R.string.sarValue) + " " + it.data.payment_data.property_details.selling_price
                                rent_layout.visibility = View.GONE
                                paymentPeroid.visibility = View.GONE
                            }

                            if (it.data.payment_data.property_details.first_owner_payament.id == 0) {
                                rent_layout.visibility = View.GONE
                            } else {
                                tvStartDate.text =
                                    it.data.payment_data.property_details.first_owner_payament.date
                            }

                            if (it.data.payment_data.property_details.last_owner_payament.id == 0) {
                                last_payment_layout.visibility = View.GONE
                            } else {
                                tvEndDate.text =
                                    it.data.payment_data.property_details.last_owner_payament.date
                            }

                            propertyPaymentDetailsViewModel.paymentHistoryListProperties(
                                selectedPaymentId!!,
                                page.toString()
                            )
                        } else {
                            llEmptyDataNotification.isVisible = true
                            container.isVisible = false
                            dismissProgressOwner()
                        }
                    }
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgressOwner()
                            isLoading = true
                        }
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

        /* payment history api */
        propertyPaymentDetailsViewModel.getOwnerPaymentHistoryListResponse()
            .observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()
                        container.isVisible = true
                        isLoading = false
                        if (it.data!!.property_payment.payments != null) {
                            totalPages = it.data.property_payment.total_page_count
                            if (paymentHistoryList.size != 0) {
                                isLoading = false
                                page += 1
                                paymentHistoryList.removeAt(paymentHistoryList.size - 1)
                                individualPaymentHistoryOwnerAdapter.notifyItemRemoved(
                                    paymentHistoryList.size
                                )
                                paymentHistoryList.addAll(it.data.property_payment.payments)
                                individualPaymentHistoryOwnerAdapter.notifyDataSetChanged()
                            } else {
                                page += 1
                                paymentHistoryList =
                                    it.data.property_payment.payments as ArrayList<OwnerPaymentHistoryListPayment>
                                setRecyclerView()
                            }

                            if (paymentHistoryList.size == 0) {
                                llEmptyData.visibility = View.VISIBLE
                                indivigual_payment_history_list_item_recycerview.visibility =
                                    View.GONE
                                owner_ShownHidePayemnt_history.visibility = View.GONE
                            } else {
                                llEmptyData.visibility = View.GONE
                                indivigual_payment_history_list_item_recycerview.visibility =
                                    View.VISIBLE
                                owner_ShownHidePayemnt_history.visibility = View.VISIBLE
                            }
                        }
                    }
                    Status.LOADING -> {
                        if (!isLoading) {
                            showProgressOwner()
                            isLoading = true
                        }
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

        /* show or hide btn */
        owner_ShownHidePayemnt_history.setOnClickListener {
            val name = owner_ShownHidePayemnt_history.text
            if (name.equals(getString(R.string.hide))) {
                indivigual_payment_history_list_item_recycerview.visibility = View.GONE
                owner_ShownHidePayemnt_history.text = getString(R.string.show)
            } else {
                indivigual_payment_history_list_item_recycerview.visibility = View.VISIBLE
                owner_ShownHidePayemnt_history.text = getString(R.string.hide)
            }
        }
    }
}