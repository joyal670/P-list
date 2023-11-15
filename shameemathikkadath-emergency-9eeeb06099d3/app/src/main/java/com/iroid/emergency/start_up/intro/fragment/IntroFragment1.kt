package com.iroid.emergency.start_up.intro.fragment

import com.iroid.emergency.R
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentIntro1Binding
import com.iroid.emergency.databinding.FragmentIntro2Binding
import com.iroid.emergency.databinding.FragmentIntroBinding
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.intro.IntroViewModal
import com.iroid.emergency.start_up.utils.Animations

class IntroFragment1 : BaseFragment<IntroViewModal, FragmentIntro1Binding>() {
    override fun initViews() {
        binding.tvTitleSlider.text = getString(R.string.slider_title1)
        binding.tvSliderDec.text = getString(R.string.slider_dec1)
        Animations.fade(binding.ivIcon)
        Animations.fade(binding.ivIcon3)
        Animations.scaleAndShakeAnimation(binding.ivIcon3)
        Animations.scaleAndShakeAnimation2(binding.ivIcon)
    }

    override fun setUpObserver() {

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentIntro1Binding {
        return FragmentIntro1Binding.inflate(layoutInflater)
    }
}
