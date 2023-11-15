package com.property.propertyuser.ui.main.property_details.slide_images

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentPropertyImageBinding
import com.property.propertyuser.databinding.FragmentPropertyVideoBinding
import com.property.propertyuser.utils.Constants
import kotlinx.android.synthetic.main.fragment_property_video.*

class PropertyVideoFragment:BaseFragment() {
    private lateinit var binding: FragmentPropertyVideoBinding
    // declaring a null variable for MediaController
    var mediaControls: MediaController? = null
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPropertyVideoBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
        /*return inflater?.inflate(R.layout.fragment_property_video, container, false)*/
    }
    companion object{
        fun newInstance(video:String)=PropertyVideoFragment().apply {
            arguments=Bundle().apply {
                putString(Constants.ARG_VIDEO,video)
            }
        }
    }

    override fun initData() {
        /*if (mediaControls == null) {
            // creating an object of media controller class
            mediaControls = MediaController(requireContext())

            // set the anchor view for the video view
            mediaControls!!.setAnchorView(this.simpleVideoView)
        }

        // set the media controller for video view
        simpleVideoView!!.setMediaController(mediaControls)

        // set the absolute path of the video file which is going to be played
        simpleVideoView!!.setVideoURI(Uri.parse(arguments?.getString(Constants.ARG_VIDEO).toString()))

        simpleVideoView!!.requestFocus()

        // starting the video
        simpleVideoView!!.start()

        // display a toast message
        // after the video is completed
        simpleVideoView!!.setOnCompletionListener {
            Toast.makeText(requireContext(), "Video completed",
                Toast.LENGTH_LONG).show()
        }

        // display a toast message if any
        // error occurs while playing the video
        simpleVideoView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(requireContext(), "An Error Occured " +
                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
            false
        }*/
        Log.e("TAG", "initData: "+ arguments?.getString(Constants.ARG_VIDEO).toString() )
        simpleVideoView.setSource(arguments?.getString(Constants.ARG_VIDEO).toString())
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