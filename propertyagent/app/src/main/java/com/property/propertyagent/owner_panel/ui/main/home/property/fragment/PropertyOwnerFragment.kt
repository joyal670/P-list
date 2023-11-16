package com.property.propertyagent.owner_panel.ui.main.home.property.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingBuildingApartment
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingOwner
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingPropertyPriorityImage
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingPropertyPriorityType
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.owner_panel.ui.main.home.property.adapter.PropertyMainAdapterOwner
import com.property.propertyagent.owner_panel.ui.main.home.property.viewmodel.OwnerPropertyMainListingViewModel
import com.property.propertyagent.utils.AppPreferences.clicked_filter_indicator
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_property_owner.*

class PropertyOwnerFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var ownerPropertyMainListingViewModel: OwnerPropertyMainListingViewModel
    private var propertyList = ArrayList<OwnerPropertyMainListingOwner>()
    private lateinit var propertyMainAdapterOwner: PropertyMainAdapterOwner
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0
    private var selectedType = 1
    private var propertyTypesNameList = ArrayList<String>()
    private var propertyTypesIdList = ArrayList<Int>()
    private var isFromFilter = false
    private var filterId = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_property_owner, container, false)
    }

    override fun initData() {

        swipeRefreshLayout.setRefreshing(false)

        fragmentTransInterface = activity as HomeOwnerActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.property))

        /* list_id -> for showing only occupied property */
        /* selected type -> residential or commercial */
        ownerPropertyMainListingViewModel.propertyList(
            page.toString(),
            selectedType.toString(),
            "2",
            "1"
        )

        filterId = -1

    }

    private fun setRecyclerView() {
        rvCommercial.layoutManager = LinearLayoutManager(context)
        propertyMainAdapterOwner = PropertyMainAdapterOwner(requireContext(), propertyList)
        rvCommercial.adapter = propertyMainAdapterOwner
        rvCommercial.scheduleLayoutAnimation()

        rvCommercial.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            propertyList.add(
                OwnerPropertyMainListingOwner(
                    -1,
                    listOf(
                        OwnerPropertyMainListingBuildingApartment(
                            OwnerPropertyMainListingPropertyPriorityImage("", -1),
                            -1,
                            -1,
                            OwnerPropertyMainListingPropertyPriorityType(-1, ""),
                            -1
                        )
                    ),
                    -1, "", -1, "", -1, "", -1, "", -1, "", "",
                    OwnerPropertyMainListingPropertyPriorityImage("", -1), "", -1, "", -1
                )
            )

            propertyMainAdapterOwner.notifyItemInserted(propertyList.size - 1)

            if (isFromFilter) {
                ownerPropertyMainListingViewModel.propertyFilterListing(
                    page.toString(),
                    filterId.toString()
                )
            } else {
                ownerPropertyMainListingViewModel.propertyList(
                    page.toString(),
                    selectedType.toString(), "2", "1"
                )
            }

        }
    }

    override fun setupUI() {

        /* check if filter indicator is clicked or not */
        if (clicked_filter_indicator) {
            filterIndicator.visibility = View.GONE
        }
    }

    override fun setupViewModel() {
        ownerPropertyMainListingViewModel = OwnerPropertyMainListingViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {

        /* property listing */
        ownerPropertyMainListingViewModel.getOwnerPaymentResponse().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    if (propertyList.size == 0) {
                        dismissProgressOwner()
                    }

                    if (it.data!!.response != null) {
                        totalPages = it.data.response.total_page_count
                        if (propertyList.size != 0) {
                            isLoading = false
                            page += 1
                            propertyList.removeAt(propertyList.size - 1)
                            propertyMainAdapterOwner.notifyItemRemoved(propertyList.size)
                            propertyList.addAll(it.data.response.owner)
                            propertyMainAdapterOwner.notifyDataSetChanged()
                        } else {
                            page += 1
                            propertyList =
                                it.data.response.owner as ArrayList<OwnerPropertyMainListingOwner>
                            setRecyclerView()
                        }

                        if (propertyList.size == 0) {
                            llEmptyData1.visibility = View.VISIBLE
                            rvCommercial.visibility = View.GONE
                        } else {
                            llEmptyData1.visibility = View.GONE
                            rvCommercial.visibility = View.VISIBLE
                        }

                    }
                }

                Status.LOADING -> {
                    if (propertyList.size == 0) {
                        showProgressOwner()
                    }
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
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

        /* types listing */
        ownerPropertyMainListingViewModel.getOwnerPropertyTypesResponse().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    propertyTypesNameList.clear()
                    propertyTypesIdList.clear()

                    propertyTypesNameList.add("All")
                    propertyTypesIdList.add(0)

                    it.data!!.response.forEach {
                        propertyTypesNameList.addAll(listOf(it.type))
                        propertyTypesIdList.addAll(listOf(it.id))
                    }

                    setupFilterDialog()

                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        requireContext(), getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
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
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
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

        /* property based on filter type*/
        ownerPropertyMainListingViewModel.getOwnerPropertyFilterListingResponse()
            .observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgressOwner()

                        if (it.data!!.response != null) {
                            totalPages = it.data.response.total_page_count
                            if (propertyList.size != 0) {
                                isLoading = false
                                page += 1
                                propertyList.removeAt(propertyList.size - 1)
                                propertyMainAdapterOwner.notifyItemRemoved(propertyList.size)
                                propertyList.addAll(it.data.response.owner)
                                propertyMainAdapterOwner.notifyDataSetChanged()
                            } else {
                                page += 1
                                propertyList =
                                    it.data.response.owner as ArrayList<OwnerPropertyMainListingOwner>
                                setRecyclerView()
                            }

                            if (propertyList.size == 0) {
                                llEmptyData1.visibility = View.VISIBLE
                                rvCommercial.visibility = View.GONE
                            } else {
                                llEmptyData1.visibility = View.GONE
                                rvCommercial.visibility = View.VISIBLE
                            }
                        }
                    }
                    Status.LOADING -> {
                        if (propertyList.size == 0) {
                            showProgressOwner()
                        }
                    }
                    Status.ERROR -> {
                        dismissProgressOwner()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
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
                    Status.NO_INTERNET -> {
                        dismissProgressOwner()
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

        /* commercial btn */
        propertyfrg_owner_commercial.setOnClickListener {
            propertyfrg_owner_commercial.setStrokeColorResource(R.color.color_accent_green)
            propertyfrg_owner_commercial.setTextColor(Color.parseColor("#6AC58C"))
            propertyfrg_owner_residential.setStrokeColorResource(R.color.color_accent_grey)
            propertyfrg_owner_residential.setTextColor(Color.parseColor("#616161"))

            selectedType = 1
            refreshUi()
        }

        /* residential btn */
        propertyfrg_owner_residential.setOnClickListener {
            propertyfrg_owner_commercial.setStrokeColorResource(R.color.color_accent_grey)
            propertyfrg_owner_commercial.setTextColor(Color.parseColor("#616161"))
            propertyfrg_owner_residential.setStrokeColorResource(R.color.color_accent_green)
            propertyfrg_owner_residential.setTextColor(Color.parseColor("#6AC58C"))

            selectedType = 0
            refreshUi()
        }

        /* filter btn */
        property_filter.setOnClickListener {

            /* set filter indicator clicked */
            clicked_filter_indicator = true

            /* api call */
            ownerPropertyMainListingViewModel.propertyTypes()
        }

        swipeRefreshLayout.setOnRefreshListener {
            refreshUi()
        }
    }

    /* filter dialog */
    private fun setupFilterDialog() {
        val checkedItem = filterId
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.select_property_type))

            .setSingleChoiceItems(
                propertyTypesNameList.toTypedArray(),
                checkedItem
            ) { dialog, which ->
                refreshUiFilter(propertyTypesIdList[which])
                dialog.dismiss()
            }
            .show()
    }

    /* property filter response setup */
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshUiFilter(id: Int) {
        filterIndicator.visibility = View.GONE
        isFromFilter = true
        filterId = id
        page = 1
        propertyList.clear()
        propertyMainAdapterOwner.notifyDataSetChanged()
        if (id == -1) {
            ownerPropertyMainListingViewModel.propertyFilterListing(
                page.toString(),
                filterId.toString()
            )
        } else {
            ownerPropertyMainListingViewModel.propertyFilterListing(
                page.toString(),
                filterId.toString()
            )
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshUi() {
        page = 1
        propertyList.clear()
        propertyMainAdapterOwner.notifyDataSetChanged()
        filterId = -1
        initData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_translate)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_lineView)
        item2.isVisible = false

        val item3: MenuItem = menu.findItem(R.id.customtoolbar_addProperty)
        item3.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.custom_toolbar_menu_owner, menu)

        val searchItem = menu.findItem(R.id.customtoolbar_search)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = requireActivity().getString(R.string.search_properties)
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = true
        searchView.isIconifiedByDefault = true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        propertyMainAdapterOwner.filter.filter(newText.toString())
        return false
    }
}