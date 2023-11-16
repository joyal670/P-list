package com.property.propertyagent.start_up.welcome_slider

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.property.propertyagent.R
import com.property.propertyagent.start_up.auth.activity.AuthActivity
import com.property.propertyagent.utils.AppPreferences.isVisited
import com.property.propertyagent.base.BaseActivity
import kotlinx.android.synthetic.main.activity_welcome_slider.*

class WelcomeSliderActivity : BaseActivity()
{
    private val fragmentList = ArrayList<Fragment>()

    override val layoutId: Int
        get() = R.layout.activity_welcome_slider
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun fragmentLaunch() {

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData()
    {
        val adapter = IntroSliderAdapter(this)
        vpIntroSlider!!.adapter = adapter
        fragmentList.addAll(listOf(
                Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
        ))
        adapter.setFragmentList(fragmentList)
        dotsIndicator!!.setViewPager2(vpIntroSlider)
        vpIntroSlider.currentItem = 0
        registerListeners()
        vpIntroSlider.setPageTransformer(DepthPageTransformer())
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    private fun registerListeners() {
        vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                vpIntroSlider.currentItem = position
                if (position < fragmentList.lastIndex) {
                    tvSkip.visibility = View.VISIBLE
                    btnNext.text = getString(R.string.next)
                } else {
                    tvSkip.visibility = View.GONE
                    btnNext.text = getString(R.string.get_started)
                }
            }
        })
        tvSkip.setOnClickListener {
            isVisited=true
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        btnNext.setOnClickListener {
            val position = vpIntroSlider.currentItem
            if (position < fragmentList.lastIndex) {
                vpIntroSlider.currentItem = position + 1
            } else {
                isVisited=true
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
    }
}

