package com.property.propertyagent.owner_panel.ui.main.home.home.activity


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingBuildingApartment
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingOwner
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingPropertyPriorityImage
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingPropertyPriorityType
import com.property.propertyagent.owner_panel.ui.main.home.home.viewmodel.OwnerHomeViewModel
import com.property.propertyagent.owner_panel.ui.main.home.property.adapter.PropertyMainAdapterOwner
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_search_property.*


class SearchPropertyActivity : BaseActivity() {

    private lateinit var ownerHomeViewModel : OwnerHomeViewModel
    private var isLoading : Boolean = false
    private var page : Int = 1
    private var totalPages : Int = 0
    private var searchPropertyList = ArrayList<OwnerPropertyMainListingOwner>()
    private lateinit var propertyMainAdapterOwner : PropertyMainAdapterOwner

    private var currentSearchItem = ""

    override val layoutId : Int
        get() = R.layout.activity_search_property
    override val setToolbar : Boolean
        get() = false
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        val data = intent
        if (data != null) {
            currentSearchItem = data.getStringExtra(Constants.SEARCH_PROPERTY_NAME).toString()
        }
        ownerHomeViewModel.homeSearch(page.toString() , currentSearchItem)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        ownerHomeViewModel = OwnerHomeViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {

        ownerHomeViewModel.getHomeSearchResponse().observe(this , androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    if (searchPropertyList.size == 0) {
                        dismissProgressOwner()
                    }

                    if(it.data!!.response != null)
                    {
                        totalPages = it.data.response.total_page_count
                        if (searchPropertyList.size != 0) {
                            isLoading = false
                            page += 1
                            searchPropertyList.removeAt(searchPropertyList.size - 1)
                            propertyMainAdapterOwner.notifyItemRemoved(searchPropertyList.size)
                            searchPropertyList.addAll(it.data.response.owner)
                            propertyMainAdapterOwner.notifyDataSetChanged()
                        } else {
                            page += 1
                            searchPropertyList =
                                it.data.response.owner as ArrayList<OwnerPropertyMainListingOwner>
                            setRecyclerView()

                            llEmptyData3.isVisible = searchPropertyList.size == 0
                        }
                    }

                }
                Status.LOADING -> {
                    if (searchPropertyList.size == 0) {
                        showProgressOwner()
                    }
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this , getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this ,
                        getString(R.string.data_empty) ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_LONG
                    )
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this , getString(R.string.internal_server_error) ,
                        Toaster.State.ERROR , Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    override fun onClicks() {
        ivClose.setOnClickListener {
            this.onBackPressed()
        }
    }

    private fun setRecyclerView() {
        rvPropertyList.layoutManager = LinearLayoutManager(this)
        propertyMainAdapterOwner = PropertyMainAdapterOwner(this , searchPropertyList)
        rvPropertyList.adapter = propertyMainAdapterOwner
        rvPropertyList.scheduleLayoutAnimation()

        rvPropertyList.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            searchPropertyList.add(OwnerPropertyMainListingOwner(
                -1 ,
                listOf(OwnerPropertyMainListingBuildingApartment(
                    OwnerPropertyMainListingPropertyPriorityImage("" , -1) ,
                    -1 ,
                    -1 ,
                    OwnerPropertyMainListingPropertyPriorityType(-1 , "") ,
                    -1)) ,
                -1 , "" , -1 , "" , -1 , "" , -1 , "" , -1 , "" , "" ,
                OwnerPropertyMainListingPropertyPriorityImage("" , -1) , "" , -1 , "" , -1
            ))

            propertyMainAdapterOwner.notifyItemInserted(searchPropertyList.size - 1)
            ownerHomeViewModel.homeSearch(page.toString() , currentSearchItem)
        }
    }

}