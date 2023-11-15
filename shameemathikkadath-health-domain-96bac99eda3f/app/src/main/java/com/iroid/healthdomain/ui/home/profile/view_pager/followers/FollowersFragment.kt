package com.iroid.healthdomain.ui.home.profile.view_pager.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.user_profile.Contact
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.FollowersFragmentBinding
import com.iroid.healthdomain.databinding.FragmentFollowerViewPagerBinding
import com.iroid.healthdomain.ui.adaptor.ProfileFollowersAdapter
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.my_health.MyHealthRepository

class FollowersFragment(private val contact: ArrayList<Contact>) : Fragment(){

    private lateinit var binding: FollowersFragmentBinding

    val followersAdapter: ProfileFollowersAdapter by lazy { ProfileFollowersAdapter() }

    companion object {
        fun newInstance(contact: ArrayList<Contact>) = FollowersFragment(contact)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.followers_fragment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
            if(contact.isNullOrEmpty()){
                binding.followersRecycler.visibility=View.GONE
                binding.noData.visibility=View.VISIBLE
            }else{
                binding.followersRecycler.visibility=View.VISIBLE
                binding.noData.visibility=View.GONE
                binding.followersRecycler.adapter = followersAdapter
                followersAdapter.list = contact
            }


    }

}