package com.property.propertyagent.agent_panel.ui.main.sidemenu.inhandcash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.inhandcash.adapter.CashInHandAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.agent.agent_cash_in_hand.CashData
import com.property.propertyagent.modal.agent.agent_cash_in_hand.PropertyRel
import com.property.propertyagent.modal.agent.agent_cash_in_hand.UserRel
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_cashinhand.*

class CashInHandFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var cashInHandViewModel: CashInHandViewModel

    private var cashDataList = ArrayList<CashData>()
    private lateinit var cashInHandAdapter: CashInHandAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    private var isLoadingMain: Boolean = false

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_cashinhand, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.InHandCash))

        cashInHandViewModel.cashInHandTotal()
    }

    private fun setCashListSellAllRecyclerView() {
        cashinhandFrgRecyclerView.layoutManager = LinearLayoutManager(context)
        cashInHandAdapter = CashInHandAdapter(requireContext(), cashDataList)
        cashinhandFrgRecyclerView.adapter = cashInHandAdapter

        cashinhandFrgRecyclerView.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            cashDataList.add(CashData(PropertyRel(-1, ""), UserRel(-1, ""), -1, "", -1, -1))
            cashInHandAdapter.notifyItemInserted(cashDataList.size - 1)
            cashInHandViewModel.cashInHandList(page.toString())
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        cashInHandViewModel = CashInHandViewModel()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun setupObserver() {
        cashInHandViewModel.getAgentCashInHandResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                            isLoadingMain = true
                        }
                    }
                    Status.SUCCESS -> {
                        Log.i("TAG", "setupObserver: " + it.data)
                        tvTotalCash.text = "SAR " + it.data!!.total_amount
                        if (it.data.canPay == 0) {
                            btnPayCash.isVisible = true
                        }
                        cashInHandViewModel.cashInHandList(page.toString())
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            })

        cashInHandViewModel.getAgentPayCashInHandResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                            isLoadingMain = true
                        }
                    }
                    Status.SUCCESS -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), it.data!!.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG
                        )
                        page = 1
                        cashDataList.clear()
                        cashInHandAdapter.notifyDataSetChanged()
                        cashInHandViewModel.cashInHandTotal()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            })

        cashInHandViewModel.getAgentCashInHandListResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            if (cashDataList.size != 0) {
                                if (page == 1) {
                                    showProgress()
                                }
                            }
                        }
                    }
                    Status.SUCCESS -> {
                        if (page == 1) {
                            dismissProgress()
                        }
                        isLoadingMain = false
                        if (!it.data!!.agent_cash_data.equals(null)) {
                            totalPages = it.data.agent_cash_data.total_page_count
                            if (cashDataList.size != 0) {
                                isLoading = false
                                page += 1
                                cashDataList.removeAt(cashDataList.size - 1)
                                cashInHandAdapter.notifyItemRemoved(cashDataList.size)
                                cashDataList.addAll(it.data.agent_cash_data.cash_data)
                                cashInHandAdapter.notifyDataSetChanged()
                            } else {
                                if (!it.data.agent_cash_data.cash_data.equals(null)) {
                                    page += 1
                                    cashDataList =
                                        it.data.agent_cash_data.cash_data as ArrayList<CashData>
                                    setCashListSellAllRecyclerView()
                                    container.isVisible = true
                                } else {
                                    llEmptyData.isVisible = true
                                    container.isVisible = false
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            })
    }

    override fun onClicks() {

        btnPayCash.setOnClickListener {
            cashInHandViewModel.payCashInHand()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            cashInHandViewModel.cashInHandTotal()
        }
    }
}