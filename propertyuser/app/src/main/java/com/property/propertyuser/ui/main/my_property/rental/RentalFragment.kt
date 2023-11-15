package com.property.propertyuser.ui.main.my_property.rental

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
import com.property.propertyuser.databinding.FragmentRentalBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.my_property_list.BookedProperty
import com.property.propertyuser.modal.my_property_list.PropertyPriorityImage
import com.property.propertyuser.modal.my_property_list.UserPropertyRelated
import com.property.propertyuser.ui.main.maintenance.MaintenanceActivity
import com.property.propertyuser.ui.main.my_property.MyPropertyActivity
import com.property.propertyuser.ui.main.my_property.rental.adapter.RentalAdapter
import com.property.propertyuser.ui.main.my_property.view_details.ViewDetailsActivity
import com.property.propertyuser.ui.main.my_property.view_details.vacate_request.VacateRequestActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.addOnScrolledToEnd
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_rental.*
import kotlinx.android.synthetic.main.layout_no_network.*

class RentalFragment : BaseFragment() {
    private var pageNo: Int = 0
    private var isLoading: Boolean = false
    private var totalPageCount = 0
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var rentalViewModel: RentalViewModel
    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentRentalBinding
    private var bookedPropertyList = ArrayList<BookedProperty>()
    private lateinit var rentalAdapter: RentalAdapter

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRentalBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_rental,container,false)*/
    }

    override fun initData() {
        binding.swipeToRefresh.isRefreshing = false
        pageNo = 1
        bookedPropertyList = ArrayList()
        rentalViewModel.fetchMyPropertiesList(pageNo.toString(), "0")
    }

    private fun setRentalListRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext())
        rvRentalList.layoutManager = layoutManager
        rentalAdapter = RentalAdapter(requireContext(),
            bookedPropertyList,
            { property_id, user_property_id -> functionService(property_id, user_property_id) },
            { functionVacate(it) },
            { functionViewDetails(it) })
        rvRentalList.adapter = rentalAdapter

        rvRentalList.addOnScrolledToEnd {
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
            rentalAdapter.notifyItemInserted(bookedPropertyList.size - 1)
            rentalViewModel.fetchMyPropertiesList(pageNo.toString(), "0")
        }

    }

    private fun functionViewDetails(it: Int) {
        Log.e("inFragment", "inside")
        val intent = Intent(context, ViewDetailsActivity::class.java)
        intent.putExtra("viewDetails", "rental")
        intent.putExtra("user_property_id", it.toString())
        context?.startActivity(intent)
    }

    private fun functionVacate(it: Int) {
        val intent = Intent(context, VacateRequestActivity::class.java)
        intent.putExtra("user_property_id", it.toString())
        startActivity(intent)
    }

    private fun functionService(proprty_id: Int, user_property_id: Int) {
        val intent = Intent(context, MaintenanceActivity::class.java)
        intent.putExtra("property_id", proprty_id.toString())
        intent.putExtra("user_property_id", user_property_id.toString())
        intent.putExtra("statuscode", "default")
        startActivity(intent)
    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.title_booked))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as MyPropertyActivity

    }

    override fun setupViewModel() {
        rentalViewModel = RentalViewModel()
    }

    override fun setupObserver() {
        rentalViewModel.getMyPropertiesListResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvRentalList.visibility = View.GONE

                    /* if(bookedPropertyList.size==0){
                         showLoader()
                     }*/
                }
                Status.SUCCESS -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.GONE
                    binding.rvRentalList.visibility = View.VISIBLE

                    /*  if(bookedPropertyList.size==0){
                          dismissLoader()
                      }*/
                    Log.e("responseList", Gson().toJson(it))
                    if (it.data!!.booked_data != null) {
                        totalPageCount = it.data.booked_data.total_page_count
                        if (bookedPropertyList.size != 0) {
                            isLoading = false
                            pageNo += 1
                            bookedPropertyList.removeAt(bookedPropertyList.size - 1)
                            rentalAdapter.notifyItemRemoved(bookedPropertyList.size)
                            if (!(it.data.booked_data.booked_property.isNullOrEmpty())) {
                                bookedPropertyList.addAll(it.data.booked_data.booked_property)
                                rentalAdapter.notifyDataSetChanged()
                            }
                        } else {
                            if (!(it.data.booked_data.booked_property.isNullOrEmpty())) {
                                includeNoInternet.visibility = View.GONE
                                rvRentalList.visibility = View.VISIBLE
                                linearNoDataFoundRental.visibility = View.GONE
                                pageNo += 1
                                bookedPropertyList =
                                    it.data.booked_data.booked_property as ArrayList<BookedProperty>
                                setRentalListRecyclerView()
                            } else {
                                rvRentalList.visibility = View.GONE
                                linearNoDataFoundRental.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvRentalList.visibility = View.GONE
                    //dismissLoader()
                    Toaster.showToast(
                        requireContext(), getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    binding.swipeToRefresh.isRefreshing = false
                    binding.shimmer.visibility = View.VISIBLE
                    binding.rvRentalList.visibility = View.GONE
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
                        rvRentalList.visibility = View.GONE
                        linearNoDataFoundRental.visibility = View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternet.visibility = View.GONE
                rvRentalList.visibility = View.VISIBLE
                pageNo = 1
                rentalViewModel.fetchMyPropertiesList(pageNo.toString(), "0")
            }
        }

        binding.swipeToRefresh.setOnRefreshListener {
            pageNo = 1
            bookedPropertyList.clear()
            rentalViewModel.fetchMyPropertiesList(pageNo.toString(), "0")
        }
    }
}