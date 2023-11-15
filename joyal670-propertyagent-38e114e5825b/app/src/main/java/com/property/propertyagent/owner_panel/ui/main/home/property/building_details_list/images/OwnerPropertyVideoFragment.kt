package com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.images

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.utils.Constants
import kotlinx.android.synthetic.main.fragment_property_owner_video.*

class OwnerPropertyVideoFragment : BaseFragment() {
    var mediaControls: MediaController? = null

    companion object {
        fun newInstance(video: String) =
            OwnerPropertyVideoFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(Constants.ARG_VIDEO, video)
                    }
                }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_property_owner_video, container, false)
    }

    override fun initData() {

        if (mediaControls == null) {
            mediaControls = MediaController(requireContext())
            mediaControls!!.setAnchorView(this.simpleVideoView)
        }

        simpleVideoView!!.setMediaController(mediaControls)

        simpleVideoView!!.setVideoURI(
            Uri.parse(
                arguments?.getString(Constants.ARG_VIDEO).toString()
            )
        )

        simpleVideoView!!.requestFocus()

        simpleVideoView!!.start()

        simpleVideoView!!.setOnCompletionListener {
            Toast.makeText(
                requireContext(), "Video completed",
                Toast.LENGTH_LONG
            ).show()
        }

        simpleVideoView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(
                requireContext(), "An Error Occured " +
                        "While Playing Video !!!", Toast.LENGTH_LONG
            ).show()
            false
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
