package com.mytodo.mytodo.start_up.welcome_slider.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mytodo.mytodo.R
import com.mytodo.mytodo.base.BaseFragment
import com.mytodo.mytodo.databinding.FragmentIntro1Binding


class Intro1Fragment : BaseFragment()
{
    private lateinit var binding: FragmentIntro1Binding

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntro1Binding.inflate(inflater!!,container,false)
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