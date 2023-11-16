package com.property.propertyagent.owner_panel.ui.main.sidemenu.change_password

import android.content.Intent
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.property.propertyagent.R
import com.property.propertyagent.base.new.BaseActivity1
import com.property.propertyagent.databinding.ActivityOwnerChangePasswordBinding
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected


class OwnerChangePasswordActivity : BaseActivity1<ActivityOwnerChangePasswordBinding>() {

    private lateinit var ownerChangePasswordViewModel: OwnerChangePasswordViewModel

    override val layoutId: Int
        get() = R.layout.activity_owner_change_password
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(binding.toolbar.ownerToolbar)
        binding.toolbar.tvToolbarTitleOwner.text = getString(R.string.change_password)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        ownerChangePasswordViewModel = OwnerChangePasswordViewModel()
    }

    override fun setupObserver() {
        ownerChangePasswordViewModel.getChangePasswordAgentResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this, it.data!!.response,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG
                    )

                    startActivity(Intent(this, HomeOwnerActivity::class.java))
                    finishAffinity()
                }
                Status.LOADING -> {
                    showProgressOwner()
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
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this, it.data!!.response,
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
            }
        }
    }

    override fun onClicks() {

        binding.changePasswordBtn.setOnClickListener {
            val oldPassword = binding.etOldPassword.text.toString()
            val newPassword = binding.etNewPassword.text.toString()
            val confirmNewPassword = binding.etNewPasswordConfirm.text.toString()
            if (oldPassword.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
                if (oldPassword.isBlank()) {
                    binding.et1.error = getString(R.string.oldPasswordIsRequired)
                }
                if (newPassword.isBlank()) {
                    binding.et2.error = getString(R.string.newPasswordIsRequired)
                }
                if (confirmNewPassword.isBlank()) {
                    binding.et3.error = getString(R.string.confirmPasswordIsRequired)
                }
            } else {
                if (newPassword == confirmNewPassword) {
                    ownerChangePasswordViewModel.changePassword(
                        oldPassword,
                        newPassword,
                        confirmNewPassword
                    )
                } else {
                    binding.et3.requestFocus()
                    binding.et3.error = getString(R.string.passwordNotMatched)
                }
            }
        }

        binding.etOldPassword.doOnTextChanged { text, start, before, count ->
            binding.et1.isErrorEnabled = false
        }

        binding.etNewPassword.doOnTextChanged { text, start, before, count ->
            binding.et2.isErrorEnabled = false
        }

        binding.etNewPasswordConfirm.doOnTextChanged { text, start, before, count ->
            binding.et3.isErrorEnabled = false
        }

        binding.forgotPasswordBtn.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }

    override fun getViewBinging(): ActivityOwnerChangePasswordBinding =
        ActivityOwnerChangePasswordBinding.inflate(layoutInflater)

}