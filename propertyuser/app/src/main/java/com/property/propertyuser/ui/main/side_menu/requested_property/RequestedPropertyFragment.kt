package com.property.propertyuser.ui.main.side_menu.requested_property

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
import com.property.propertyuser.databinding.FragmentReferAFriendBinding
import com.property.propertyuser.databinding.FragmentRequestedPropertyBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.desired_property_request_list.Requested
import com.property.propertyuser.modal.user_notification.Notification
import com.property.propertyuser.ui.main.my_property.booked.adapter.BookedAdapter
import com.property.propertyuser.ui.main.side_menu.SideMenuActivity
import com.property.propertyuser.ui.main.side_menu.find_property.FindPropertyFragment
import com.property.propertyuser.ui.main.side_menu.requested_property.adapter.RequestedPropertyTicketAdapter
import com.property.propertyuser.ui.main.side_menu.requested_property.generated_ticket_requested_property.GenerateTicketRequestedPropertyFragment
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.fragment_requested_property.*
import kotlinx.android.synthetic.main.layout_no_network.*

class RequestedPropertyFragment: BaseFragment()  {
    private lateinit var requestedPropertyViewModel: RequestedPropertyViewModel
    private lateinit var binding: FragmentRequestedPropertyBinding
    private lateinit var activityListener: ActivityListener
    private var isLoading: Boolean = false
    private var page=0
    private var totalPageCount=0
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var requestedPropertyTicketAdapter: RequestedPropertyTicketAdapter
    private var  requestedList= ArrayList<Requested>()
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRequestedPropertyBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
       /* return inflater?.inflate(R.layout.fragment_requested_property,container,false)*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener=activity as SideMenuActivity

    }
    override fun initData() {
        page=1
        activityListener.setTitle(getString(R.string.requestedProperty))
        requestedList= ArrayList()
        requestedPropertyViewModel.fetchDesiredRequestedPropertyList(page.toString())
    }
    private fun setRequestedPropertyTicketListRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext())
        rvRequestedProperty.layoutManager = layoutManager
        requestedPropertyTicketAdapter=RequestedPropertyTicketAdapter(requireContext(),requestedList) {position->
            activityListener.
            navigateToFragment(GenerateTicketRequestedPropertyFragment.newInstance(position.toString()))}
        rvRequestedProperty.adapter = requestedPropertyTicketAdapter
        rvRequestedProperty.addOnScrolledToEnd{
            if (!isLoading) {
                Log.e("end","reached")
                loadData()
                isLoading = true
            }
        }
    }
    fun loadData() {
        if(page<=totalPageCount && requireContext().isConnected){
            requestedList.add(Requested(-1,""))
            requestedPropertyTicketAdapter.notifyItemInserted(requestedList.size - 1)
            requestedPropertyViewModel.fetchDesiredRequestedPropertyList(page.toString())
        }

    }
    override fun setupUI() {

    }

    override fun setupViewModel() {
        requestedPropertyViewModel= RequestedPropertyViewModel()
    }

    override fun setupObserver() {
        requestedPropertyViewModel.getDesiredRequestedPropertyListResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    if(requestedList.size==0){
                        showLoader()
                    }
                }
                Status.SUCCESS->{
                    if(requestedList.size==0){
                        dismissLoader()
                    }
                    includeNoInternet.visibility=View.GONE
                    if(it.data!=null){
                        Log.e("response event pacage", Gson().toJson(it))
                        if(it.data.data!=null){
                            totalPageCount=it.data.data.total_page_count
                            if(requestedList.size!=0){
                                isLoading = false
                                page+=1
                                requestedList.removeAt(requestedList.size - 1)
                                requestedPropertyTicketAdapter.notifyItemRemoved(requestedList.size)
                                if(!(it.data.data.requested_list.isNullOrEmpty())){
                                    requestedList.addAll(it.data!!.data!!.requested_list)
                                    requestedPropertyTicketAdapter.notifyDataSetChanged()
                                }
                            }
                            else{
                                if(!(it.data.data.requested_list.isNullOrEmpty())){
                                    requestPropertyMainLayout.visibility=View.VISIBLE
                                    linearNoDataFound.visibility=View.GONE
                                    requestedList=it.data.data.requested_list as ArrayList<Requested>
                                    page+=1
                                    setRequestedPropertyTicketListRecyclerView()
                                }
                                else{
                                    requestPropertyMainLayout.visibility=View.GONE
                                    linearNoDataFound.visibility=View.VISIBLE
                                }
                            }
                        }
                    }
                    else{
                        requestPropertyMainLayout.visibility=View.GONE
                        linearNoDataFound.visibility=View.VISIBLE
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(requireContext().isConnected){
                        Toaster.showToast(requireContext(),getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        includeNoInternet.visibility=View.VISIBLE
                        requestPropertyMainLayout.visibility=View.GONE
                        linearNoDataFound.visibility=View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(requireContext().isConnected){
                includeNoInternet.visibility=View.GONE
                requestPropertyMainLayout.visibility=View.VISIBLE
                linearNoDataFound.visibility=View.GONE
                requestedPropertyViewModel.fetchDesiredRequestedPropertyList("")
            }
        }
    }
}