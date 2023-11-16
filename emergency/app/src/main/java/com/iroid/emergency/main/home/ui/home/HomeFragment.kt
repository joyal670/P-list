package com.iroid.emergency.main.home.ui.home

import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.body
import com.example.awesomedialog.icon
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.position
import com.example.awesomedialog.title
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentHomeBinding
import com.iroid.emergency.main.home.HomeViewModal
import com.iroid.emergency.main.home.LocationHistoryViewModel
import com.iroid.emergency.main.home.accepted.FragmentAccepted
import com.iroid.emergency.main.home.adapter.HomeViewPagerAdapter
import com.iroid.emergency.main.home.rejected.FragmentRejected
import com.iroid.emergency.main.settings.SettingsActivity
import com.iroid.emergency.modal.common.CommonResponse
import com.iroid.emergency.modal.common.RejectedRequest
import com.iroid.emergency.modal.common.home.AcceptedRequest
import com.iroid.emergency.modal.common.home.NewRequestData
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.preference.ConstantPreference
import com.iroid.emergency.start_up.utils.CommonUtils.Companion.isOpenRecently
import com.iroid.emergency.start_up.utils.Resource
import com.iroid.emergency.start_up.utils.Status
import com.iroid.emergency.start_up.utils.Toaster
import com.iroid.emergency.start_up.utils.isConnected
import com.iroid.emergency.start_up.utils.netDialog
import com.iroid.emergency.start_up.utils.nextActivity


class HomeFragment : BaseFragment<HomeViewModal, FragmentHomeBinding>(){
    private val homeFragmentList = ArrayList<Fragment>()
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    private var acceptedList: List<AcceptedRequest> = ArrayList()
    private var startedList: List<AcceptedRequest> = ArrayList()
    private var rejectedList: List<RejectedRequest> = ArrayList()
    private lateinit var bundle: Bundle
    private val CHECK_OP_NO_THROW = "checkOpNoThrow"
    private val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
    private lateinit var awesomeDialog: AwesomeDialog
    private var isShow=false
    private lateinit var registeryCallBack:ViewPager2.OnPageChangeCallback
    private lateinit var type:String
    private var mLastClickTime: Long = 0

    private val locationViewModel by lazy {
        ViewModelProvider(this)[LocationHistoryViewModel::class.java]
    }




