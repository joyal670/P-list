package com.property.propertyuser.ui.startup.welcome_slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentIntro1Binding

class Intro1Fragment : BaseFragment() {
    private lateinit var binding: FragmentIntro1Binding
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntro1Binding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_intro1, container, false)*/
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

    override fun onResume() {
        super.onResume()
        binding.mainConstraintIntroOne.rebuildScene()
    }
}