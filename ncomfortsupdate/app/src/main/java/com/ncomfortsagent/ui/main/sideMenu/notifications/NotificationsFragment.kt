package com.ncomfortsagent.ui.main.sideMenu.notifications

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentNotificationsBinding
import com.ncomfortsagent.listeners.FragmentTransInterface
import com.ncomfortsagent.ui.main.sideMenu.activity.SideMenuActivity

class NotificationsFragment : BaseFragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as SideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.notifications))
    }

    override fun setupUI() {
        binding.rvNotification.layoutManager = LinearLayoutManager(context)
        binding.rvNotification.adapter = NotificationsAdapter(requireContext())

        binding.shimmerLayout.startShimmer()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerLayout.stopShimmer()
            binding.rvNotification.visibility = View.VISIBLE
            binding.shimmerLayout.visibility = View.GONE
        }, 3000)

    }


    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }


}