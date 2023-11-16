package com.ncomfortsagent.ui.main.sideMenu.bankDetails.activity

import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityAddAccountBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.ui.main.sideMenu.bankDetails.viewmodel.BankViewModel
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected

class EditBankAccountActivity : BaseActivity<ActivityAddAccountBinding>() {

    private lateinit var bankViewModel: BankViewModel
    private var bankAccountId = ""
    private var bankAccountName = ""
    private var bankAccountNumber = ""
    private var bankAccountBic = ""
    private var bankAccountBankName = ""
    private var bankAccountBranchName = ""

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
        binding.tool.tvToolbarTitle.text = getString(R.string.edit_account)
        binding.addAccountBtn.text = getString(R.string.update)

        bankAccountId = intent.getStringExtra(Constants.BANK_ACCOUNT_ID)!!
        bankAccountName = intent.getStringExtra(Constants.BANK_ACCOUNT_NAME)!!
        bankAccountNumber = intent.getStringExtra(Constants.BANK_ACCOUNT_NUMBER)!!
        bankAccountBic = intent.getStringExtra(Constants.BANK_ACCOUNT_BIC)!!
        bankAccountBankName = intent.getStringExtra(Constants.BANK_ACCOUNT_BANK_NAME)!!
        bankAccountBranchName = intent.getStringExtra(Constants.BANK_ACCOUNT_BRANCH_NAME)!!

        binding.etAccountNumber.setText(bankAccountNumber)
        binding.etBankName.setText(bankAccountBankName)
        binding.etBranchName.setText(bankAccountBranchName)
        binding.etIFSCCode.setText(bankAccountBic)
        binding.etAccountName.setText(bankAccountName)
    }

    override fun fragmentLaunch() {
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        bankViewModel = BankViewModel(this)
    }

    override fun setupObserver() {
        bankViewModel.getAgentEditBankDetailsResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.success),
                        it.data!!.response,
                        R.color.de_york
                    )

                    binding.etAccountNumber.setText("")
                    binding.etBankName.setText("")
                    binding.etBranchName.setText("")
                    binding.etIFSCCode.setText("")
                    binding.etAccountName.setText("")
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        CommonUtils.showCookieBar(
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
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    CommonUtils.showCookieBar(
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

        binding.addAccountBtn.setOnClickListener {
            val bankName = binding.etBankName.text.trim().toString()
            val branchName = binding.etBranchName.text.trim().toString()
            val accountNumber = binding.etAccountNumber.text.trim().toString()
            val ifsc = binding.etIFSCCode.text.trim().toString()
            val accountName = binding.etAccountName.text.trim().toString()

            if (bankName.isBlank() || branchName.isBlank() || accountNumber.isBlank() || ifsc.isBlank() || accountName.isBlank()) {
                if (bankName.isBlank()) {
                    binding.etBankName.error = getString(R.string.banknamerequired)
                }
                if (branchName.isBlank()) {
                    binding.etBranchName.error = getString(R.string.branchnamerequired)
                }
                if (accountNumber.isBlank()) {
                    binding.etAccountNumber.error = getString(R.string.accountnumberrequired)
                }
                if (accountName.isBlank()) {
                    binding.etAccountName.error = getString(R.string.accountnamerequired)
                }
                if (ifsc.isBlank()) {
                    binding.etIFSCCode.error = getString(R.string.bicrequired)
                }
            } else {
                bankViewModel.editBankDetails(
                    accountName,
                    accountNumber,
                    bankName,
                    branchName,
                    ifsc,
                    bankAccountId
                )
            }
        }

    }
}