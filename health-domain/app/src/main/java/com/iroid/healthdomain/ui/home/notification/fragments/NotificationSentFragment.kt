package com.iroid.healthdomain.ui.home.notification.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.FragmentNotificationSentBinding
import com.iroid.healthdomain.ui.adaptor.ReceivedNotificationAdapter
import com.iroid.healthdomain.ui.adaptor.SentNotificationAdapter
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import com.iroid.healthdomain.ui.home.notification.NotificationViewModel
import com.iroid.healthdomain.ui.utils.handleApiError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class NotificationSentFragment :
    BaseFragment<NotificationViewModel, FragmentNotificationSentBinding, HomeRepository>() {

    val notificationAdapter: SentNotificationAdapter by lazy { SentNotificationAdapter() }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationSentBinding {
        return FragmentNotificationSentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<NotificationViewModel> = NotificationViewModel::class.java

    override fun getFragmentRepository(): HomeRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ApiServices::class.java, authToken = token)
        return HomeRepository(api, userPreferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.notificationRecycler.adapter = notificationAdapter
        //notificationAdapter.list = activeChallenges
        viewModel.getSentNotifications()

        getObservers()
    }

    private fun getObservers() {
        viewModel.sentNotificationLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    showLoader()
                }
                is Resource.Success -> {
                    dismissLoader()
                    if (it.value.status == 200) {
                        if (it.value.data.data.isNotEmpty()){
                            notificationAdapter.list = it.value.data.data
                        }
                    }
                }
                is Resource.Failure -> {
                    dismissLoader()
                    handleApiError(requireView(), it)
                }
            }
        })
    }
}