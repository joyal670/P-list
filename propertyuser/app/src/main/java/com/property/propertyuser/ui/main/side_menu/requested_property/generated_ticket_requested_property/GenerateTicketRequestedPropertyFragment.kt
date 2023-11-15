package com.property.propertyuser.ui.main.side_menu.requested_property.generated_ticket_requested_property

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
import com.property.propertyuser.databinding.FargmentRequestedPropertyGenerateTicketBinding
import com.property.propertyuser.databinding.FragmentIntro3Binding
import com.property.propertyuser.databinding.FragmentRequestedPropertyBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.desired_property_request_details.DesiredProperty
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.favorites.FavoritePropertyViewModel
import com.property.propertyuser.ui.main.property_details.PropertyDetailViewModel
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyImageFragment
import com.property.propertyuser.ui.main.side_menu.SideMenuActivity
import com.property.propertyuser.ui.main.side_menu.requested_property.RequestedPropertyFragment
import com.property.propertyuser.ui.main.side_menu.requested_property.adapter.RequestedPropertyTicketAdapter
import com.property.propertyuser.ui.main.side_menu.requested_property.generated_ticket_requested_property.adapter.GeneratedTicketRequestedPropertyDetailsAdapter
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.fargment_requested_property_generate_ticket.*
import kotlinx.android.synthetic.main.fargment_requested_property_generate_ticket.includeNoInternet
import kotlinx.android.synthetic.main.fargment_requested_property_generate_ticket.linearNoDataFound
import kotlinx.android.synthetic.main.layout_no_network.*

class GenerateTicketRequestedPropertyFragment: BaseFragment() {
    private lateinit var binding: FargmentRequestedPropertyGenerateTicketBinding
    private lateinit var generatedTicketRequestedPropertyViewModel: GeneratedTicketRequestedPropertyViewModel
    private lateinit var propertyDetailViewModel: PropertyDetailViewModel
    private var desiredPropertyList=ArrayList<DesiredProperty>()
    private lateinit var activityListener: ActivityListener
    private var id=""
    private var passedPosition=-1
    private var favoritePropertyIdList=ArrayList<Int>()
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FargmentRequestedPropertyGenerateTicketBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fargment_requested_property_generate_ticket,container,false)*/
    }
    companion object{
        fun newInstance(requested_id:String)= GenerateTicketRequestedPropertyFragment().apply {
            arguments=Bundle().apply {
                putString(Constants.ARG_REQUESTED_ID,requested_id)
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener=activity as SideMenuActivity

    }
    override fun initData() {
        activityListener.setTitle("Ticket ID: 2102102")
        id=arguments?.getString(Constants.ARG_REQUESTED_ID).toString()
        Log.e("id",id)
        desiredPropertyList=ArrayList()
        favoritePropertyIdList=ArrayList()
        generatedTicketRequestedPropertyViewModel.fetchRequestedPropertyDetails(id)
    }
    private fun setGeneratedTicketRequestedPropertyTicketListRecyclerView() {
        rvRequestPropertyGeneratedTicketList.layoutManager = LinearLayoutManager(context)
        rvRequestPropertyGeneratedTicketList.adapter = GeneratedTicketRequestedPropertyDetailsAdapter(requireContext(),
            desiredPropertyList,
            { position ->
                val intent=Intent(requireContext(),PropertyDetailsActivity::class.java)
                intent.putExtra(Constants.INTENT_PROPERTY_ID,position)
                intent.putExtra("propertyType","")
                startActivity(intent)
            },{it,pos->functionAddToFavSimilar(it,pos)},{it1,pos1->functionRemoveFavSimilar(it1,pos1)})
    }
    private fun functionRemoveFavSimilar(remFav: Int, pos1: Int) {
        if(favoritePropertyIdList.contains(remFav)) {
            favoritePropertyIdList.remove(remFav)
        }
        propertyDetailViewModel.updateFavoriteProperty(remFav)
    }

    private fun functionAddToFavSimilar(addFav: Int, pos: Int) {
        if(favoritePropertyIdList.contains(addFav)){
            favoritePropertyIdList.remove(addFav)
        }else{
            favoritePropertyIdList.add(addFav)
        }
        propertyDetailViewModel.updateFavoriteProperty(addFav)
    }
    override fun setupUI() {

    }

    override fun setupViewModel() {
        generatedTicketRequestedPropertyViewModel= GeneratedTicketRequestedPropertyViewModel()
        propertyDetailViewModel= PropertyDetailViewModel()
    }

    override fun setupObserver() {
        generatedTicketRequestedPropertyViewModel.getRequestedPropertyDetailsResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    includeNoInternet.visibility=View.GONE
                    if(it.data!=null){
                        Log.e("response event pacage", Gson().toJson(it))
                        if (it.data.data!=null){
                            linearMainRequestPropertyTicket.visibility=View.VISIBLE
                            linearNoDataFound.visibility=View.GONE
                            if(it.data.data.request_details!=null){
                                activityListener.setTitle("Ticket ID: "+it.data.data.request_details.request_id_no.toString())
                                tvTicketRaiseOnDate.text=it.data.data.request_details.request_date
                                if(it.data.data.request_details.property_to==0){
                                    tvPropertyTo.text=getString(R.string.property_for_rent)
                                }else{
                                    tvPropertyTo.text=getString(R.string.property_for_sale)
                                }
                                tvPreferedLocation.text=it.data.data.request_details.location
                                if(it.data.data.request_details.property_type!=null){
                                    tvDesiredPropertyType.text=
                                        it.data.data.request_details.property_type.type
                                }

                            }
                            if(!(it.data.data.desired_properties.isNullOrEmpty())){
                                desiredPropertyList=it.data.data.desired_properties as ArrayList<DesiredProperty>
                                setGeneratedTicketRequestedPropertyTicketListRecyclerView()
                            }
                            else{
                                nestedGeneratedTicketRequestedProperty.snack(getString(R.string.no_property_found))
                            }
                        }
                        else{
                            linearMainRequestPropertyTicket.visibility=View.GONE
                            linearNoDataFound.visibility=View.VISIBLE
                        }
                    }
                    else{
                        linearMainRequestPropertyTicket.visibility=View.GONE
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
                        linearMainRequestPropertyTicket.visibility=View.GONE
                        linearNoDataFound.visibility=View.GONE
                    }
                }

            }
        })
        propertyDetailViewModel.updateFavoritePropertyResponse().observe(this, Observer   {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    AppPreferences.reload_property_list=true
                    nestedGeneratedTicketRequestedProperty.snack(it.data!!.response)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),it.data!!.response, Toaster.State.ERROR,Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(requireContext().isConnected){
                        Toaster.showToast(requireContext(),getString(R.string.something_wrong),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(requireContext(),getString(R.string.no_internet),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(requireContext().isConnected){
                includeNoInternet.visibility=View.GONE
                linearMainRequestPropertyTicket.visibility=View.VISIBLE
                linearNoDataFound.visibility=View.GONE
                generatedTicketRequestedPropertyViewModel.fetchRequestedPropertyDetails(id)
            }
        }
    }
}