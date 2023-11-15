package com.property.propertyuser.ui.main.my_property.owned

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentOwnedBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.my_property_list.BookedProperty
import com.property.propertyuser.modal.my_property_list.PropertyPriorityImage
import com.property.propertyuser.modal.my_property_list.UserPropertyRelated
import com.property.propertyuser.ui.main.maintenance.MaintenanceActivity
import com.property.propertyuser.ui.main.my_property.MyPropertyActivity
import com.property.propertyuser.ui.main.my_property.owned.adapter.OwnedAdapter
import com.property.propertyuser.ui.main.my_property.view_details.ViewDetailsActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.addOnScrolledToEnd
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_owned.*
import kotlinx.android.synthetic.main.layout_no_network.*

class OwnedFragment : BaseFragment() {
    private var pageNo: Int = 0
    private var isLoading: Boolean = false
    private var totalPageCount = 0
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var ownedAdapter: OwnedAdapter
    private lateinit var ownedViewModel: OwnedViewModel
    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentOwnedBinding
    private var bookedPropertyList = ArrayList<BookedProperty>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOwnedBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /* return inflater?.inflate(R.layout.fragment_owned,container,false)*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as MyPropertyActivity

    }

    override fun initData() {
        binding.swipeToRefresh.isRefreshing = false
        pageNo = 1
        bookedPropertyList = ArrayList()
        ownedViewModel.fetchMyPropertiesList(pageNo.toString(), "1")
    }

    private fun setOwnedListRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext())
        rvOwnedList.layoutManager = layoutManager
        ownedAdapter = OwnedAdapter(requireContext(),
            bookedPropertyList,
            { id, user_property_id -> funServiceRequest(id, user_property_id) },
            { functionViewDetails(it) })
        rvOwnedList.adapter = ownedAdapter

        rvOwnedList.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (pageNo <= totalPageCount) {
            bookedPropertyList.add(
                BookedProperty(
                    -1, PropertyPriorityImage("", -1), "",
                    "", "", -1, "", UserPropertyRelated(-1, -1), ""
                ,-1)
            )
            ownedAdapter.notifyItemInserted(bookedPropertyList.size - 1)
            ownedViewModel.fetchMyPropertiesList(pageNo.toString(), "0")
        }

    }

    private fun functionViewDetails(it: Int) {
        Log.e("inFragment", "inside")
        val intent = Intent(context, ViewDetailsActivity::class.java)
        intent.putExtra("viewDetails", "owned")
        intent.putExtra("user_property_id", it.toString())
        context?.startActivity(intent)
    }

    private fun funServiceRequest(id: Int, user_property_id: Int) {
        val intent = Intent(context, MaintenanceActivity::class.java)
        intent.putExtra("property_id", id.toString())
        intent.putExtra("user_property_id", user_property_id.toString())
        intent.putExtra("statuscode", "default")
        startActivity(intent)
    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.title_booked))
    }

    override fun setupViewModel() {
        ownedViewModel = OwnedViewModel()
    }

    override fun setupObserver() {
        ownedViewModel.getMyPropertiesListResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvOwnedList.visibility = View.GONE
                    /*if(bookedPropertyList.size==0){
                        showLoader()
                    }*/
                }
                Status.SUCCESS -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.GONE
                    binding.rvOwnedList.visibility = View.VISIBLE

                    /* if(bookedPropertyList.size==0){
                         dismissLoader()
                     }*/
                    Log.e("responseList", Gson().toJson(it))
                    if (it.data!!.booked_data != null) {
                        totalPageCount = it.data.booked_data.total_page_count
                        if (bookedPropertyList.size != 0) {
                            isLoading = false
                            pageNo += 1
                            bookedPropertyList.removeAt(bookedPropertyList.size - 1)
                            ownedAdapter.notifyItemRemoved(bookedPropertyList.size)
                            if (!(it.data.booked_data.booked_property.isNullOrEmpty())) {
                                bookedPropertyList.addAll(it.data.booked_data.booked_property)
                                ownedAdapter.notifyDataSetChanged()
                            }
                        } else {
                            if (!(it.data.booked_data.booked_property.isNullOrEmpty())) {
                                includeNoInternet.visibility = View.GONE
                                rvOwnedList.visibility = View.VISIBLE
                                linearNoDataFoundOwned.visibility = View.GONE
                                pageNo += 1
                                bookedPropertyList =
                                    it.data.booked_data.booked_property as ArrayList<BookedProperty>
                                setOwnedListRecyclerView()
                            } else {
                                rvOwnedList.visibility = View.GONE
                                linearNoDataFoundOwned.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvOwnedList.visibility = View.GONE
                    //dismissLoader()
                    Toaster.showToast(
                        requireContext(), getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvOwnedList.visibility = View.GONE
                    //dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    binding.swipeToRefresh.isRefreshing = false
                    //dismissLoader()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternet.visibility = View.VISIBLE
                        rvOwnedList.visibility = View.GONE
                        linearNoDataFoundOwned.visibility = View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternet.visibility = View.GONE
                rvOwnedList.visibility = View.VISIBLE
                pageNo = 1
                ownedViewModel.fetchMyPropertiesList(pageNo.toString(), "1")
            }
        }

        binding.swipeToRefresh.setOnRefreshListener {
            pageNo = 1
            bookedPropertyList.clear()
            ownedViewModel.fetchMyPropertiesList(pageNo.toString(), "1")
        }
    }
}