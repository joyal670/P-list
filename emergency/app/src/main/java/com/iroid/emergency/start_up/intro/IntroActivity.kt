package com.iroid.emergency.start_up.intro

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.emergency.base.BaseActivity
import com.iroid.emergency.databinding.ActivityIntroSliderBinding
import com.iroid.emergency.start_up.auth.AuthActivity
import com.iroid.emergency.start_up.intro.adapter.IntroSliderAdapter
import com.iroid.emergency.start_up.intro.fragment.IntroFragment1
import com.iroid.emergency.start_up.intro.fragment.IntroFragment2
import com.iroid.emergency.start_up.intro.fragment.IntroFragment3
import com.iroid.emergency.start_up.utils.nextActivity
import kotlinx.android.synthetic.main.activity_intro_slider.*
import java.text.FieldPosition

class IntroActivity:BaseActivity<IntroViewModal,ActivityIntroSliderBinding>() {
    private var posItem=0
    private val introFragmentList = ArrayList<Fragment>()
    private lateinit var introSliderAdapter: IntroSliderAdapter
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun initViews() {
        introFragmentList.addAll(listOf(IntroFragment1(), IntroFragment2(), IntroFragment3()))
        introSliderAdapter= IntroSliderAdapter(this,introFragmentList)
        binding.vpIntroSlider.adapter=introSliderAdapter
        TabLayoutMediator(binding.tabIndicator,binding.vpIntroSlider){ tabIndicator: TabLayout.Tab, viewPager: Int ->

        }.attach()
        binding.vpIntroSlider.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                posItem=position
                if(position==0){
                    binding.btnPrev.visibility= View.GONE
                    binding.tvDone.text="Next"
                }
                if(position==1){
                    binding.btnPrev.visibility= View.VISIBLE
                    binding.tvDone.text="Next"
                }
                if(position==2){
                    binding.btnPrev.visibility= View.VISIBLE
                    binding.tvDone.text="DONE"
                }

            }
        })



    }

    override fun getViewBinding(): ActivityIntroSliderBinding {
        return ActivityIntroSliderBinding.inflate(layoutInflater)
    }

    override fun setOnClick() {
        binding.tvDone.setOnClickListener {
            if(posItem==2){
                this.nextActivity(AuthActivity::class.java, Bundle())
            }else{
                binding.vpIntroSlider.currentItem=posItem+1
            }

        }

        binding.btnPrev.setOnClickListener {
            if(posItem==1||posItem==2){
                binding.vpIntroSlider.currentItem=posItem-1
            }
        }
    }
}
