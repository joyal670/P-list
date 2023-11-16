package com.iroid.patrickstore.ui.my_account.reward_points


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf

import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.DialogRewardBinding
import com.iroid.patrickstore.ui.my_account.reward_points.cash_back.CashBackFragment
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Constants.ARG_REWARD
import kotlinx.android.synthetic.main.dialog_reward.*

/**
 * A simple [Fragment] subclass.
 */
class RewardPointsFragment : BaseFragment<WalletViewModal,DialogRewardBinding>(){
    companion object{
        fun newInstance(totalRewardPoints: Int) = RewardPointsFragment().apply {
            arguments = bundleOf()
            arguments!!.putInt(Constants.ARG_REWARD,0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_reward, container, false)
    }

    override fun initViews() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressBar.progress= requireArguments()[ARG_REWARD] as Int
        tvProgress.text="${requireArguments()[ARG_REWARD] as Int} /150"

    }

    override fun setUpObserver() {

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): DialogRewardBinding {
        return DialogRewardBinding.inflate(layoutInflater)
    }


}
