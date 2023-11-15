package com.property.propertyuser.ui.main.home.property_image_view.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentImageSliderBinding
import com.property.propertyuser.utils.Constants.ARG_IMAGE
import com.property.propertyuser.utils.loadImagesWithGlideExtFull
import kotlinx.android.synthetic.main.fragment_image_slider.*


class ImageFragment:BaseFragment() {
    private lateinit var binding: FragmentImageSliderBinding
    private var uri: Uri? = null
    companion object{
        fun newInstance(image:String)=ImageFragment().apply {
            arguments=Bundle().apply {
                putString(ARG_IMAGE,image)
            }
        }
    }
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentImageSliderBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
    }

    override fun initData() {
        if (arguments != null) {
            Glide.with(this)
                .load(arguments?.getString(ARG_IMAGE).toString())
                .error(R.drawable.shape_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewer)
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