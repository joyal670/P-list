package com.property.propertyagent.owner_panel.ui.main.sidemenu.reports

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class ReportsActivity : BaseActivity() {
    private lateinit var reportsViewModel : ReportsViewModel

    override val layoutId : Int
        get() = R.layout.activity_reports
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = getString(R.string.reports)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        ReportsfrgOwnerRecyclerview.layoutManager = LinearLayoutManager(this)
        ReportsfrgOwnerRecyclerview.adapter = OwnerReportsAdapter({ type -> sendRequest(type) })
    }

    private fun sendRequest(type : String) {
        when (type) {
            getString(R.string.vacant_report) -> {
                reportsViewModel.ownerVacantReport()
            }
            getString(R.string.occupied_report) -> {
                reportsViewModel.ownerOccupiedReport()
            }
            getString(R.string.overall_report) -> {
                reportsViewModel.ownerOverallReport()
            }
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        reportsViewModel = ReportsViewModel()
    }

    override fun setupObserver() {
        reportsViewModel.getOwnerVacantReportResponse().observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    if (it.data!!.data.pdf.isNotBlank()) {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW , Uri.parse(it.data!!.data.pdf))
                        startActivity(browserIntent)
                    }
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        it.data?.response!! ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        it.data?.response!! ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })

        reportsViewModel.getOwnerOccupiedReportResponse().observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    if (it.data!!.data.pdf.isNotBlank()) {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW , Uri.parse(it.data!!.data.pdf))
                        startActivity(browserIntent)
                    }
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        it.data?.response!! ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        it.data?.response!! ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })

        reportsViewModel.getOwnerOverallReportResponse().observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    if (it.data!!.data.pdf.isNotBlank()) {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW , Uri.parse(it.data!!.data.pdf))
                        startActivity(browserIntent)
                    }
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        it.data?.response!! ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        it.data?.response!! ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    override fun onClicks() {

    }
}