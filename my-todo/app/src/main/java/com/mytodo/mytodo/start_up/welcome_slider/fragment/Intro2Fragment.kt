package com.mytodo.mytodo.start_up.welcome_slider.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentIntro2Binding


class Intro2Fragment : BaseFragment()
{
    private lateinit var binding: FragmentIntro2Binding

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntro2Binding.inflate(inflater!!,container,false)
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

    }

}