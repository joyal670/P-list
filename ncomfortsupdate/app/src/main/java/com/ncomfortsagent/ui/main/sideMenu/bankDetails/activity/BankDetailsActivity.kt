package com.ncomfortsagent.ui.main.sideMenu.bankDetails.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityBankDetailsBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.model.view_bank_details.AgentBankDetail
import com.ncomfortsagent.ui.main.sideMenu.bankDetails.adapter.BankAdapter
import com.ncomfortsagent.ui.main.sideMenu.bankDetails.viewmodel.BankViewModel
import com.ncomfortsagent.utils.*

class BankDetailsActivity : BaseActivity<ActivityBankDetailsBinding>() {
    private lateinit var bankViewModel: BankViewModel
    private lateinit var bankAdapter: BankAdapter
    private var bankDetails = ArrayList<AgentBankDetail>()
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override val layoutId: Int
        get() = R.layout.activity_bank_details
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityBankDetailsBinding =
        ActivityBankDetailsBinding.inflate(layoutInflater)

    override fun initData() {

        setSupportActionBar(binding.tool.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.tool.tvToolbarTitle.text = getString(R.string.bank_details)

        binding.swipeToRefresh.isRefreshing = false
        bankViewModel.viewBankDetails(page.toString())

    }

    private fun setupRecyclerView() {

        bankAdapter = BankAdapter(
            this,
            bankDetails,
            { id: Int, accountName: String, accountNumber: String, ifsc: String, bankName: String, branchName ->
                editAccount(
                    id,
                    accountName,
                    accountNumber,
                    ifsc,
                    bankName,
                    branchName
                )
            },
            { id: Int -> remove(id) })
        binding.rvBank.adapter = bankAdapter
        binding.rvBank.scheduleLayoutAnimation()
        binding.rvBank.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    private fun loadData() {
        if (page <= totalPages) {
            bankDetails.add(AgentBankDetail("", "", "", "", -1, ""))
            bankAdapter.notifyItemInserted(bankDetails.size - 1)
            bankViewModel.viewBankDetails(page.toString())
        }
    }

    /* Remove bank account */
    private fun remove(id: Int) {
        bankViewModel.removeBankDetails(id.toString())
    }

    /* Edit bank account */
    private fun editAccount(
        id: Int,
        accountName: String,
        accountNumber: String,
        ifsc: String,
        bankName: String,
        branchName: String
    ) {
        val intent = Intent(this, EditBankAccountActivity::class.java)
        intent.putExtra(Constants.BANK_ACCOUNT_ID, id.toString())
        intent.putExtra(Constants.BANK_ACCOUNT_NAME, accountName)
        intent.putExtra(Constants.BANK_ACCOUNT_NUMBER, accountNumber)
        intent.putExtra(Constants.BANK_ACCOUNT_BIC, ifsc)
        intent.putExtra(Constants.BANK_ACCOUNT_BANK_NAME, bankName)
        intent.putExtra(Constants.BANK_ACCOUNT_BRANCH_NAME, branchName)
        startActivity(intent)
    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {
        binding.rvBank.layoutManager = LinearLayoutManager(this)
    }

    override fun setupViewModel() {
        bankViewModel = BankViewModel(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        bankViewModel.getAgentViewBankDetailsResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvBank.visibility = View.VISIBLE

                    if (it.data!!.data != null) {
                        totalPages = it.data.data.total_page_count
                        if (bankDetails.size != 0) {
                            isLoading = false
                            page += 1
                            bankDetails.removeAt(bankDetails.size - 1)
                            bankAdapter.notifyItemRemoved(bankDetails.size)
                            bankDetails.addAll(it.data.data.agent_bank_details)
                            bankAdapter.notifyDataSetChanged()
                        } else {
                            page += 1
                            bankDetails =
                                it.data.data.agent_bank_details as ArrayList<AgentBankDetail>
                            setupRecyclerView()
                        }

                        if (bankDetails.size == 0) {
                            binding.rvBank.isVisible = false
                            binding.noResult.noData.isVisible = true
                        } else {
                            binding.rvBank.isVisible = true
                            binding.noResult.noData.isVisible = false
                        }



                    }
                }
                Status.LOADING -> {
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmer()
                }
                Status.DATA_EMPTY -> {
                   binding.shimmerLayout.stopShimmer()
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
                Status.ERROR -> {
                    binding.shimmerLayout.stopShimmer()
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.NO_INTERNET -> {
                    binding.shimmerLayout.stopShimmer()
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
            }
        })

        bankViewModel.getAgentRemoveBankDetailsResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.success),
                        it.data!!.response,
                        R.color.de_york
                    )
                    refreshData()
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    CommonUtils.showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
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
            }
        })
    }

    override fun onClicks() {
        binding.tool.addAccountLayout.setOnClickListener {
            val intent = Intent(this, AddAccountActivity::class.java)
            startActivity(intent)
        }

        binding.swipeToRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshData() {
        try {
            page = 1
            bankDetails.clear()
            bankAdapter.notifyDataSetChanged()
            initData()
        } catch (ex: Exception) {

        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }
}