package com.iroid.healthdomain.ui.home.profile.view_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.user_profile.Contact
import com.iroid.healthdomain.databinding.FragmentFollowerViewPagerBinding
import com.iroid.healthdomain.ui.home.profile.view_pager.followers.FollowersFragment
import com.iroid.healthdomain.ui.home.profile.view_pager.following.FollowingFragment
import com.iroid.healthdomain.ui.viewpager_adaptor.ViewPagerAdaptor

class FollowerViewPagerFragment(private val contact: List<Contact>) : Fragment() {


    private lateinit var binding: FragmentFollowerViewPagerBinding
    private lateinit var adapterFollower: ViewPagerAdaptor

    private var followersList: ArrayList<Contact> = ArrayList()
    private var followingList: ArrayList<Contact> = ArrayList()
    private var fragmentList:ArrayList<Fragment> = ArrayList()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_follower_view_pager,
            container,
            false
        )
        return binding.root
    }

    companion object {
        fun newInstance(contact: List<Contact>) = FollowerViewPagerFragment(contact)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        for (item in contact) {
            if (item.is_follower == 1) {
                followersList.add(item)
            } else {
                followingList.add(item)
            }
        }

        fragmentList = arrayListOf<Fragment>(
            FollowersFragment.newInstance(followersList),
            FollowingFragment.newInstance(followingList)
        )

        adapterFollower = ViewPagerAdaptor(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.followViewPager.adapter = adapterFollower
        TabLayoutMediator(binding.followTabLayout, binding.followViewPager) { tab, position ->

            when (position) {
                0 -> tab.text = "Followers"
                1 -> tab.text = "Followings"
            }

        }.attach()

        binding.followViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val view=adapterFollower.getViewAtPosition(position)
                updatePagerHeightForChild(view!!,binding.followViewPager)
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
                        lp.height = view.measuredHeight
                    }
            }
        }
    }


}