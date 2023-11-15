package com.property.propertyuser.ui.main.home.property_image_view

import android.util.Log
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityPropertyImageViewBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.property_list.Document
import com.property.propertyuser.ui.main.home.property_image_view.adapter.ImageSliderAdapter
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.activity_property_image_view.*

class PropertyImageViewActivity : BaseActivity<ActivityPropertyImageViewBinding>(),ActivityListener {
    private var passedDocumentImageList=ArrayList<Document>()
    private var imageSliderList=ArrayList<String>()
    private var i=0
    override fun getViewBinging(): ActivityPropertyImageViewBinding = ActivityPropertyImageViewBinding.inflate(layoutInflater)
    override val layoutId: Int
        get() = R.layout.activity_property_image_view
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        imageSliderList=ArrayList()
        passedDocumentImageList=intent.getParcelableArrayListExtra<Document>("documentImageList")!!
        Log.e("list image",Gson().toJson(passedDocumentImageList))
        if(passedDocumentImageList.isNotEmpty()) {
            for (i in 0 until passedDocumentImageList.size){
                if(passedDocumentImageList[i].type==0){
                    imageSliderList.add(passedDocumentImageList[i].document)
                }
            }
            val imageSliderAdapter =
            ImageSliderAdapter(supportFragmentManager, lifecycle, imageSliderList)
            view_pager.adapter = imageSliderAdapter
            dotsIndicatorImageSlider.setViewPager2(view_pager)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        ic_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun navigateToFragment(fragment: Fragment) {
    }

    override fun setTitle(title: String) {
       /* initializeToolbar(title)*/
    }

}