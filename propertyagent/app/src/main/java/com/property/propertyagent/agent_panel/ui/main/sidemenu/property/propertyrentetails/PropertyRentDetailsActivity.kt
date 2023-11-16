package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyrentetails

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyrentetails.adapter.RentDetailsAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyrentetails.adapter.SaleDetailsAdapter
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_property_rent_details.OtherChargeData
import com.property.propertyagent.modal.agent.agent_property_sale_details.EmiDetail
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.AppPreferences.property_type
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_property_rent_details.*
import kotlinx.android.synthetic.main.fragment_myproperty.llEmptyData
import kotlinx.android.synthetic.main.rent_layout.*
import kotlinx.android.synthetic.main.sale_layout.*
import kotlinx.android.synthetic.main.toolbar_main.*

class PropertyRentDetailsActivity : BaseActivity() {

    private lateinit var propertyRentSaleDetailsViewModel: PropertyRentSaleDetailsViewModel

    var type: String = ""
    private var ran: Int? = null

    override val layoutId: Int
        get() = R.layout.activity_property_rent_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {

        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.rent_details)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        ran = property_type
        if (ran == 0) {
            rent_layout.visibility = View.VISIBLE
            type = "rent"
            propertyRentSaleDetailsViewModel.agentPropertyRentDetails(clicked_property_id, type)
        } else {
            sale_layout.visibility = View.VISIBLE
            type = "sale"
            propertyRentSaleDetailsViewModel.agentPropertySaleDetails(clicked_property_id, type)
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        propertyRentSaleDetailsViewModel = PropertyRentSaleDetailsViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        propertyRentSaleDetailsViewModel.getAgentPropertyRentDetailsResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data != null) {
                            tvRentDuration.text = it.data.rent_data.type
                            tvRentAmount.text =
                                getString(R.string.sar) + " " + it.data.rent_data.rent
                            tvSecurityDepositAmount.text =
                                getString(R.string.sar) + " " + it.data.rent_data.security_deposit
                            if (!it.data.other_charge_data.isNullOrEmpty()) {
                                // recyclerview setup
                                setupRentRecyclerView(it.data.other_charge_data)
                            }
                            container.isVisible = true
                        } else {
                            llEmptyData.isVisible = true
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
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
                }
            })

        propertyRentSaleDetailsViewModel.getAgentPropertySaleDetailsResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data != null) {
                            tvSalesAmount.text = "SAR " + it.data.selling_price
                            if (!it.data.emi_details.isNullOrEmpty()) {
                                // recyclerview setup
                                setupSaleRecyclerView(it.data.emi_details)
                            }
                            container.isVisible = true
                        } else {
                            llEmptyData.isVisible = true
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
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
                }
            })
    }

    override fun onClicks() {

    }

    private fun setupRentRecyclerView(response: List<OtherChargeData>) {
        rvRent.layoutManager = LinearLayoutManager(this)
        rvRent.adapter =
            RentDetailsAdapter(response)
    }

    private fun setupSaleRecyclerView(response: List<EmiDetail>) {
        rvSale.layoutManager = LinearLayoutManager(this)
        rvSale.adapter =
            SaleDetailsAdapter(response)
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            if (ran == 0) {
                rent_layout.visibility = View.VISIBLE
                type = "rent"
                propertyRentSaleDetailsViewModel.agentPropertyRentDetails(
                    clicked_property_id,
                    type
                )
            } else {
                sale_layout.visibility = View.VISIBLE
                type = "sale"
                propertyRentSaleDetailsViewModel.agentPropertySaleDetails(
                    clicked_property_id,
                    type
                )
            }
        }
    }
}