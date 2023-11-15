package com.property.propertyuser.ui.main.home.events_see_all

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityEventsSeeAllBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.events_list.Event
import com.property.propertyuser.modal.events_list.EventPriorityImage
import com.property.propertyuser.preference.AppPreferences.home_lat
import com.property.propertyuser.preference.AppPreferences.home_long
import com.property.propertyuser.ui.main.home.events_see_all.adapter.EventsSeeAllItemAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.addOnScrolledToEnd
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.activity_events_see_all.*
import kotlinx.android.synthetic.main.layout_no_network.*

class EventsSeeAllActivity : BaseActivity<ActivityEventsSeeAllBinding>(), ActivityListener {

    private lateinit var eventsSeeAllViewModel: EventsSeeAllViewModel
    override fun getViewBinging(): ActivityEventsSeeAllBinding =
        ActivityEventsSeeAllBinding.inflate(layoutInflater)

    private var events = ArrayList<Event>()
    private lateinit var eventsSeeAllAdapter: EventsSeeAllItemAdapter
    private var isLoading: Boolean = false
    private var i = 1
    private var totalPageCount = 0
    private lateinit var layoutManager: LinearLayoutManager


    override val layoutId: Int
        get() = R.layout.activity_events_see_all
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {

        setTitle(getString(R.string.events_title))
        events = ArrayList()
        eventsSeeAllViewModel.fetchEventsList(
            home_lat.toString(),
            home_long.toString(),
            i.toString()
        )


    }

    private fun setEventsSellAllRecyclerView(/*events: List<Event>*/) {
        layoutManager = LinearLayoutManager(this)
        rvEventsSellAllList.layoutManager = layoutManager
        eventsSeeAllAdapter = EventsSeeAllItemAdapter(this, events)
        rvEventsSellAllList.adapter = eventsSeeAllAdapter
        /*rvEventsSellAllList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    Log.e("layout",layoutManager.findLastCompletelyVisibleItemPosition().toString())
                    Log.e("homepos",(events.size - 1).toString())
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == events.size - 1) {
                        loadData()
                        isLoading = true
                    }
                }
            }
        })
        eventsSeeAllAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                layoutManager.scrollToPositionWithOffset(positionStart, 0)
            }
        })*/
        rvEventsSellAllList.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (i <= totalPageCount) {
            events.add(Event(EventPriorityImage(-1, ""), -1, "", ""))
            eventsSeeAllAdapter.notifyItemInserted(events.size - 1)
            eventsSeeAllViewModel.fetchEventsList(
                home_lat.toString(),
                home_long.toString(),
                i.toString()
            )
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        eventsSeeAllViewModel = EventsSeeAllViewModel()
    }

    override fun setupObserver() {
        eventsSeeAllViewModel.getEventsListResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    if (events.size == 0) {
                        showLoader()
                    }
                }
                Status.SUCCESS -> {
                    if (events.size == 0) {
                        dismissLoader()
                    }
                    Log.e("responsecheck", Gson().toJson(it))
                    if (it.data?.event_data != null) {
                        totalPageCount = it.data.event_data.total_page_count
                        if (events.size != 0) {
                            isLoading = false
                            i += 1
                            events.removeAt(events.size - 1)
                            eventsSeeAllAdapter.notifyItemRemoved(events.size)
                            if (!(it.data.event_data.events.isNullOrEmpty())) {
                                events.addAll(it.data.event_data.events)
                                eventsSeeAllAdapter.notifyDataSetChanged()
                            }
                        } else {
                            if (!(it.data.event_data.events.isNullOrEmpty())) {
                                includeNoInternet.visibility = View.GONE
                                rvEventsSellAllList.visibility = View.VISIBLE
                                linearNoDataFound.visibility = View.GONE
                                i += 1
                                events = it.data.event_data.events as ArrayList<Event>
                                setEventsSellAllRecyclerView()
                            } else {
                                rvEventsSellAllList.visibility = View.GONE
                                linearNoDataFound.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this,
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternet.visibility = View.VISIBLE
                        rvEventsSellAllList.visibility = View.GONE
                        linearNoDataFound.visibility = View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (this.isConnected) {
                includeNoInternet.visibility = View.GONE
                rvEventsSellAllList.visibility = View.VISIBLE
                i = 1
                eventsSeeAllViewModel.fetchEventsList(
                    home_lat.toString(),
                    home_long.toString(),
                    i.toString()
                )
            }
        }
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}