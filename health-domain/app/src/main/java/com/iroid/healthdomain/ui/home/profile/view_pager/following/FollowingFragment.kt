package com.iroid.healthdomain.ui.home.profile.view_pager.following

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.user_profile.Contact
import com.iroid.healthdomain.databinding.FollowersFragmentBinding
import com.iroid.healthdomain.databinding.FollowingFragmentBinding
import com.iroid.healthdomain.ui.adaptor.ProfileFollowersAdapter


class FollowingFragment(private val contact: ArrayList<Contact>) : Fragment(){

    private lateinit var binding: FollowingFragmentBinding

    val followersAdapter: ProfileFollowersAdapter by lazy { ProfileFollowersAdapter() }

    companion object {
        fun newInstance(contact: ArrayList<Contact>) = FollowingFragment(contact)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.following_fragment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(contact.isNullOrEmpty()){
            binding.followingRecycler.visibility=View.GONE
            binding.noData.visibility=View.VISIBLE
        }else{
            binding.followingRecycler.visibility=View.VISIBLE
            binding.noData.visibility=View.GONE
            binding.followingRecycler.adapter = followersAdapter
            followersAdapter.list = contact
        }
    }

}

/*
class FollowingFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingFragment()
    }

    private lateinit var viewModel: FollowingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.following_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}*/
