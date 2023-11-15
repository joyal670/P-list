package com.iroid.emergency.main.settings.about

import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentAboutBinding
import com.iroid.emergency.main.settings.SettingsViewModal

class AboutFragment :BaseFragment<SettingsViewModal,FragmentAboutBinding>() {
    override fun initViews() {

    }

    override fun setUpObserver() {

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(layoutInflater)
    }
}
