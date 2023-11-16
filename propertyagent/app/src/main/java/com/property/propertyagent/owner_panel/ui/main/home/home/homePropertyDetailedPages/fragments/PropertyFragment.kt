package com.property.propertyagent.owner_panel.ui.main.home.home.homePropertyDetailedPages.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingBuildingApartment
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingOwner
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingPropertyPriorityImage
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingPropertyPriorityType
import com.property.propertyagent.owner_panel.ui.main.home.home.homePropertyDetailedPages.activity.HomePropertyDetailedActivity
import com.property.propertyagent.owner_panel.ui.main.home.home.homePropertyDetailedPages.viewmodel.OwnerHomePropertyListingDetailsViewModel
import com.property.propertyagent.owner_panel.ui.main.home.property.adapter.PropertyMainAdapterOwner
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_noof_properties.*

class PropertyFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var ownerHomePropertyListingDetailsViewModel: OwnerHomePropertyListingDetailsViewModel
    private var propertyList = ArrayList<OwnerPropertyMainListingOwner>()
    private lateinit var propertyMainAdapterOwner: PropertyMainAdapterOwner
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    companion object {

        private const val TAG = "Fragment"
        private var selectedIntentType: Int? = null

        @JvmStatic
        fun newInstance(type: Int) =
            PropertyFragment().apply {
                arguments = Bundle().apply {
                    putInt("type", type)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            selectedIntentType = it.getInt("type")
        }

    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_noof_properties, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as HomePropertyDetailedActivity

        /* set title */
        when (selectedIntentType) {
            1 -> {
                fragmentTransInterface.setTitleFromFragment(getString(R.string.number_of_properties))
                ownerHomePropertyListingDetailsViewModel.homePropertyListingDetails(
                    page.toString(),
                    selectedIntentType.toString()
                )
            }
            2 -> {
                fragmentTransInterface.setTitleFromFragment(getString(R.string.occupied_properties))
                ownerHomePropertyListingDetailsViewModel.homePropertyListingDetails(
                    page.toString(),
                    selectedIntentType.toString()
                )
            }
            3 -> {
                fragmentTransInterface.setTitleFromFragment(getString(R.string.vacant_properties))
                ownerHomePropertyListingDetailsViewModel.homePropertyListingDetails(
                    page.toString(),
                    selectedIntentType.toString()
                )
            }
            4 -> {
                fragmentTransInterface.setTitleFromFragment(getString(R.string.under_maintenance))
                ownerHomePropertyListingDetailsViewModel.homePropertyListingDetails(
                    page.toString(),
                    selectedIntentType.toString()
                )
            }
        }
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
            ownerHomePropertyListingDetailsViewModel.homePropertyListingDetails(
                page.toString(),
                selectedIntentType.toString()
            )
        }
    }


    override fun setupUI() {

    }

    override fun setupViewModel() {
        ownerHomePropertyListingDetailsViewModel = OwnerHomePropertyListingDetailsViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        ownerHomePropertyListingDetailsViewModel.getOwnerHomePropertyListingDetails()
            .observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {

                        if (propertyList.size == 0) {
                            dismissProgressOwner()
                        }

                        totalPages = it.data!!.response.total_page_count
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

                        llEmptyData1.isVisible = propertyList.size == 0
                    }

                    Status.LOADING -> {
                        if (propertyList.size == 0) {
                            showProgressOwner()
                        }
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
                            requireContext(), it.message!!,
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }
            })
    }

    override fun onClicks() {
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_addProperty)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_lineView)
        item2.isVisible = false

        val item3: MenuItem = menu.findItem(R.id.customtoolbar_translate)
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