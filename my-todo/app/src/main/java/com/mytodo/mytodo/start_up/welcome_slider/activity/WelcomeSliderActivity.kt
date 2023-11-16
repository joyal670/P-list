package com.mytodo.mytodo.start_up.welcome_slider.activity

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.mytodo.mytodo.R
import com.mytodo.mytodo.base.BaseActivity
import com.mytodo.mytodo.databinding.ActivityWelcomeSliderBinding
import com.mytodo.mytodo.start_up.auth.activity.AuthActivity
import com.mytodo.mytodo.start_up.welcome_slider.adapter.DepthPageTransformer
import com.mytodo.mytodo.start_up.welcome_slider.adapter.IntroSliderAdapter
import com.mytodo.mytodo.start_up.welcome_slider.fragment.Intro1Fragment
import com.mytodo.mytodo.start_up.welcome_slider.fragment.Intro2Fragment
import com.mytodo.mytodo.start_up.welcome_slider.fragment.Intro3Fragment
import com.mytodo.mytodo.ui.main.home.activity.DashboardActivity
import com.mytodo.mytodo.utils.AppPreferences.prefIsLogin
import com.mytodo.mytodo.utils.AppPreferences.prefIsWelcomeSlider

class WelcomeSliderActivity : BaseActivity<ActivityWelcomeSliderBinding>()
{
    private val fragmentList = ArrayList<Fragment>()

    override val layoutId: Int
        get() = R.layout.activity_welcome_slider
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun getViewBinging(): ActivityWelcomeSliderBinding = ActivityWelcomeSliderBinding.inflate(layoutInflater)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData()
    {
        val adapter = IntroSliderAdapter(this)
        binding.vpIntroSlider.adapter = adapter
        fragmentList.addAll(listOf(
            Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
        ))
        adapter.setFragmentList(fragmentList)
        binding.dotsIndicator.setViewPager2( binding.vpIntroSlider)
        binding.vpIntroSlider.currentItem = 0
        registerListeners()
        binding.vpIntroSlider.setPageTransformer(DepthPageTransformer())
    }

    private fun registerListeners() {
        binding.vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.vpIntroSlider.currentItem = position
                if (position < fragmentList.lastIndex) {
                    binding.tvSkip.visibility = View.VISIBLE
                    binding.btnNext.text = "Next"
                } else {
                    binding.tvSkip.visibility = View.GONE
                    binding.btnNext.text = "Get Started"
                }
            }
        })
        binding.tvSkip.setOnClickListener {
            prefIsWelcomeSlider = true
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        binding.btnNext.setOnClickListener {
            val position =  binding.vpIntroSlider.currentItem
            if (position < fragmentList.lastIndex) {
                binding.vpIntroSlider.currentItem = position + 1
            } else {
                prefIsWelcomeSlider = true
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
    }

    override fun fragmentLaunch() {

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
        if(prefIsWelcomeSlider)
        {
            if(prefIsLogin)
            {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                ActivityCompat.finishAffinity(this)
            }
            else
            {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
                ActivityCompat.finishAffinity(this)
            }
        }

    }
}