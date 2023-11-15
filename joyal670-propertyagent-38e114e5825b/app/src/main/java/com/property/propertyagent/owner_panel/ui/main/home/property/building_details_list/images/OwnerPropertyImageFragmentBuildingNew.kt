package com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.images

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.FullScreenImageDialogFragmentNew
import com.property.propertyagent.modal.owner.owner_building_details.OwnerBuildingDetailsListDocument
import com.property.propertyagent.utils.loadImagesWithGlideExt
import kotlinx.android.synthetic.main.fragment_property_owner_image.*

class OwnerPropertyImageFragmentBuildingNew : BaseFragment() {

    companion object {

        private const val TAG = "Fragment"
        private var img: String? = null
        private var doc = ArrayList<OwnerBuildingDetailsListDocument>()

        @JvmStatic
        fun newInstance(image: String, documents: ArrayList<OwnerBuildingDetailsListDocument>) =
            OwnerPropertyImageFragmentBuildingNew().apply {
                arguments = Bundle().apply {
                    putString("img", image)
                    putParcelableArrayList("doc", documents)
                    img = getString(image)
                    Log.i(TAG, "newInstance: $img")
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            img = it.getString("img")
            doc = it.getParcelableArrayList("doc")!!
        }

        Log.i(TAG, "newInstance: $doc")
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_property_owner_image, container, false)
    }

    override fun initData() {
        Log.i(TAG, "initData: $img")
        owner_image_slider.loadImagesWithGlideExt(img!!)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        owner_image_slider.setOnClickListener {
            setUpFullScreenImage()
        }
    }

    private fun setUpFullScreenImage() {
        val dialog = FullScreenImageDialogFragmentNew(requireActivity(), doc!!)
        dialog.show(parentFragmentManager, "TAG")
    }
}

