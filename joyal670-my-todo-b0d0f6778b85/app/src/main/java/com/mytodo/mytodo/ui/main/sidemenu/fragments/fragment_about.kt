package com.mytodo.mytodo.ui.main.sidemenu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentAboutBinding


class fragment_about : BaseFragment() {
    private lateinit var binding: FragmentAboutBinding


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.setRefreshing(false)
        }
    }

}