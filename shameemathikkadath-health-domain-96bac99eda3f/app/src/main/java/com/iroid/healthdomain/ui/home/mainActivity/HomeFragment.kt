package com.iroid.healthdomain.ui.home.mainActivity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.material.tabs.TabLayoutMediator
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.data.model_class.contacts_api.ContactResponse
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.HomeFragmentBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.utils.handleApiError
import com.iroid.healthdomain.ui.viewpager_adaptor.ViewPagerAdaptor2
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding, HomeRepository>() {

    private var fragmentList = arrayListOf<Fragment>()
    private lateinit var adaptor: ViewPagerAdaptor2
    private lateinit var dialog:AlertDialog

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentRepository(): HomeRepository {
     //   val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiZWU1OTMyOWEyZjNmOGUyZTYxMjZiOTBlYmFjMjJmMzk5ZDQzOTgzZWYxZDRmODI2YzRjMGFjMGNlMTQ2MmYyYTZhNzZlODQ4NTIwODM4NzMiLCJpYXQiOjE2MTUyODEwNDAsIm5iZiI6MTYxNTI4MTA0MCwiZXhwIjoxNjMxMTc4NjQwLCJzdWIiOiIyIiwic2NvcGVzIjpbXX0.Say3OdyijjX1EStGjL0Akg2G2fnzuFWA21wEnkxPI_4kLRbJM3AA9Re2ywR64RctFt5i07SsdpVNgnSGVkndiOxfOrq03OQhcgM2Py57-o6pisqP1aXC3MXU1XJZ1Dq9S_56cMjL8kVJpyz79Wex2kJjdmnEFfM8VOOpYQgZn_Ioi1d0Xt3SfRe2SrStsUmxbrnYU3bEhFc9XZqIH4wUHKVuOaAFc4yMghQ2AvpfDRyUaYNSfYTfoCesgto6F09j_EmVi4zgO9OT0V6PMK1tAzte-t1CFYZovJOkkemwK-88fooNjHqFJAL2bHjPNy_uI7p2ToxdKpdzf96U9CA0EzD3WExoX02CkbCv8b4HoE70luew_LdXIbGyh8tyvKrGx3zvqLj-C7ocDa_Aa8gzXykW0lLi1R5zpMKkPwyFYLLNIhH3WIXfK48811dochCjJbBlvi7wdarrbbLyEgcDIZcOV1_nQg3IYkkC75yvAHDXh1ylC-EuCO9J2rDedVP07on-x86oO59_iC88OoBjF_M3gw76rfH_xsZmCx3TY_aRADf2ZikvL-136RrOzUx99PxsW6yPJeRtYwJ_7nGPNlvyGWdh5pNJ-exKijfeT6tvV19VQDppRVXtyVIdZsJ0kRQn-cucff9S3NUWnFCsMo2aPuyQ3tg4tZk8aRpsVvc"
      //  val api = remoteDataSource.buildApi(ApiServices::class.java, token)
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ApiServices::class.java, authToken = token)
        return HomeRepository(api, userPreferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val conatctList=ArrayList<ContactData>()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "onActivityCreated: ${it}")

        })

        addObserver()
        viewModel.getContacts()

    }

    private fun addObserver() {
        viewModel.contactLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    /*             binding.cpiLoader.isVisible = false
                                 binding.nestedData.isVisible=true*/

                    handleApiError(requireView(), it)
                }
                Resource.Loading -> {
//                    showLoader()
                }
                is Resource.Success -> {
                    if (it.value.status == 200) {

                        generateViewPager(it.value.data)
                    }
                }
            }
        }
    }

    private fun generateViewPager(data: List<ContactData>) {


        adaptor = ViewPagerAdaptor2(
                fragmentList,
                requireActivity().supportFragmentManager,
                lifecycle,
            data
        ) {
            dismissLoader()
        }


        binding.homeViewPager.adapter = adaptor

        binding.homeViewPager.offscreenPageLimit=2

        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->

            when (position) {
                0 -> {tab.text = "All Contacts"}
                1 -> tab.text = "Status"
            }

        }.attach()
    }


}

