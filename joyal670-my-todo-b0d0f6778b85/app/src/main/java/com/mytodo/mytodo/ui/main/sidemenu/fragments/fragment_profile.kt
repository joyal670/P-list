package com.mytodo.mytodo.ui.main.sidemenu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mytodo.mytodo.R
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentProfileBinding
import com.mytodo.mytodo.ui.main.sidemenu.adapter.ViewPagerAdapter
import com.mytodo.mytodo.utils.AppPreferences.prefUserDisplayName
import com.mytodo.mytodo.utils.AppPreferences.prefUserEmail
import com.mytodo.mytodo.utils.AppPreferences.prefUserProfilePic
import java.util.*


class fragment_profile : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding
    var mViewPagerAdapter: ViewPagerAdapter? = null
    var images = intArrayOf(
        R.drawable.message_bg,
        R.drawable.music_bg,
        R.drawable.feed_bg,
        R.drawable.message_bg
    )
    var timer: Timer? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
    }

    override fun setupUI() {

        refreshUI()

        mViewPagerAdapter = ViewPagerAdapter(requireContext(), images)
        binding.vpSlider.adapter = mViewPagerAdapter

        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                binding.vpSlider.post(Runnable {
                    binding.vpSlider.currentItem =
                        (binding.vpSlider.currentItem + 1) % images.size
                })
            }
        }
        timer = Timer()
        timer!!.schedule(timerTask, 2000, 2000)

    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshUI()
        }

    }

    private fun refreshUI() {
        binding.swipeRefreshLayout.setRefreshing(false)

        Glide.with(requireActivity()).load(prefUserProfilePic).into(binding.ivProfilePic)
        binding.tvProfileName.text = prefUserDisplayName
        binding.tvProfileEmail.text = prefUserEmail
    }

}