package com.iroid.emergency.main.home.accept_request

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.awesomedialog.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentAcceptBinding
import com.iroid.emergency.main.home.HomeViewModal
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.preference.ConstantPreference
import com.iroid.emergency.start_up.utils.Status
import com.iroid.emergency.start_up.utils.Toaster
import com.iroid.emergency.start_up.utils.isConnected
import com.iroid.emergency.start_up.utils.netDialog
import java.util.*


class AcceptRequestFragment : BaseFragment<HomeViewModal, FragmentAcceptBinding>(),
    LocationListener {
    private lateinit var bundle: Bundle
    private lateinit var lat: String
    private lateinit var lan: String

    override fun initViews() {
        binding.tvName.text = requireArguments().getString("name")!!
        binding.tvMobile.text = requireArguments().getString("mobile")!!
        binding.tvAddress.text = requireArguments().getString("address")!!
        val db = Firebase.firestore
        db.collection("Location").document(requireArguments().getString("mobile")!!)
            .get()
            .addOnSuccessListener { result ->
                lat= result["lat"].toString()
                lan=result["lan"].toString()
                Log.e("#565656","$lat")
                Log.e("#464646","$lan")
            }
            .addOnFailureListener { exception ->

            }


        startTimer()
        val lm = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        lm.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0, 0f,
            (this as LocationListener)
        )

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEnabled) {
                        isEnabled = false
                       // requireActivity().onBackPressed()
                        findNavController().navigate(R.id.nav_home)
                    }
                }
            }
            )
    }


    override fun setUpObserver() {
        viewModel.completeNormalLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (AppPreferences.userType == ConstantPreference.SECONDARY_TYPE) {
                        showSuccessDialog(it.data!!.message)
                    } else {
                        showTransferDialog()
                    }

                }
                Status.ERROR -> {
                    dismissProgress()
                }

                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            "Something went wrong",
                            Toaster.State.ERROR
                        )
                    } else {
                        requireActivity().netDialog(lifecycle)
                    }
                }
            }
        })
    }

    override fun setOnClick() {
        binding.btnComplete.setOnClickListener {
            viewModel.completeNormal(requireArguments().getString("request_id")!!)
        }
        binding.btnRoute.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=${AppPreferences.userLat},${AppPreferences.userLan}&daddr=$lat,$lan")
            )
            startActivity(intent)


        }
        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${requireArguments().getString("mobile")!!}")
            startActivity(intent)
        }
    }

    override fun getViewBinding(): FragmentAcceptBinding {
        return FragmentAcceptBinding.inflate(layoutInflater)
    }

    private fun showSuccessDialog(message: String) {
        AwesomeDialog.build(requireActivity())
            .title("Success")
            .body("More medical help needed?")
            .icon(R.drawable.ic_icon2)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Ok", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                findNavController().navigate(R.id.nav_home)
            }
    }

    private fun showTransferDialog() {
        bundle = bundleOf(
            "request_id" to requireArguments().getString("request_id")!!,
            "name" to requireArguments().getString("name")!!,
            "mobile" to requireArguments().getString("mobile")!!,
            "address" to requireArguments().getString("address")!!,
            "lat" to requireArguments().getString("lat")!!,
            "lan" to requireArguments().getString("lan")!!
        )
        AwesomeDialog.build(requireActivity())
            .title("Success")
            .body("More medical help needed?")
            .icon(R.drawable.ic_icon2)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Yes", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                findNavController().navigate(
                    R.id.action_fragmentAccepted_to_operationCompleteFragment,
                    bundle
                )
            }
            .onNegative("No", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                findNavController().navigate(R.id.nav_home)
            }


    }

    fun startTimer() {
        val hd = Handler()

        hd.post(object : Runnable {
            var sec = 0
            override fun run() {
                val hours_var: Int = sec / 3600
                val minutes_var: Int = sec % 3600 / 60
                val secs_var: Int = sec % 60
                val time_value: String = java.lang.String.format(
                    Locale.getDefault(),
                    "%d:%02d:%02d", hours_var, minutes_var, secs_var
                )
                binding.tv1.text = java.lang.String.format(
                    Locale.getDefault(), "%02d", hours_var
                )
                binding.tv3.text = java.lang.String.format(
                    Locale.getDefault(), "%02d", minutes_var
                )
                binding.tv5.text = java.lang.String.format(
                    Locale.getDefault(), "%02d", secs_var
                )


                sec++
                hd.postDelayed(this, 1000)
            }
        })
    }

    override fun onLocationChanged(location: Location) {
        Log.e("#585858", "onLocationChanged: ${location.latitude}")
    }

}



