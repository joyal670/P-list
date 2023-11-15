package com.property.propertyuser.ui.main.my_property.booked

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
import com.property.propertyuser.databinding.FragmentBookedBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.my_property_list.BookedProperty
import com.property.propertyuser.modal.my_property_list.PropertyPriorityImage
import com.property.propertyuser.modal.my_property_list.UserPropertyRelated
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.my_property.MyPropertyActivity
import com.property.propertyuser.ui.main.my_property.booked.adapter.BookedAdapter
import com.property.propertyuser.ui.main.my_property.view_details.ViewDetailsActivity
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.fragment_booked.*
import kotlinx.android.synthetic.main.layout_no_network.*

class BookedFragment : BaseFragment() {
    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentBookedBinding
    private lateinit var bookedViewModel: BookedViewModel
    private var pageNo: Int = 0
    private var isLoading: Boolean = false
    private var totalPageCount = 0
    private lateinit var layoutManager: LinearLayoutManager
    private var bookedPropertyList = ArrayList<BookedProperty>()
    private lateinit var bookedAdapter: BookedAdapter
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookedBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_booked,container,false)*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as MyPropertyActivity

    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.reload_booked_property_list) {
            AppPreferences.reload_booked_property_list = false
            pageNo = 1
            bookedViewModel.fetchMyPropertiesList(pageNo.toString(), "2")
        }
    }

    override fun initData() {
        binding.swipeToRefresh.isRefreshing = false
        pageNo = 1
        bookedPropertyList = ArrayList()
        bookedViewModel.fetchMyPropertiesList(pageNo.toString(), "2")
    }

    private fun setBookedListRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext())
        rvBookedList.layoutManager = layoutManager
        bookedAdapter = BookedAdapter(requireContext(), bookedPropertyList,
            { viewSelectedPropertyDetails(it) }, { selectedProperty(it) })
        rvBookedList.adapter = bookedAdapter
        rvBookedList.addOnScrolledToEnd {
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
            bookedAdapter.notifyItemInserted(bookedPropertyList.size - 1)
            bookedViewModel.fetchMyPropertiesList(pageNo.toString(), "0")
        }

    }

    private fun viewSelectedPropertyDetails(propertyId: Int) {
        val intent = Intent(requireContext(), PropertyDetailsActivity::class.java)
        intent.putExtra(Constants.INTENT_PROPERTY_ID, propertyId)
        startActivity(intent)
    }

    private fun selectedProperty(propertyId: Int) {
        Log.e("inFragment", "inside")
        val intent = Intent(context, ViewDetailsActivity::class.java)
        intent.putExtra("viewDetails", "booked")
        intent.putExtra("property_id", propertyId.toString())
        context?.startActivity(intent)
    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.title_booked))
    }

    override fun setupViewModel() {
        bookedViewModel = BookedViewModel()
    }

    override fun setupObserver() {
        bookedViewModel.getMyPropertiesListResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvBookedList.visibility = View.GONE
                    /* if(bookedPropertyList.size==0){
                         showLoader()
                     }*/
                }
                Status.SUCCESS -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.GONE
                    binding.rvBookedList.visibility = View.VISIBLE
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
                            bookedAdapter.notifyItemRemoved(bookedPropertyList.size)
                            if (!(it.data.booked_data.booked_property.isNullOrEmpty())) {
                                bookedPropertyList.addAll(it.data.booked_data.booked_property)
                                bookedAdapter.notifyDataSetChanged()
                            }
                        } else {
                            if (!(it.data.booked_data.booked_property.isNullOrEmpty())) {
                                includeNoInternet.visibility = View.GONE
                                rvBookedList.visibility = View.VISIBLE
                                linearNoDataFoundBooked.visibility = View.GONE
                                pageNo += 1
                                bookedPropertyList =
                                    it.data.booked_data.booked_property as ArrayList<BookedProperty>
                                setBookedListRecyclerView()
                            } else {
                                rvBookedList.visibility = View.GONE
                                linearNoDataFoundBooked.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvBookedList.visibility = View.GONE
                    //dismissLoader()
                    Toaster.showToast(
                        requireContext(), getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvBookedList.visibility = View.GONE
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
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternet.visibility = View.VISIBLE
                        rvBookedList.visibility = View.GONE
                        linearNoDataFoundBooked.visibility = View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternet.visibility = View.GONE
                rvBookedList.visibility = View.VISIBLE
                pageNo = 1
                bookedViewModel.fetchMyPropertiesList(pageNo.toString(), "2")
            }
        }

        binding.swipeToRefresh.setOnRefreshListener {
            pageNo = 1
            bookedPropertyList.clear()
            bookedViewModel.fetchMyPropertiesList(pageNo.toString(), "2")
        }
    }
}