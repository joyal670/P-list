package com.property.propertyuser.ui.startup.welcome_slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentIntro2Binding

class Intro2Fragment:BaseFragment() {
    private lateinit var binding: FragmentIntro2Binding
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentIntro2Binding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_intro2, container, false)*/
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