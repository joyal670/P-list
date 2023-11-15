package com.property.propertyuser.ui.main.my_property.view_details.generate_ticket_vacate_request

import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityGenerateTicketVacateRequestBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected

class GenerateTicketVacateRequestActivity :
    BaseActivity<ActivityGenerateTicketVacateRequestBinding>(), ActivityListener {
    private var vacateID = ""
    private lateinit var generateTicketVacateRequestViewModel: GenerateTicketVacateRequestViewModel
    override fun getViewBinging(): ActivityGenerateTicketVacateRequestBinding = ActivityGenerateTicketVacateRequestBinding.inflate(layoutInflater)

    override val layoutId: Int
        get() = R.layout.activity_generate_ticket_vacate_request
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        vacateID = intent.getStringExtra("vacate_id").toString()
        generateTicketVacateRequestViewModel.vacateRequestDetails(vacateID)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        generateTicketVacateRequestViewModel = GenerateTicketVacateRequestViewModel()
    }

    override fun setupObserver() {
        generateTicketVacateRequestViewModel.getVacateRequestDetailsResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> {
                        showLoader()
                        binding.linearMain.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("responseproceeddetails", Gson().toJson(it))
                        if (it.data?.user_vacate_request != null) {

                            binding.tvTicketRaiseOnDate.text =
                                it.data.user_vacate_request.vacating_date
                            binding.tvPropertyId.text =
                                it.data.user_vacate_request.user_property_related.property_reg_no
                            binding.tvContractStartDate.text =
                                it.data.user_vacate_request.user_property_related.contract_start_date
                            binding.tvContractEndDate.text =
                                it.data.user_vacate_request.user_property_related.contract_start_date
                            if (it.data.user_vacate_request.status == 1) {
                                binding.tvStatus.text = getString(R.string.tvStatus1)
                            } else {
                                binding.tvStatus.text = getString(R.string.inActive)
                            }

                            if (it.data.user_vacate_request.user_property_related.status == 1) {
                                binding.tvPropertyVerification.text =
                                    getString(R.string.tvPropertyVerification)
                            } else {
                                binding.tvPropertyVerification.text =
                                    getString(R.string.not_verified)
                            }

                            binding.tvDueAmount.text =
                                it.data.user_vacate_request.user_property_related.due_amount
                            binding.tvSecurityDepositAmount.text =
                                it.data.user_vacate_request.user_property_related.security_deposit
                            setTitle("Ticket ID: T-${it.data.user_vacate_request.id}")


                            binding.linearMain.visibility = View.VISIBLE
                            val animation  = AnimationUtils.loadAnimation(this, R.anim.blink)
                            binding.tvTicketClosed.startAnimation(animation)
                        }
                    }
                    Status.ERROR -> {
                        dismissLoader()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissLoader()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            Toaster.showToast(
                                this, getString(R.string.no_internet),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        }
                    }

                }
            })
    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}