package com.property.propertyuser.ui.main.side_menu.user_profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.allShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityProfileBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.layout_no_network.*

class ProfileActivity : BaseActivity<ActivityProfileBinding>(),ActivityListener {
    private lateinit var profileViewModel: ProfileViewModel
    private var imagesPassed:String=""
    private var premissionGranded:Boolean=false
    private var placesClient: PlacesClient? = null
    private val PLACE_PICKER_REQUEST = 3
    private var passLng=0.0
    private var passLat=0.0
    override val layoutId: Int
        get() = R.layout.activity_profile
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle(getString(R.string.user_profile))
        profileViewModel.fetchProfileDetails()
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        profileViewModel= ProfileViewModel()
    }

    override fun setupObserver() {
        profileViewModel.getProfileDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    includeNoInternet.visibility=View.GONE
                    if(it.data?.data!=null){
                        nestedProfile.visibility=View.VISIBLE
                        linearNoDataFound.visibility=View.GONE
                        sivUserProfile.loadImagesProfileWithGlideExt(it.data.data.profile_pic)
                        etProfileName.setText(it.data.data.name)
                        etProfileEmail.setText(it.data.data.email)
                        etProfilePhone.setText(it.data.data.phone)
                        etProfileLocation.text = it.data.data.address
                        if(it.data.data.address.isNotEmpty()){
                            etProfileLocation.setTextColor(ContextCompat.getColor(this,R.color.black))
                        }
                        else{
                            if(it.data.data.location.isNotEmpty()){
                                etProfileLocation.setText(it.data.data.location)
                            }
                        }
                        AppPreferences.user_name=it.data.data.name
                        AppPreferences.user_email=it.data.data.email
                        AppPreferences.user_profile_image=it.data.data.profile_pic
                        AppPreferences.reload_profile_details=true
                    }
                    else{
                        nestedProfile.visibility=View.GONE
                        linearNoDataFound.visibility=View.VISIBLE
                    }

                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        includeNoInternet.visibility=View.VISIBLE
                        nestedProfile.visibility=View.GONE
                        linearNoDataFound.visibility=View.GONE
                    }
                }

            }
        })

        profileViewModel.getProfileUpdateResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    Toaster.showToast(this,it.data!!.response,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                    if(it.data!=null){
                        if(it.data.user_data!=null){
                            AppPreferences.user_name=it.data.user_data.name
                            AppPreferences.user_email=it.data.user_data.email
                            AppPreferences.user_profile_image=it.data.user_data.profile_pic
                            AppPreferences.reload_profile_details=true
                        }
                    }
                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
        profileViewModel.getRemoveProfilePicResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responseproceeddetails", Gson().toJson(it))
                    Toaster.showToast(this,it.data!!.response,
                        Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                    Glide.with(this).load(R.drawable.ic_account_circle).into(sivUserProfile)
                    AppPreferences.user_profile_image=""
                    AppPreferences.reload_profile_details=true
                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.internal_server_error),
                        Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnProfileUpdate.setOnClickListener {
            when {
                etProfileName.text.trim().toString().isNullOrEmpty() -> {
                    Toaster.showToast(this,getString(R.string.name_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG)
                }
                etProfileEmail.text.trim().toString().isNullOrEmpty() -> {
                    Toaster.showToast(this,getString(R.string.email_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG)
                }
                etProfilePhone.text.trim().toString().isNullOrEmpty() -> {
                    Toaster.showToast(this,getString(R.string.phone_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG)
                }
                etProfileLocation.text.trim().toString().isNullOrEmpty() -> {
                    if(!premissionGranded){
                        getLocationDetails()
                    }
                    Toaster.showToast(this,getString(R.string.address_required),
                        Toaster.State.WARNING, Toast.LENGTH_LONG)
                }
                else -> {
                    profileViewModel.profileUpdate(this,
                        etProfileName.text.trim().toString(),
                        etProfileEmail.text.trim().toString(),
                        etProfilePhone.text.trim().toString(),
                        etProfileLocation.text.trim().toString(),imagesPassed
                    )
                }
            }
        }
        btnChangeImage.setOnClickListener {
            permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()
                        .start()
                }
            }
        }
        btnRemove.setOnClickListener {
            profileViewModel.removeProfilePic()
        }
        etProfileLocation.setOnClickListener {
            getLocationDetails()
        }
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                includeNoInternet.visibility= View.GONE
                nestedProfile.visibility=View.VISIBLE
                profileViewModel.fetchProfileDetails()
            }
        }
    }
    private fun getLocationDetails(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            methodWithPermissions()
            return
        }
        premissionGranded=true
        val fields: List<Place.Field> = listOf(
            Place.Field.ID, Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }
    private fun methodWithPermissions() {
        permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION).build().send { result ->
            if (result.allGranted()) {
                permissionsBuilder(Manifest.permission.ACCESS_COARSE_LOCATION).build().send {result1 ->
                    if(result1.allGranted()){
                        getLocationDetails()
                    }

                }
            }
            if(result.allShouldShowRationale()){
                Toaster.showToast(this,"Please allow permissions", Toaster.State.WARNING,Toast.LENGTH_SHORT)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.e("placedata", Gson().toJson(place))
                Log.e("place details", "Place: " + place.name + ", " + place.id)
                passLat=place.latLng!!.latitude
                passLng=place.latLng!!.longitude
                etProfileLocation.text=place.name
                etProfileLocation.setTextColor(ContextCompat.getColor(this,R.color.black))

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e("onstatus error", status.statusMessage.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }else{
            if (resultCode == Activity.RESULT_OK)
            {

                if (data!!.clipData != null)
                {
                    val imageUri = data.clipData!!.getItemAt(0).uri
                    imagesPassed= imageUri?.path.toString()
                    Glide.with(this).load(imageUri).into(sivUserProfile)
                }
                else{
                    val imageUri = data.data
                    imagesPassed= imageUri?.path.toString()
                    Glide.with(this).load(imageUri).into(sivUserProfile)
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getViewBinging(): ActivityProfileBinding {
       return ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }

}