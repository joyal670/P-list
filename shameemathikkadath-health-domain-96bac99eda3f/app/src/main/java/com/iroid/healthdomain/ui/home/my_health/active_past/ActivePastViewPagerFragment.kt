package com.iroid.healthdomain.ui.home.my_health.active_past

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.healthdomain.data.model_class.index.ActiveChallenge
import com.iroid.healthdomain.data.model_class.index.PastChallenge
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.FragmentViewPagerBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.my_health.MyHealthRepository
import com.iroid.healthdomain.ui.home.my_health.MyHealthViewModel
import com.iroid.healthdomain.ui.home.my_health.active_past.active.ActiveFragment
import com.iroid.healthdomain.ui.home.my_health.active_past.past.PastFragment
import com.iroid.healthdomain.ui.viewpager_adaptor.ViewPagerAdaptor

class ActivePastViewPagerFragment(
    private val activeChallenges: List<ActiveChallenge>,
    private val pastChallenges: List<PastChallenge>
) :
    BaseFragment<MyHealthViewModel, FragmentViewPagerBinding, MyHealthRepository>() {
    private lateinit var adapterFollower: ViewPagerAdaptor


    companion object {
        fun newInstance(
            activeChallenges: List<ActiveChallenge>,
            pastChallenges: List<PastChallenge>
        ) = ActivePastViewPagerFragment(activeChallenges, pastChallenges)
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentViewPagerBinding {
        return FragmentViewPagerBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<MyHealthViewModel> = MyHealthViewModel::class.java

    override fun getFragmentRepository(): MyHealthRepository {
        return MyHealthRepository(remoteDataSource.buildApi(ApiServices::class.java))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fragmentList = arrayListOf<Fragment>(
            ActiveFragment.newInstance(activeChallenges),
            PastFragment.newInstance(pastChallenges)
        )

        adapterFollower = ViewPagerAdaptor(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapterFollower
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = "Active"

                }
                1 -> {
                    tab.text = "Past"
                }
            }

        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val view=adapterFollower.getViewAtPosition(position)
                updatePagerHeightForChild(view!!,binding.viewPager)
                updatePagerHeightForChild(view!!,binding.viewPager)
            }
        })

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
                        Log.e("#565656", "updatePagerHeightForChild:${lp.height} " )
                        lp.height = view.measuredHeight
                    }
            }
        }
    }

}
