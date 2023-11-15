package com.property.propertyuser.ui.main.side_menu.become_owner

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FargmentBecomeOwnerBinding
import com.property.propertyuser.databinding.FragmentSalesDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.fetch_requested_property_data.PropertyCategory
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.side_menu.SideMenuActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import com.skydoves.powerspinner.SpinnerGravity
import kotlinx.android.synthetic.main.fargment_become_owner.*
import kotlinx.android.synthetic.main.fragment_find_property.*


class BecomeOwnerFragment:BaseFragment() {
    private lateinit var becomeOwnerViewModel: BecomeOwnerViewModel
    private lateinit var binding: FargmentBecomeOwnerBinding
    private lateinit var activityListener: ActivityListener
    private var relationShipSelected=false
    private var selectedRentalCount=""
    private var selectedSaleCount=""
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FargmentBecomeOwnerBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
       /* return inflater?.inflate(R.layout.fargment_become_owner,container,false)*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener=activity as SideMenuActivity

    }
    override fun initData() {
        activityListener.setTitle(getString(R.string.becomeOwner))
        if(AppPreferences.chooseLanguage=="arabic"){
            spPropertyRelated.arrowGravity= SpinnerGravity.START
        }else{
            spPropertyRelated.arrowGravity= SpinnerGravity.END
        }
        numberOfRentalPropertiesDropDown()
        numberOfSalePropertiesDropDown()
        setupPropertyRelationShip()
    }
    private fun numberOfRentalPropertiesDropDown() {
        val numberOfRentalProperties = arrayOf(0,1,2,3,4,5,6,7,8,9,10)
        val adapterRental =
            context?.let { ArrayAdapter(it,android.R.layout.simple_spinner_dropdown_item,numberOfRentalProperties) }
        spRentalCount.adapter = adapterRental

        binding.spRentalCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedRentalCount = numberOfRentalProperties[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
    private fun numberOfSalePropertiesDropDown() {
        val numberOfSaleProperties = arrayOf(0,1,2,3,4,5,6,7,8,9,10)
        val adapterSales =
            context?.let { ArrayAdapter(it,android.R.layout.simple_spinner_dropdown_item,numberOfSaleProperties) }
        spSaleCount.adapter = adapterSales

        binding.spSaleCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSaleCount = numberOfSaleProperties[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
    private fun setupPropertyRelationShip(){
        val relationShip= listOf<String>(getString(R.string.owner),getString(R.string.agent))
        spPropertyRelated.setItems(relationShip)
        spPropertyRelated.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            relationShipSelected=true
            //Toast.makeText(context,newText, Toast.LENGTH_SHORT).show()
        }
    }
    override fun setupUI() {

    }

    override fun setupViewModel() {
        becomeOwnerViewModel= BecomeOwnerViewModel()
    }

    override fun setupObserver() {
        becomeOwnerViewModel.getBecomeOwnerResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    if(it.data!=null){
                        Log.e("response event pacage", Gson().toJson(it))
                        Toaster.showToast(requireContext(),it.data.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                        activity?.finish()
                    }
                    else{
                        Toaster.showToast(requireContext(),it.status.toString(),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),it.data!!.response,
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(requireContext().isConnected){
                        Toaster.showToast(requireContext(),getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(requireContext(),getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
    }

    override fun onClicks() {

        binding.btnDiscard.setOnClickListener {
            val intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
            binding.tvOwnerName.requestFocus()
        }
        binding.btnRequest.setOnClickListener {

            if(binding.tvOwnerName.text.trim().toString().isEmpty()){
                Toaster.showToast(requireContext(),getString(R.string.name_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(binding.tvOwnerMobile.text.trim().toString().isEmpty()){
                Toaster.showToast(requireContext(),getString(R.string.phone_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(binding.tvOwnerEmail.text.trim().toString().isEmpty()){
                Toaster.showToast(requireContext(),getString(R.string.email_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(binding.tvOwnerMobile.text.trim().toString().isEmpty()){
                Toaster.showToast(requireContext(),getString(R.string.phone_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if((selectedRentalCount.isEmpty())&&(selectedSaleCount.isEmpty())){
                Toaster.showToast(requireContext(),getString(R.string.no_of_properties_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(!relationShipSelected){
                Toaster.showToast(requireContext(),getString(R.string.relationship_not_selected),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else{
                becomeOwnerViewModel.becomeOwner(binding.tvOwnerName.text.trim().toString(),
                    binding.tvOwnerMobile.text.trim().toString(),binding.tvOwnerEmail.text.trim().toString(),
                    selectedRentalCount,selectedSaleCount,spPropertyRelated.text.trim().toString())
            }
        }
    }
}