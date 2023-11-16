package com.iroid.emergency.start_up.intro.fragment

import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentIntro3Binding
import com.iroid.emergency.databinding.FragmentIntroBinding
import com.iroid.emergency.start_up.intro.IntroViewModal

class IntroFragment3 : BaseFragment<IntroViewModal, FragmentIntro3Binding>() {
    override fun initViews() {
        binding.tvTitleSlider.text = getString(R.string.slider_title3)
        binding.tvSliderDec.text = getString(R.string.slider_dec3)
    }

    override fun setUpObserver() {

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentIntro3Binding {
        return FragmentIntro3Binding.inflate(layoutInflater)
    }
}
