package com.ncomfortsagent.ui.main.sideMenu.bankDetails.activity

import android.content.Intent
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityAddAccountBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.ui.main.sideMenu.bankDetails.viewmodel.BankViewModel
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected

class AddAccountActivity : BaseActivity<ActivityAddAccountBinding>() {
    private lateinit var bankViewModel: BankViewModel

    override val layoutId: Int
        get() = R.layout.activity_add_account
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityAddAccountBinding =
        ActivityAddAccountBinding.inflate(layoutInflater)

    override fun initData() {
        setSupportActionBar(binding.tool.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.tool.tvToolbarTitle.text = getString(R.string.add_account)
    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        bankViewModel = BankViewModel(this)
    }

    override fun setupObserver() {

        bankViewModel.getAgentAddBankDetailsResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    binding.etAccountName.setText("")
                    binding.etAccountNumber.setText("")
                    binding.etBankName.setText("")
                    binding.etBranchName.setText("")
                    binding.etIFSCCode.setText("")

                    showCookieBar(
                        this,
                        getString(R.string.success),
                        it.data!!.response,
                        R.color.de_york
                    )

                    val intent = Intent(this, BankDetailsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        showCookieBar(
                            this,
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        })
    }

    override fun onClicks() {

        /* add bank account */
        binding.addAccountBtn.setOnClickListener {
            val accountName = binding.etAccountName.text.trim().toString()
            val accountNumber = binding.etAccountNumber.text.trim().toString()
            val bankName = binding.etBankName.text.trim().toString()
            val branchName = binding.etBranchName.text.trim().toString()
            val ifsc = binding.etIFSCCode.text.trim().toString()

            if (accountName.isBlank() || accountNumber.isBlank() || bankName.isBlank() || branchName.isBlank() || ifsc.isBlank()) {
                if (accountName.isBlank()) {
                    binding.etAccountName.error = getString(R.string.accountnamerequired)
                }
                if (accountNumber.isBlank()) {
                    binding.etAccountNumber.error = getString(R.string.accountnumberrequired)
                }
                if (bankName.isBlank()) {
                    binding.etBankName.error = getString(R.string.banknamerequired)
                }
                if (branchName.isBlank()) {
                    binding.etBranchName.error = getString(R.string.branchnamerequired)
                }
                if (ifsc.isBlank()) {
                    binding.etIFSCCode.error = getString(R.string.bicrequired)
                }
            } else {
                bankViewModel.addBankDetails(accountName, accountNumber, bankName, branchName, ifsc)
            }
        }
    }

}