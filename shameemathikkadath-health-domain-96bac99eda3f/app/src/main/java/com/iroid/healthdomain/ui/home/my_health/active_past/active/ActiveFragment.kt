package com.iroid.healthdomain.ui.home.my_health.active_past.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iroid.healthdomain.data.model_class.index.ActiveChallenge
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.ActiveFragmentBinding
import com.iroid.healthdomain.ui.adaptor.ActiveChallengesAdapter
import com.iroid.healthdomain.ui.adaptor.ContactAdaptor
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.my_health.MyHealthRepository
import com.iroid.healthdomain.ui.home.my_health.MyHealthViewModel

class ActiveFragment(private val activeChallenges: List<ActiveChallenge>) :
    BaseFragment<MyHealthViewModel, ActiveFragmentBinding, MyHealthRepository>() {

    val challengeAdapter: ActiveChallengesAdapter by lazy { ActiveChallengesAdapter() }

    companion object {
        fun newInstance(activeChallenges: List<ActiveChallenge>) = ActiveFragment(activeChallenges)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActiveFragmentBinding {
        return ActiveFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<MyHealthViewModel> = MyHealthViewModel::class.java

    override fun getFragmentRepository(): MyHealthRepository {
        return MyHealthRepository(remoteDataSource.buildApi(ApiServices::class.java))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activeChallenges.isNullOrEmpty()){
            binding.noData.visibility=  View.VISIBLE
            binding.activeRecycler.visibility=View.GONE
        }else{
            binding.noData.visibility=  View.GONE
            binding.activeRecycler.visibility=View.VISIBLE
            binding.activeRecycler.adapter = challengeAdapter
            challengeAdapter.list = activeChallenges
        }


    }
}