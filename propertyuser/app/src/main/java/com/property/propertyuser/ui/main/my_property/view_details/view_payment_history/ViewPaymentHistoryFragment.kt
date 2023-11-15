package com.property.propertyuser.ui.main.my_property.view_details.view_payment_history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentViewPaymentHistoryBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.payment_history.PaymentHistory
import com.property.propertyuser.ui.main.my_property.view_details.ViewDetailsActivity
import com.property.propertyuser.ui.main.my_property.view_details.view_payment_history.adapter.PaymentHistoryAdapter
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.fragment_view_payment_history.*

class ViewPaymentHistoryFragment : BaseFragment() {

    private lateinit var viewPaymentHistoryViewModel: ViewPaymentHistoryViewModel
    private var userPropertyId = ""
    private var pageNo: Int = 0
    private var isLoading: Boolean = false
    private var totalPageCount = 0
    private var paymentHistoryList = ArrayList<PaymentHistory>()
    private lateinit var paymentHistoryAdapter: PaymentHistoryAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var passedPropertyCode = ""
    private var passedPropertyName = ""
    private var passedRentAmount = ""
    private var passedPropertyImage = ""

    companion object {
        const val ARG_USER_PROPERTY_ID = "user_property_id"
        const val ARG_PROPERTY_CODE = "property_code"
        const val ARG_PROPERTY_NAME = "property_name"
        const val ARG_PROPERTY_RENT = "property_rent"
        const val ARG_PROPERTY_IMAGE = "property_image"
        fun newInstance(
            user_property_id: String,
            property_name: String,
            property_code: String,
            property_rent: String,
            property_image: String
        ): ViewPaymentHistoryFragment {
            val fragment = ViewPaymentHistoryFragment()
            Log.e("code", property_code)
            Log.e("name", property_name)
            Log.e("rent", property_rent)
            Log.e("image", property_image)
            val bundle = Bundle().apply {
                putString(ARG_USER_PROPERTY_ID, user_property_id)
                putString(ARG_PROPERTY_NAME, property_name)
                putString(ARG_PROPERTY_CODE, property_code)
                putString(ARG_PROPERTY_RENT, property_rent)
                putString(ARG_PROPERTY_IMAGE, property_image)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentViewPaymentHistoryBinding
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPropertyId = arguments?.getString(ARG_USER_PROPERTY_ID).toString()
        passedPropertyCode = arguments?.getString(ARG_PROPERTY_CODE).toString()
        passedPropertyName = arguments?.getString(ARG_PROPERTY_NAME).toString()
        passedRentAmount = arguments?.getString(ARG_PROPERTY_RENT).toString()
        passedPropertyImage = arguments?.getString(ARG_PROPERTY_IMAGE).toString()
        binding = FragmentViewPaymentHistoryBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        pageNo = 1
        activityListener.setTitle(getString(R.string.payment_history))
        Log.e("userpropertyid", userPropertyId)
        paymentHistoryList = ArrayList()
        roundedPropertyForPayment.loadImagesWithGlideExt(passedPropertyImage)
        tvPropertyName.text = passedPropertyName
        tvPropertyCodePayment.text =
            requireContext().getString(R.string.tvPropertyCode) + " " + passedPropertyCode
        if (passedRentAmount.isNullOrBlank()) {
            tvRentText.visibility = View.GONE
        }
        tvRentAmountPayment.text = passedRentAmount
        viewPaymentHistoryViewModel.paymentHistory(pageNo.toString(), userPropertyId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as ViewDetailsActivity

    }

    private fun setupRecyclerViewPaymentHistory() {
        layoutManager = LinearLayoutManager(requireContext())
        rvPaymentHistoryList.layoutManager = layoutManager
        paymentHistoryAdapter = PaymentHistoryAdapter(requireContext(), paymentHistoryList)
        rvPaymentHistoryList.adapter = paymentHistoryAdapter

        rvPaymentHistoryList.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (pageNo <= totalPageCount) {
            pageNo += 1
            paymentHistoryList.add(
                PaymentHistory(
                    "", -1, "", -1,
                    -1, -1, -1, "", ""
                )
            )
            paymentHistoryAdapter.notifyItemInserted(paymentHistoryList.size - 1)
            viewPaymentHistoryViewModel.paymentHistory(pageNo.toString(), userPropertyId)
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        viewPaymentHistoryViewModel = ViewPaymentHistoryViewModel()
    }

    override fun setupObserver() {
        viewPaymentHistoryViewModel.getPaymentHistoryResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    if (paymentHistoryList.size == 0) {
                        showLoader()
                    }
                }
                Status.SUCCESS -> {
                    if (paymentHistoryList.size == 0) {
                        dismissLoader()
                    }
                    if (it.data != null) {
                        Log.e("response event pacage", Gson().toJson(it))
                        if (it.data.data != null) {
                            totalPageCount = it.data.data.total_page_count
                            if (paymentHistoryList.size != 0) {
                                isLoading = false
                                paymentHistoryList.removeAt(paymentHistoryList.size - 1)
                                paymentHistoryAdapter.notifyItemRemoved(paymentHistoryList.size)
                                if (!(it.data.data.payment_history.isNullOrEmpty())) {
                                    paymentHistoryList.addAll(it.data.data.payment_history)
                                    paymentHistoryAdapter.notifyDataSetChanged()
                                }
                            } else {
                                if (!(it.data.data.payment_history.isNullOrEmpty())) {
                                    paymentHistoryList =
                                        it.data.data.payment_history as ArrayList<PaymentHistory>
                                    setupRecyclerViewPaymentHistory()
                                } else {
                                    mainViewPaymentHistory.snackWhite(getString(R.string.no_payment))
                                }
                            }
                        }

                    } else {
                        Toaster.showToast(
                            requireContext(), it.status.toString(),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            requireContext(), getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
    }

    override fun onClicks() {

    }
}