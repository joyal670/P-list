package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentPropertyVideoBinding

class PropertyVideoFragment : BaseFragment() {

    private lateinit var binding: FragmentPropertyVideoBinding

    companion object {
        private const val TAG = "Fragment"
        private  var Id : Int? = null

        @JvmStatic
        fun newInstance(id: Int) =
            PropertyImageFragment().apply {
                arguments = Bundle().apply {
                    putInt("id",id)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            Id = it.getInt("id")
            Log.e(TAG, "onCreate: $Id" )
        }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertyVideoBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

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