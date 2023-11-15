package com.property.propertyuser.ui.main.my_property.view_details.booked_view_details

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentBookedViewDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.booking.book_a_tour.BookATourFragment
import com.property.propertyuser.ui.main.my_property.view_details.ViewDetailsActivity
import com.property.propertyuser.ui.main.my_property.view_details.booked_view_details.adapter.BookedImageUploadAdapter
import com.property.propertyuser.ui.main.payment.PaymentActivity
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.fragment_booked_view_details.*

class BookedViewDetailsFragment:BaseFragment() {
    private var propertyIdPassed=""
    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentBookedViewDetailsBinding
    private lateinit var bookedViewDetailsViewModel: BookedViewDetailsViewModel
    private lateinit var imageAdapter : BookedImageUploadAdapter
    private var images : ArrayList<String?>? = null
    private var imagesList : ArrayList<Uri?>? = null
    private var position = 0
    private var userPropertyId=""
    companion object {
        const val ARG_PROPERTY_ID = "property_id"
        fun newInstance(property_id: String): BookedViewDetailsFragment {
            val fragment = BookedViewDetailsFragment()

            val bundle = Bundle().apply {
                putString(ARG_PROPERTY_ID, property_id)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        propertyIdPassed = arguments?.getString(ARG_PROPERTY_ID).toString()
        binding= FragmentBookedViewDetailsBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_booked_view_details,container,false)*/
    }

    override fun initData() {
        images = ArrayList()
        imagesList = ArrayList()
        if(imagesList!!.size<=0){
            btnUpload.visibility=View.GONE
        }
        bookedViewDetailsViewModel.proceedBookPropertyDetails(propertyIdPassed)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener=activity as ViewDetailsActivity

    }
    override fun setupUI() {

        val gridLayoutManager = GridLayoutManager(requireContext() , 4)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvAttachFileLists.layoutManager = gridLayoutManager
        imageAdapter = BookedImageUploadAdapter(requireContext() , imagesList!!) {
            images!!.removeAt(it)
            imagesList!!.removeAt(it)
            imageAdapter.notifyDataSetChanged()
            if(imagesList!!.size<=0){
                btnUpload.visibility=View.GONE
            }else{
                btnUpload.visibility=View.VISIBLE
            }
        }
        rvAttachFileLists.adapter = imageAdapter
    }

    private fun openImageFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE , true)
        intent.action = Intent.ACTION_PICK
        startForResult.launch(Intent.createChooser(intent , "Select Image(s)") )
    }
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent!!.clipData != null) {
                val count = intent.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = intent.clipData!!.getItemAt(i).uri
                    images!!.add(CommonMethods.getImageRealPath(imageUri , requireContext()))
                    imagesList!!.add(imageUri)
                    imageAdapter.notifyDataSetChanged()

                }
                position = 0
            } else {
                val imageUri = intent.data
                images!!.add(CommonMethods.getImageRealPath(imageUri , requireContext()))
                imagesList!!.add(imageUri)
                imageAdapter.notifyDataSetChanged()
                position = 0

            }
            btnUpload.visibility=View.VISIBLE
        }
    }
    override fun setupViewModel() {
        bookedViewDetailsViewModel= BookedViewDetailsViewModel()
    }

    override fun setupObserver() {
        bookedViewDetailsViewModel.getProceedBookPropertyDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    if(it.data?.property_details!=null){
                        includeNoInternetBookedViewDetails.visibility=View.GONE
                        linearNoDataFoundBookedViewDetails.visibility=View.GONE
                        constraintMainBookedViewDetails.visibility=View.VISIBLE
                        activityListener.setTitle(getString(R.string.booked_property))
                        if(it.data.property_details.user_booked_property !=null){
                            binding.tvRentalAmount.text=
                                it.data?.property_details.user_booked_property?.rent+" "+context?.getString(R.string.sar)
                            binding.tvDueDateforPay.text=
                                it.data.property_details.user_booked_property.due_date
                            userPropertyId= it.data.property_details.user_booked_property.id.toString()
                        }
                        if(it.data?.property_details?.documents!=null){
                            binding.roundedPropertyImage.
                            loadImagesWithGlideExt(it.data?.property_details?.documents[0].document)
                        }
                        binding.tvPropertyCode.text=it.data?.property_details?.property_reg_no

                        when (it.data.property_details.user_booked_property.status) {
                            0 -> {
                                /* disable pay now button */
                                binding.btnPayNow.visibility = View.GONE
                            }
                            1 -> {
                                /* disable upload button */
                                binding.ivUploadCloud.visibility = View.GONE
                                binding.tvUploadDocumentsText.visibility = View.GONE
                                binding.btnAttachFile.visibility = View.GONE
                                binding.rvAttachFileLists.visibility = View.GONE
                                binding.btnUpload.visibility = View.GONE
                            }
                            2 -> {

                            }
                        }
                    }
                    else{
                        includeNoInternetBookedViewDetails.visibility=View.GONE
                        linearNoDataFoundBookedViewDetails.visibility=View.VISIBLE
                        constraintMainBookedViewDetails.visibility=View.GONE
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    includeNoInternetBookedViewDetails.visibility=View.GONE
                    linearNoDataFoundBookedViewDetails.visibility=View.VISIBLE
                    constraintMainBookedViewDetails.visibility=View.GONE
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(requireContext().isConnected){
                        Toaster.showToast(requireContext(),getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        includeNoInternetBookedViewDetails.visibility=View.VISIBLE
                        linearNoDataFoundBookedViewDetails.visibility=View.GONE
                        constraintMainBookedViewDetails.visibility=View.GONE
                    }
                }

            }
        })
        bookedViewDetailsViewModel.getUploadUserPropertyDocumentsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    Toaster.showToast(requireContext(),it.data!!.response,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
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
                        Toaster.showToast(requireContext(),getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
        bookedViewDetailsViewModel.getCancelBookedPropertyResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    Toaster.showToast(requireContext(),it.data!!.response,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                    AppPreferences.reload_booked_property_list=true
                    activity?.onBackPressed()
                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),it.data!!.response, Toaster.State.ERROR, Toast.LENGTH_LONG)
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
        btnPayNow.setOnClickListener {
            if(userPropertyId!=null){
                var intent=Intent(requireContext(),PaymentActivity::class.java)
                intent.putExtra("passed_type","booked_view_details")
                intent.putExtra("passed_booking_id_or_property_id",userPropertyId)
                startActivity(intent)
            }
            else{
                Toaster.showToast(requireContext(),getString(R.string.user_property_id_missing),
                    Toaster.State.ERROR, Toast.LENGTH_LONG)
            }
        }
        btnAttachFile.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openImageFileChooser()
                }
            }
        }
        btnUpload.setOnClickListener {
            bookedViewDetailsViewModel.uploadUserPropertyDocumentsDetails(requireContext(),userPropertyId,images)
        }
        btnCancelBooking.setOnClickListener {
            bookedViewDetailsViewModel.cancelBookedPropertyDetails(userPropertyId)
        }
    }
}