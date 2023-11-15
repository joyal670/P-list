package com.iroid.healthdomain.ui.home.my_health.active_past.past

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iroid.healthdomain.data.model_class.index.PastChallenge
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.PastFragmentBinding
import com.iroid.healthdomain.ui.adaptor.PastChallengesAdapter
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.my_health.MyHealthRepository
import com.iroid.healthdomain.ui.home.my_health.MyHealthViewModel

class PastFragment(private val pastChallenges: List<PastChallenge>) :
    BaseFragment<MyHealthViewModel, PastFragmentBinding, MyHealthRepository>() {

    val challengeAdapter: PastChallengesAdapter by lazy { PastChallengesAdapter() }

    companion object {
        fun newInstance(pastChallenges: List<PastChallenge>) = PastFragment(pastChallenges)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PastFragmentBinding {
        return PastFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<MyHealthViewModel> = MyHealthViewModel::class.java

    override fun getFragmentRepository(): MyHealthRepository {
        return MyHealthRepository(remoteDataSource.buildApi(ApiServices::class.java))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(pastChallenges.isNullOrEmpty()){
            binding.noData.visibility=  View.VISIBLE
            binding.pastRecycler.visibility= View.GONE
        }else{
            binding.noData.visibility=  View.GONE
            binding.pastRecycler.visibility= View.VISIBLE
            binding.pastRecycler.adapter = challengeAdapter
            challengeAdapter.list = pastChallenges
            binding.pastRecycler.layoutManager!!.isMeasurementCacheEnabled = false;

        }
    }
}
