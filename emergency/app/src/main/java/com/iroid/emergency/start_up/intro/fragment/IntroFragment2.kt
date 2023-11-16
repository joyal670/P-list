package com.iroid.emergency.start_up.intro.fragment

import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentIntro1Binding
import com.iroid.emergency.databinding.FragmentIntro2Binding
import com.iroid.emergency.databinding.FragmentIntroBinding
import com.iroid.emergency.start_up.intro.IntroViewModal

class IntroFragment2 : BaseFragment<IntroViewModal, FragmentIntro2Binding>() {
    override fun initViews() {
        binding.tvTitleSlider.text = getString(R.string.slider_title2)
        binding.tvSliderDec.text = getString(R.string.slider_dec2)
    }

    override fun setUpObserver() {

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentIntro2Binding {
        return FragmentIntro2Binding.inflate(layoutInflater)
    }
}
