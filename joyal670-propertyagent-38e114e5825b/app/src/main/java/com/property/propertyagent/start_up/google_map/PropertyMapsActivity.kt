package com.property.propertyagent.start_up.google_map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.gson.Gson
import com.google.maps.android.ui.IconGenerator
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.start_up.google_map.model.MarkerData
import com.property.propertyagent.utils.AppPreferences.is_user
import com.property.propertyagent.utils.AppPreferences.tour_id
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.googlemap_bottomsheet.*

@Suppress("DEPRECATION")
class PropertyMapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mapLocationViewViewModel: MapLocationViewViewModel

    private var markerList: ArrayList<Marker> = ArrayList()
    private var markerDataList: ArrayList<MarkerData> = ArrayList()

    private lateinit var userSelection: String
    private var amount: String = ""

    private var isDataLoaded: Boolean = false

    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null
    private var markerTag: Int = 0

    private var behavior: BottomSheetBehavior<*>? = null
    override val layoutId: Int
        get() = R.layout.activity_property__maps
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {

        behavior = BottomSheetBehavior.from(bottom_sheet_map)
        behavior!!.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(
                @NonNull bottomSheet: View,
                newState: Int
            ) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                }
            }

            override fun onSlide(
                @NonNull bottomSheet: View,
                slideOffset: Float
            ) {
            }
        })

        userSelection = if (is_user) {
            "1"
        } else {
            "2"
        }
        mapLocationViewViewModel.tourLocationViewData(tour_id, userSelection)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        mapLocationViewViewModel = MapLocationViewViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        mapLocationViewViewModel.getAgentTourLocationViewDataResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> showProgress()
                    Status.SUCCESS -> {
                        dismissProgress()
                        if (it.data?.property_details != null) {
                            Log.e("response", Gson().toJson(it))
                            if (it.data.property_details.property_priority_image.document.isNotEmpty()) {
                                Glide.with(this)
                                    .load(it.data.property_details.property_priority_image.document)
                                    .into(bottomsheet_ImageView)
                            }

                            tvBhkCount.text = it.data.property_details.Beds.toString() + " BKH"
                            tvRating.text = it.data.property_details.rating

                            if (it.data.property_details.property_to == 0) {
                                tvApartmentType.text = getString(R.string.appartment_for_rent)
                                amount = it.data.property_details.rent
                            } else {
                                tvApartmentType.text = getString(R.string.appartment_for_sale)
                                amount = it.data.property_details.selling_price
                            }

                            bottomsheet_Name.text = it.data.property_details.property_name
                            bottomsheet_Amount.text = amount

                            if (it.data.property_details.longitude.isNotEmpty()) {
                                markerDataList.add(
                                    MarkerData(
                                        it.data.property_details.latitude.toDouble(),
                                        it.data.property_details.longitude.toDouble(),
                                        "SAR $amount"
                                    )
                                )
                                isDataLoaded = true
                                setUpMaps()
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            applicationContext, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            applicationContext,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (applicationContext.isConnected) {
                            Toaster.showToast(
                                applicationContext, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                }
            }
    }

    override fun onClicks() {

    }

    private fun setUpMaps() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.Map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val tc = IconGenerator(this)
        val tcNotSelected = IconGenerator(this)

        tc.setTextAppearance(R.style.iconGenText)
        tc.setContentPadding(50, 40, 50, 0)

        tcNotSelected.setTextAppearance(R.style.iconGenText)
        tcNotSelected.setContentPadding(40, 15, 40, 0)

        tc.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_mark_plain_tick))
        tcNotSelected.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_mark_plain))

        for (i in 0 until markerDataList.size) {
            createMarker(
                markerDataList[i].lat, markerDataList[i].lng,
                if (i == markerDataList.size - 1) tc.makeIcon(markerDataList[i].price) else tcNotSelected.makeIcon(
                    markerDataList[i].price
                )
            )?.let {
                markerList.add(
                    it
                )
            }
        }

        bottomsheet_Amount.text = markerDataList[markerDataList.size - 1].price

        googleMap.setOnMarkerClickListener { marker ->
            setBottom()
            for (i in 0 until markerList.size) {
                markerList[i].setIcon(
                    BitmapDescriptorFactory.fromBitmap(
                        tcNotSelected.makeIcon(
                            markerDataList[i].price
                        )
                    )
                )
            }
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(tc.makeIcon(markerDataList[marker.tag as Int].price)))
            bottomsheet_Amount.text = markerDataList[marker.tag as Int].price
            true
        }

        googleMap.setOnCameraMoveListener {
            setupBottom()
        }
    }

    private fun createMarker(
        latitude: Double,
        longitude: Double,
        bmp: Bitmap
    ): Marker? {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 12.0f)
        googleMap?.animateCamera(cameraUpdate)
        marker = googleMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
        )
        marker?.tag = markerTag
        Log.e("markerTag", " is $markerTag")
        markerTag += 1
        return marker
    }

    private fun setBottom() {
        if (behavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupBottom() {
        if (behavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}