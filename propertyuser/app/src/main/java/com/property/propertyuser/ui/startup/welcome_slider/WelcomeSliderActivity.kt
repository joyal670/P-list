package com.property.propertyuser.ui.startup.welcome_slider

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityWelcomeSliderBinding
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.startup.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_welcome_slider.*

class WelcomeSliderActivity : BaseActivity<ActivityWelcomeSliderBinding>()  {

    private val fragmentList = ArrayList<Fragment>()
    override fun getViewBinging(): ActivityWelcomeSliderBinding = ActivityWelcomeSliderBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_welcome_slider
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun fragmentLaunch() {

    }

    override fun initData() {
        val adapter = IntroSliderAdapter(this)
        vpIntroSlider!!.adapter = adapter
        fragmentList.addAll(listOf(
            Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
        ))
        adapter.setFragmentList(fragmentList)
        dotsIndicator!!.setViewPager2(vpIntroSlider)
        vpIntroSlider.currentItem = 0
        registerListeners()


    }

    private fun registerListeners() {
        vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                vpIntroSlider.currentItem = position
                if (position < fragmentList.lastIndex) {
                    tvSkip.visibility = View.VISIBLE
                    btnNext.text = getString(R.string.btnNext)
                } else {
                    tvSkip.visibility = View.GONE
                    btnNext.text = getString(R.string.btnGetStarted)
                }
            }
        })
        binding.tvSkip.setOnClickListener {
            AppPreferences.isFirstTime=false
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        binding.btnNext.setOnClickListener {
            val position = vpIntroSlider.currentItem
            if (position < fragmentList.lastIndex) {
                vpIntroSlider.currentItem = position + 1
            } else {
                AppPreferences.isFirstTime=false
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
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