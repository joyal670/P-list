package com.property.propertyuser.ui.main.my_property.scheduled

import android.content.Context
import android.content.Intent
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
import com.property.propertyuser.databinding.FragmentScheduledBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.scheduled_property.BookedTour
import com.property.propertyuser.modal.scheduled_property.PropertyDetails
import com.property.propertyuser.modal.scheduled_property.PropertyPriorityImage
import com.property.propertyuser.ui.main.my_property.MyPropertyActivity
import com.property.propertyuser.ui.main.my_property.scheduled.adapter.ScheduledAdapter
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.fragment_scheduled.*
import kotlinx.android.synthetic.main.layout_no_network.*

class ScheduledFragment : BaseFragment() {
    private lateinit var scheduledViewModel: ScheduledViewModel
    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentScheduledBinding
    private lateinit var scheduledAdapter: ScheduledAdapter
    private var scheduledPropertyList = ArrayList<BookedTour>()
    private var isLoading: Boolean = false
    private var i = 1
    private var totalPageCount = 0
    private lateinit var layoutManager: LinearLayoutManager

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduledBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        binding.swipeToRefresh.isRefreshing = false
        i = 1
        scheduledPropertyList = ArrayList()
        scheduledViewModel.fetchScheduledPropertiesList(i.toString())
    }

    private fun setupScheduledRecyclerview() {
        layoutManager = LinearLayoutManager(requireContext())
        rvScheduledList.layoutManager = layoutManager
        scheduledAdapter = ScheduledAdapter(requireContext(), scheduledPropertyList) { i: Int ->
            val intent = Intent(requireContext(), PropertyDetailsActivity::class.java)
            intent.putExtra(Constants.INTENT_PROPERTY_ID, i)
            intent.putExtra("my_property_type", "scheduled")
            startActivity(intent)
        }
        rvScheduledList.adapter = scheduledAdapter

        rvScheduledList.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (i <= totalPageCount) {
            scheduledPropertyList.add(
                BookedTour(
                    "", -1,
                    PropertyDetails(-1, "", ""), -1,
                    PropertyPriorityImage("", -1, -1, -1), -1, "", -1
                )
            )
            scheduledAdapter.notifyItemInserted(scheduledPropertyList.size - 1)
            scheduledViewModel.fetchScheduledPropertiesList(i.toString())
        }

    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.title_scheduled))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as MyPropertyActivity

    }

    override fun setupViewModel() {
        scheduledViewModel = ScheduledViewModel()
    }

    override fun setupObserver() {
        scheduledViewModel.getScheduledPropertiesListResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvScheduledList.visibility = View.GONE
                    /* if(scheduledPropertyList.size==0){
                         showLoader()
                     }*/
                }
                Status.SUCCESS -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.GONE
                    binding.rvScheduledList.visibility = View.VISIBLE
                    /* if(scheduledPropertyList.size==0){
                         dismissLoader()
                     }*/

                    Log.e("responseList", Gson().toJson(it))
                    if (it.data != null) {
                        if (it.data.data != null) {
                            totalPageCount = it.data.data.total_page_count
                            if (scheduledPropertyList.size != 0) {
                                isLoading = false
                                i += 1
                                scheduledPropertyList.removeAt(scheduledPropertyList.size - 1)
                                scheduledAdapter.notifyItemRemoved(scheduledPropertyList.size)
                                if (!(it.data.data.booked_tours.isNullOrEmpty())) {
                                    scheduledPropertyList.addAll(it.data.data.booked_tours)
                                    scheduledAdapter.notifyDataSetChanged()
                                }
                            } else {
                                if (!(it.data.data.booked_tours.isNullOrEmpty())) {
                                    includeNoInternet.visibility = View.GONE
                                    rvScheduledList.visibility = View.VISIBLE
                                    linearNoDataFoundScheduled.visibility = View.GONE
                                    i += 1
                                    scheduledPropertyList =
                                        it.data.data.booked_tours as ArrayList<BookedTour>
                                    setupScheduledRecyclerview()
                                } else {
                                    rvScheduledList.visibility = View.GONE
                                    linearNoDataFoundScheduled.visibility = View.VISIBLE
                                }

                            }
                        }
                    }
                }
                Status.ERROR -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvScheduledList.visibility = View.GONE
                    //dismissLoader()
                    Toaster.showToast(
                        requireContext(), getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvScheduledList.visibility = View.GONE
                    // dismissLoader()
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
                        rvScheduledList.visibility = View.GONE
                        linearNoDataFoundScheduled.visibility = View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternet.visibility = View.GONE
                rvScheduledList.visibility = View.VISIBLE
                i = 1
                scheduledViewModel.fetchScheduledPropertiesList(i.toString())
            }
        }

        binding.swipeToRefresh.setOnRefreshListener {
            i = 1
            scheduledPropertyList.clear()
            scheduledViewModel.fetchScheduledPropertiesList(i.toString())
        }
    }
}