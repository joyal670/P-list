package com.property.propertyagent.owner_panel.ui.main.sidemenu.change_password

import android.widget.Toast
import com.property.propertyagent.R
import com.property.propertyagent.base.new.BaseActivity1
import com.property.propertyagent.databinding.ActivityForgotPasswordBinding
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.start_up.auth.viewmodel.ForgotPasswordViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected

class ForgotPasswordActivity : BaseActivity1<ActivityForgotPasswordBinding>() {

    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override val layoutId: Int
        get() = R.layout.activity_forgot_password
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(binding.toolbar.ownerToolbar)
        binding.toolbar.tvToolbarTitleOwner.text = getString(R.string.forgot_password)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        forgotPasswordViewModel = ForgotPasswordViewModel()
    }

    override fun setupObserver() {
        forgotPasswordViewModel.forgotPasswordData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                    super.onBackPressed()

                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        }
    }

    override fun onClicks() {
        binding.forgotPasswordBtn.setOnClickListener {
            val email = binding.fragmentForgotEmailEtx.text.toString()
            if (email.isBlank()) {
                binding.tilEmailForgot.error = getString(R.string.email_required)
            } else {
                forgotPasswordViewModel.forgotPassword(email, "1")
            }
        }
    }

    override fun getViewBinging(): ActivityForgotPasswordBinding =
        ActivityForgotPasswordBinding.inflate(layoutInflater)

}