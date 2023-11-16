package com.property.propertyagent.owner_panel.ui.main.home.payment.payment_fragment.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.owner.owner_payment_list_of_properties.*
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.owner_panel.ui.main.home.payment.payment_fragment.adapter.RequestPaymentOwnerAdapter
import com.property.propertyagent.owner_panel.ui.main.home.payment.payment_fragment.viewmodel.OwnerPaymentListOfPropertiesViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_payment_owner.*

class PaymentOwnerFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var ownerPaymentListOfPropertiesViewModel: OwnerPaymentListOfPropertiesViewModel
    private var paymentList = ArrayList<OwnerPaymentListOfPropertiesProperty>()
    private lateinit var requestPaymentOwnerAdapter: RequestPaymentOwnerAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0
    private var selectedType = 1


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_payment_owner, container, false)
    }

    override fun initData() {

        swipeRefreshLayout.setRefreshing(false)

        fragmentTransInterface = activity as HomeOwnerActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.payment))

        ownerPaymentListOfPropertiesViewModel.paymentListOfProperties(
            selectedType,
            page.toString()
        )

    }

    private fun setRecyclerView() {
        paymentFrgRentRecyclerview.layoutManager = LinearLayoutManager(context)
        requestPaymentOwnerAdapter = RequestPaymentOwnerAdapter(requireContext(), paymentList)
        paymentFrgRentRecyclerview.adapter = requestPaymentOwnerAdapter
        paymentFrgRentRecyclerview.scheduleLayoutAnimation()

        paymentFrgRentRecyclerview.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            paymentList.add(
                OwnerPaymentListOfPropertiesProperty(
                    "",
                    OwnerPaymentListOfPropertiesFirstOwnerPayament("", -1, "", -1, -1, -1),
                    -1,
                    -1,
                    OwnerPaymentListOfPropertiesLastOwnerPayament("", -1, "", -1, -1, -1),
                    "",
                    "",
                    "",
                    OwnerPaymentListOfPropertiesPropertyPriorityImage("", -1),
                    "",
                    OwnerPaymentListOfPropertiesPropertyRentFrequency(-1, ""),
                    -1, "", ""
                )
            )
            requestPaymentOwnerAdapter.notifyItemInserted(paymentList.size - 1)
            ownerPaymentListOfPropertiesViewModel.paymentListOfProperties(
                selectedType,
                page.toString()
            )
        }
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        ownerPaymentListOfPropertiesViewModel = OwnerPaymentListOfPropertiesViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        ownerPaymentListOfPropertiesViewModel.getOwnerPaymentResponse()
            .observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()

                        if (it.data!!.property_data != null) {
                            totalPages = it.data.property_data.total_page_count
                            if (paymentList.size != 0) {
                                isLoading = false
                                page += 1
                                paymentList.removeAt(paymentList.size - 1)
                                requestPaymentOwnerAdapter.notifyItemRemoved(paymentList.size)
                                paymentList.addAll(it.data.property_data.properties)
                                requestPaymentOwnerAdapter.notifyDataSetChanged()
                            } else {
                                page += 1
                                paymentList =
                                    it.data.property_data.properties as ArrayList<OwnerPaymentListOfPropertiesProperty>
                                setRecyclerView()
                            }

                            if (paymentList.size == 0) {
                                llEmptyData.visibility = View.VISIBLE
                                paymentFrgRentRecyclerview.visibility = View.GONE
                            } else {
                                llEmptyData.visibility = View.GONE
                                paymentFrgRentRecyclerview.visibility = View.VISIBLE
                            }
                        }
                    }
                    Status.LOADING -> {
                        showProgressOwner()
                    }
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
                        if (requireActivity().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })
    }

    override fun onClicks() {

        paymentfrgRentBtn.setOnClickListener {
            if (paymentfrgRentBtn.text.equals(getString(R.string.rent))) {
                val transition1: Transition = Slide(Gravity.BOTTOM)
                transition1.duration = 600
                transition1.addTarget(R.id.paymentFrgRentRecyclerview)
                TransitionManager.beginDelayedTransition(paymentFrgRentRecyclerview, transition1)

                paymentFrgRentRecyclerview.visibility = View.VISIBLE
                paymentfrgRentBtn.text = getString(R.string.hide)
                paymentfrgRentBtn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_up_green,
                    0,
                    0,
                    0
                )

                paymentfrgSaleBtn.text = getString(R.string.sale)
                paymentfrgSaleBtn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_down_green,
                    0,
                    0,
                    0
                )

                /* api call for rent */
                /* rent - 0 */
                selectedType = 1
                refreshUi()

            } else {
                val transition2: Transition = Fade()
                transition2.duration = 600
                transition2.addTarget(R.id.paymentFrgRentRecyclerview)
                TransitionManager.beginDelayedTransition(paymentFrgRentRecyclerview, transition2)

                paymentfrgRentBtn.text = getString(R.string.rent)
                paymentfrgRentBtn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_down_green,
                    0,
                    0,
                    0
                )

                /* api call for all list
                * for both rent and sale */
                selectedType = 3
                refreshUi()
            }
        }

        paymentfrgSaleBtn.setOnClickListener {
            if (paymentfrgSaleBtn.text.equals(getString(R.string.sale))) {
                val transition3: Transition = Slide(Gravity.BOTTOM)
                transition3.duration = 600
                transition3.addTarget(R.id.paymentFrgRentRecyclerview)
                TransitionManager.beginDelayedTransition(paymentFrgRentRecyclerview, transition3)

                paymentFrgRentRecyclerview.visibility = View.VISIBLE
                paymentfrgSaleBtn.text = getString(R.string.hide)
                paymentfrgSaleBtn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_up_green,
                    0,
                    0,
                    0
                )

                paymentfrgRentBtn.text = getString(R.string.rent)
                paymentfrgRentBtn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_down_green,
                    0,
                    0,
                    0
                )
                /* api call for sale */
                /* sale - 1 */
                selectedType = 2
                refreshUi()

            } else {
                val transition4: Transition = Fade()
                transition4.duration = 600
                transition4.addTarget(R.id.paymentFrgRentRecyclerview)
                TransitionManager.beginDelayedTransition(paymentFrgRentRecyclerview, transition4)

                paymentfrgSaleBtn.text = getString(R.string.sale)
                paymentfrgSaleBtn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_down_green,
                    0,
                    0,
                    0
                )

                /* api call for all list
                * for both rent and sale */
                selectedType = 3
                refreshUi()
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            refreshUi()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshUi() {
        page = 1
        paymentList.clear()
        requestPaymentOwnerAdapter.notifyDataSetChanged()
        initData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_addProperty)
        item2.isVisible = false

        val item3: MenuItem = menu.findItem(R.id.customtoolbar_lineView)
        item3.isVisible = false

        val item4: MenuItem = menu.findItem(R.id.customtoolbar_translate)
        item4.isVisible = false
    }
}