    override fun initViews() {
        Log.e("TAG", "initViews: ")
        val db = Firebase.firestore
        db.collection("Location").document(AppPreferences.userMobile!!)
            .get()
            .addOnSuccessListener { result ->
                AppPreferences.userLat = result["lat"].toString()
                AppPreferences.userLan = result["lan"].toString()

            }
            .addOnFailureListener { exception ->

            }
        if (AppPreferences.userType == ConstantPreference.PRIMARY_TYPE || AppPreferences.userType == ConstantPreference.SECONDARY_TYPE) {
            binding.cardTab.visibility = View.VISIBLE
            binding.vpHome.visibility = View.VISIBLE

        } else {
            binding.cardTab.visibility = View.GONE
            binding.vpHome.visibility = View.GONE
        }
        isShow=false


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    val count: Int = requireActivity().supportFragmentManager.backStackEntryCount
                    if (count == 0) {
                       requireActivity().finish()
                    } else {
                        requireActivity().supportFragmentManager.popBackStack()
                    }

                }
            }
            )


    }


    override fun onResume() {
        super.onResume()
    }



    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams)
                    .also { lp ->
                        lp.height = view.measuredHeight
                    }
            }
        }
    }

    fun setImageButtonStateNew(mContext: Context): StateListDrawable {
        val states = StateListDrawable()
        states.addState(
            intArrayOf(android.R.attr.state_selected),
            ContextCompat.getDrawable(mContext, R.drawable.back_select)
        )
        states.addState(
            intArrayOf(-android.R.attr.state_selected),
            ContextCompat.getDrawable(mContext, R.drawable.bakc_tabs)
        )

        return states


    }

    override fun setUpObserver() {
        viewModel.getHome()
        viewModel.emergencyLiveData.observe(requireActivity(), Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.noDataLottie.clearAnimation()
                    binding.noDataLottie.pauseAnimation()
                    showEmergency(it.data!!.message)
                }
                Status.ERROR -> {
                    binding.noDataLottie.pauseAnimation()
                    binding.noDataLottie.clearAnimation()

                }

                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {
                    binding.noDataLottie.pauseAnimation()
                    binding.noDataLottie.clearAnimation()
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
//        viewModel.homeLiveData.observe(viewLifecycleOwner,homeObserver.onChanged())
        viewModel.homeLiveData.removeObserver(homeObserver)
        viewModel.homeLiveData.observe(requireActivity(), homeObserver)

        viewModel.approvalLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (type == ConstantPreference.TYPE_ACCEPTED) {
                        if (this::bundle.isInitialized) {
                            findNavController().navigate(
                                R.id.action_nav_home_to_fragmentAccepted,
                                bundle
                            )



                        }
                    }

                }
                Status.ERROR -> {
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR
                    )
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



    private fun showSosDialogSatted(acceptedRequest: AcceptedRequest) {
            AwesomeDialog.build(requireActivity())
                .title("Request on progress")
                .body("One request on progress....continue the operation")
                .icon(R.drawable.ic_icon2)
                .position(AwesomeDialog.POSITIONS.CENTER)
                .onPositive("Ok", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                    bundle = bundleOf(
                        "request_id" to acceptedRequest._id,
                        "name" to acceptedRequest.patient_name,
                        "mobile" to acceptedRequest.mobile.toString(),
                        "address" to acceptedRequest.address,
                        "lat" to acceptedRequest.location.lat.toString(),
                        "lan" to  acceptedRequest.location.lan.toString()
                    )
                    viewModel.homeLiveData.removeObserver(homeObserver)
                    findNavController().navigate(
                        R.id.action_nav_home_to_fragmentAccepted,
                        bundle
                    )
                }

    }

    private fun showSosDialog(newRequest: NewRequestData) {
        Log.e("#1234","d12222")

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialof_accept)
        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val tvKm = dialog.findViewById<TextView>(R.id.tvKm)
        val tvAddress = dialog.findViewById<TextView>(R.id.tvAddress)
        val btnAccept = dialog.findViewById<Button>(R.id.btnAccept)
        val btnReject = dialog.findViewById<Button>(R.id.btnReject)
        tvName.text = newRequest.patient_name
        tvAddress.text = newRequest.address

        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val params: WindowManager.LayoutParams = dialog.window!!.attributes
        params.width =ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params
        dialog.show()


        btnReject.setOnClickListener {
            dialog.dismiss()
            type=ConstantPreference.TYPE_REJECTED

            viewModel.approvalLiveData(newRequest._id, ConstantPreference.REJECTED)
        }
        btnAccept.setOnClickListener {
            dialog.dismiss()
            type=ConstantPreference.TYPE_ACCEPTED
            bundle = bundleOf(
                "request_id" to newRequest._id,
                "name" to newRequest.patient_name,
                "mobile" to newRequest.mobile.toString(),
                "address" to newRequest.address,
                "lat" to newRequest.location.lat.toString(),
                "lan" to newRequest.location.lan.toString()
            )

            viewModel.approvalLiveData(newRequest._id, ConstantPreference.ACCEPTED)
        }




    }

    private fun setUpData(
        acceptedList: List<AcceptedRequest>,
        rejectedList: List<RejectedRequest>
    ) {
        if(homeFragmentList.size==0){
            homeFragmentList.addAll(
                listOf(
                    FragmentAccepted.newInstance(acceptedList),
                    FragmentRejected.newInstance(rejectedList)
                )
            )

        }
        homeViewPagerAdapter = HomeViewPagerAdapter(requireActivity(), homeFragmentList)
        binding.vpHome.adapter = homeViewPagerAdapter
        TabLayoutMediator(
            binding.tabIndicator,
            binding.vpHome
        ) { tabIndicator: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tabIndicator.text = "COMPLETED"
                }
                1 -> {
                    tabIndicator.text = "REJECTED"
                }
            }
        }.attach()

        val tabCount: Int = 2

        for (i in 0 until tabCount) {
            val tabView: View = (binding.tabIndicator.getChildAt(0) as ViewGroup).getChildAt(i)
            tabView.requestLayout()
            ViewCompat.setBackground(tabView, setImageButtonStateNew(requireContext()));
            ViewCompat.setPaddingRelative(
                tabView,
                tabView.paddingStart,
                tabView.paddingTop,
                tabView.paddingEnd,
                tabView.paddingBottom
            )
        }
        registeryCallBack=(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                try {
                    val view = homeViewPagerAdapter.getViewAtPosition(position)
                    updatePagerHeightForChild(view!!, binding.vpHome)
                } catch (e:Exception) {
                    Log.e("TAG", "onPageScrolled: "+e )
                }
            }
        })

        binding.vpHome.registerOnPageChangeCallback(registeryCallBack)


    }


    private fun isNotificationEnabled(context: Context): Boolean {
        val manager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.importance
        } else {
            return true
        }
        Log.e("#595959", "isNotificationEnabled: ${manager.importance}" )
        return importance < 0 || importance >= NotificationManager.IMPORTANCE_DEFAULT

    }

    override fun setOnClick() {

        binding.noDataLottie.setOnClickListener {
            if (isOpenRecently()) return@setOnClickListener
            if(AppPreferences.userLat!!.isNotEmpty()){
                binding.noDataLottie.playAnimation()
                viewModel.updateEmergency(AppPreferences.userLat!!, AppPreferences.userLan!!)
            }
        }
        binding.btnCareGiver.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ConstantPreference.TYPE, ConstantPreference.SUBMISSION)
            requireActivity().nextActivity(SettingsActivity::class.java, bundle)
        }
    }

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    private fun showEmergency2() {
        AwesomeDialog.build(requireActivity())
            .title("Warning")
            .body("Please allow to notification sound")
            .icon(R.drawable.icons_bell)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Ok", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.channel_id))
                startActivity(settingsIntent)
            }
            .onNegative("Cancel", buttonBackgroundColor = R.drawable.drawable_rounded_corners)
    }

    private fun showEmergency(message: String) {
        AwesomeDialog.build(requireActivity())
            .title("Success")
            .body(message)
            .icon(R.drawable.ic_icon2)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Ok", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {

            }

    }

    override fun onStop() {
        viewModel.homeLiveData.removeObserver(homeObserver)
        super.onStop()

    }
    override fun onPause() {
        viewModel.homeLiveData.removeObserver(homeObserver)
        super.onPause()

    }


    var homeObserver: Observer<Resource<CommonResponse>> =
        Observer<Resource<CommonResponse>> { it ->
                when (it.status) {
                    Status.SUCCESS -> {
                        isShow=true
                        AppPreferences.userType = it.data!!.user_type
                        if (AppPreferences.userType == ConstantPreference.PRIMARY_TYPE || AppPreferences.userType == ConstantPreference.SECONDARY_TYPE) {
                            binding.cardTab.visibility = View.VISIBLE
                            binding.vpHome.visibility = View.VISIBLE
                        } else {
                            binding.cardTab.visibility = View.GONE
                            binding.vpHome.visibility = View.GONE
                        }

                        if (it.data!!.new_request.status) {
                            showSosDialog(it.data!!.new_request.new_request)
                        }
                        acceptedList = it.data!!.accepted_requests
                        rejectedList = it.data!!.rejected_requests
                        if (it.data!!.started_request.isNotEmpty()) {
                            showSosDialogSatted(it.data.started_request[0])
                        }
                        setUpData(acceptedList, rejectedList)
                    }
                    Status.ERROR -> {
                        isShow=true
                    }

                    Status.LOADING -> {

                    }
                    Status.NO_INTERNET -> {
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


        }

    override fun onDestroy() {
        super.onDestroy()

    }
}



