package com.property.propertyuser.ui.main.property_details.slide_images

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentPropertyImageBinding
import com.property.propertyuser.modal.property_details.Document
import com.property.propertyuser.utils.Constants.ARG_IMAGE
import com.property.propertyuser.utils.loadImagesWithGlideExt
import com.stfalcon.imageviewer.StfalconImageViewer

class PropertyImageFragment : BaseFragment() {
    private lateinit var binding: FragmentPropertyImageBinding
    private var doc = ArrayList<Document>()
    private var docImg = ArrayList<String>()
    private var overlayView: PosterOverlayView? = null
    lateinit var builder: StfalconImageViewer<String>

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyImageBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_property_image, container, false)*/
    }

    companion object {
        fun newInstance(image: String, imageList: ArrayList<Document>) =
            PropertyImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE, image)
                    putParcelableArrayList("doc", imageList)
                }
            }
    }

    override fun initData() {
        //CommonMethods.setImage(requireContext(),arguments?.getString(ARG_IMAGE).toString(),binding.ivImage)
        binding.ivImage.loadImagesWithGlideExt(arguments?.getString(ARG_IMAGE).toString())
        doc = arguments?.getParcelableArrayList("doc")!!
        Log.e("TAG", "initData: $doc")

        doc.forEach {
            docImg.add(it.document)
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

        binding.ivImage.setOnClickListener {
            setOverlay()
            builder = StfalconImageViewer.Builder(context, docImg) { view, image ->
                view.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.shape_placeholder)
                Glide.with(requireActivity())
                    .load(image)
                    .error(R.drawable.shape_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view)
            }.withOverlayView(overlayView).withHiddenStatusBar(false).show()
        }
    }

    private fun setOverlay() {
        overlayView = PosterOverlayView(requireContext()).apply {
            update()
            onClose = {
                builder.close()
            }
        }
    }
}