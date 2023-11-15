package com.property.propertyuser.ui.main.property_details.packages

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityPackageBinding
import com.property.propertyuser.databinding.ActivitySaleAndRentBinding
import com.property.propertyuser.ui.main.property_details.packages.adapter.PackageContentAdapter
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.preference.AppPreferences
import kotlinx.android.synthetic.main.activity_package.*


class PackageActivity : BaseActivity<ActivityPackageBinding>(),ActivityListener {
    override fun getViewBinging(): ActivityPackageBinding = ActivityPackageBinding.inflate(layoutInflater)

    private var indicatorWidth = 0
    override val layoutId: Int
        get() = R.layout.activity_package
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setTitle(getString(R.string.packages))
        vpPackageDescription!!.adapter =
            PackageContentAdapter(
                this?.supportFragmentManager,
                lifecycle
            ) {
                Log.e("onPackageFragment", it)
                val intent = Intent()
                intent.putExtra("statuscode", it)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        TabLayoutMediator(tabs!!, vpPackageDescription!!, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.weekly)
                1 -> tab.text = resources.getString(R.string.monthly)
                2 -> tab.text = resources.getString(R.string.sixMonths)
                3 -> tab.text = resources.getString(R.string.yearly)
                4 -> tab.text = "xyzxyz"
                5 -> tab.text = "xyzzzzzz"
                6 -> tab.text = "xywwert"
            }
        }).attach()

        tabs.post(Runnable {
            indicatorWidth = tabs.getWidth() / tabs.getTabCount()

            //Assign new width
            val indicatorParams =
                indicator.getLayoutParams() as FrameLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            indicator.setLayoutParams(indicatorParams)
        })
        vpPackageDescription.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val params =
                    indicator.getLayoutParams() as FrameLayout.LayoutParams

                //Multiply positionOffset with indicatorWidth to get translation

                //Multiply positionOffset with indicatorWidth to get translation
                val translationOffset: Float = (positionOffset + position) * indicatorWidth
                if(AppPreferences.chooseLanguage=="arabic"){
                    params.rightMargin = translationOffset.toInt()
                }
                else{
                    params.leftMargin = translationOffset.toInt()
                }
                indicator.layoutParams = params
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("Selected_Page", position.toString())

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {
        initializeToolbar(title)
    }
}