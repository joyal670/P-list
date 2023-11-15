package com.property.propertyagent.agent_panel.ui.main.sidemenu.myearning

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
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.agent.my_earnings.Payment
import com.property.propertyagent.modal.agent.my_earnings.PropertyRel
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_myearning.*

class MyEarningsFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var myEarningsViewModel: MyEarningsViewModel

    private var earningsDataList = ArrayList<Payment>()
    private lateinit var myEarningAdapter: MyEarningAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    private var isLoadingMain: Boolean = false
    private var isTotalZero: Boolean = false

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_myearning, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.my_earnings))

        myEarningsViewModel.myEarningsTotal()
    }

    private fun setCashListSellAllRecyclerView() {
        myEarngFrgRecyclerView.layoutManager = LinearLayoutManager(context)
        myEarningAdapter = MyEarningAdapter(requireContext(), earningsDataList)
        myEarngFrgRecyclerView.adapter = myEarningAdapter

        myEarngFrgRecyclerView.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            earningsDataList.add(Payment("", -1, -1, PropertyRel(-1, ""), -1))
            myEarningAdapter.notifyItemInserted(earningsDataList.size - 1)
            myEarningsViewModel.myEarningsList(page.toString())
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        myEarningsViewModel = MyEarningsViewModel()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun setupObserver() {
        myEarningsViewModel.getAgentMyEarningsTotalResponse()
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
                        tvTotalEarnings.text = "SAR " + it.data!!.data.total_earnings
                        btnRequest.isVisible = it.data.data.can_request == 1

                        if (it.data.data.total_earnings == "0") {
                            isTotalZero = true
                        }

                        myEarningsViewModel.myEarningsList(page.toString())
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

        myEarningsViewModel.getAgentMyEarningsListResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            if (earningsDataList.size != 0) {
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
                        if (!it.data!!.data.equals(null)) {
                            totalPages = it.data.data.total_page_count
                            if (earningsDataList.size != 0) {
                                isLoading = false
                                page += 1
                                earningsDataList.removeAt(earningsDataList.size - 1)
                                myEarningAdapter.notifyItemRemoved(earningsDataList.size)
                                earningsDataList.addAll(it.data.data.payments)
                                myEarningAdapter.notifyDataSetChanged()
                            } else {
                                page += 1
                                earningsDataList =
                                    it.data.data.payments as ArrayList<Payment>
                                setCashListSellAllRecyclerView()
                                if (isTotalZero) {
                                    llEmptyData.isVisible = true
                                    container.isVisible = false
                                } else {
                                    container.isVisible = true
                                    llEmptyData.isVisible = false
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

        myEarningsViewModel.getAgentMyEarningsRequestResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (!isLoadingMain) {
                            showProgress()
                            isLoadingMain = true
                            page = 1
                            earningsDataList.clear()
                            myEarningAdapter.notifyDataSetChanged()
                        }
                    }
                    Status.SUCCESS -> {
                        Log.i("TAG", "setupObserver: " + it.data)
                        myEarningsViewModel.myEarningsTotal()
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
        btnRequest.setOnClickListener {
            myEarningsViewModel.myEarningsRequest()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            myEarningsViewModel.myEarningsTotal()
        }
    }
}