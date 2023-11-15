package com.property.propertyuser.ui.main.side_menu.rewards

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentRewardsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.user_rewards.Data
import com.property.propertyuser.ui.main.booking.BookingActivity
import com.property.propertyuser.ui.main.side_menu.SideMenuActivity
import com.property.propertyuser.ui.main.side_menu.rewards.adapter.RedeemedAdapter
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_rewards.*
import kotlinx.android.synthetic.main.layout_no_network.*

class RewardsFragment : BaseFragment() {
    private lateinit var rewardsViewModel: RewardsViewModel
    private lateinit var binding: FragmentRewardsBinding
    private var rewardsList = ArrayList<Data>()
    private lateinit var activityListener: ActivityListener
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRewardsBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_rewards,container,false)*/
    }

    companion object {
        var activityFrom = "baseActivity"

        @JvmStatic
        fun newInstance(base: String) =
            RewardsFragment().apply {
                arguments = Bundle().apply {
                    putString("value", base)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            activityFrom = it.getString("value").toString()
        }
        Log.e("TAG", "initData: $activityFrom")
        when (activityFrom) {
            "1" -> {
                activityListener = activity as SideMenuActivity
            }
            "2" -> {
                activityListener = activity as BookingActivity
            }
        }
    }


    override fun initData() {

        rewardsList = ArrayList()
        constraintSubOne.visibility = View.GONE
        val slideup = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        tvRewardAmount.startAnimation(slideup)
        tvTotalCashEarn.startAnimation(slideup)
        rewardsViewModel.fetchRewards()
        //setRewardsRecyclerView()
    }

    private fun setRewardsRecyclerView() {
        rvRedeemList.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rvRedeemList.adapter = RedeemedAdapter(requireContext(), rewardsList) { result ->
            Log.e("onRewardClick", result)
            constraintSubOne.visibility = View.VISIBLE
            rewardAnimation.visibility = View.VISIBLE
            tvRewardAmount.text = result
            setupAnimation()
        }
    }

    private fun setupAnimation() {
        /*rewardAnimation.addAnimatorUpdateListener {
            // Called everytime the frame of the animation changes
        }*/
        rewardAnimation.playAnimation()
        ObjectAnimator.ofInt(nsvRewaredsMain, "scrollY", cvReward.top).setDuration(1000).start()
        /*rewardAnimation.loop(false)
        rewardAnimation.cancelAnimation() // Cancels the animation*/

        Handler(Looper.getMainLooper()).postDelayed({
            Log.e("animation", "end")
            rewardAnimation.cancelAnimation()
            rewardAnimation.visibility = View.GONE
        }, 3000)

    }

    override fun setupUI() {
        activityListener.setTitle(getString(R.string.available_coupons))
    }

    override fun setupViewModel() {
        rewardsViewModel = RewardsViewModel()
    }

    override fun setupObserver() {
        rewardsViewModel.getRewardsResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showLoader()
                }
                Status.SUCCESS -> {
                    dismissLoader()
                    includeNoInternet.visibility = View.GONE
                    Log.e("responsecheck", Gson().toJson(it))
                    if (it.data != null) {
                        if (!(it.data.data.isNullOrEmpty())) {
                            constraintMain.visibility = View.VISIBLE
                            linearNoDataFound.visibility = View.GONE
                            rewardsList = it.data.data as ArrayList<Data>
                            setRewardsRecyclerView()
                        } else {
                            constraintMain.visibility = View.GONE
                            linearNoDataFound.visibility = View.VISIBLE
                        }
                    }

                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(),
                        getString(R.string.data_empty),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireContext().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternet.visibility = View.VISIBLE
                        constraintMain.visibility = View.GONE
                        linearNoDataFound.visibility = View.GONE
                    }
                }

            }
        })
    }

    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternet.visibility = View.GONE
                constraintMain.visibility = View.VISIBLE
                linearNoDataFound.visibility = View.GONE
                rewardsViewModel.fetchRewards()
            }
        }
    }
}