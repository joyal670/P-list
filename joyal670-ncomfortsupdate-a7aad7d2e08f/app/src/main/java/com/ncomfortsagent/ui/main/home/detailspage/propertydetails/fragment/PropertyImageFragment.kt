package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentPropertyImageBinding
import com.ncomfortsagent.dialog.FullScreenPropertyImageDialogFragment
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsDocument


class PropertyImageFragment : BaseFragment() {
    private lateinit var binding: FragmentPropertyImageBinding

    companion object {
        private const val TAG = "Fragment"
        private var propertyImage: String? = null
        private var doc = ArrayList<AgentPropertyDetailsDocument>()

        @JvmStatic
        fun newInstance(image: String, imageList: ArrayList<AgentPropertyDetailsDocument>) =
            PropertyImageFragment().apply {
                arguments = Bundle().apply {
                    putString("img", image)
                    putParcelableArrayList("doc", imageList)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            propertyImage = it.getString("img")
            doc = it.getParcelableArrayList("doc")!!
        }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertyImageBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        Glide.with(requireContext()).load(propertyImage).placeholder(R.drawable.no_image)
            .into(binding.ownerImageSlider)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {
        binding.ownerImageSlider.setOnClickListener {
            val dialog = FullScreenPropertyImageDialogFragment(requireActivity(), doc)
            dialog.show(parentFragmentManager, "TAG")
        }
    }

}