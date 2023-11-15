package com.iroid.patrickstore.ui.my_account.reward_points.cash_back


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseFragment
import com.iroid.patrickstore.databinding.FragmentCashBackBinding
import com.iroid.patrickstore.ui.my_account.reward_points.WalletViewModal
import com.iroid.patrickstore.ui.my_account.reward_points.cash_back.adapter.CashBackItemAdapter
import com.iroid.patrickstore.utils.Status
import kotlinx.android.synthetic.main.activity_search.view.*
import kotlinx.android.synthetic.main.fragment_cash_back.*
import kotlinx.android.synthetic.main.layout_no_data.view.*
import kotlin.math.roundToInt


class CashBackFragment : BaseFragment<WalletViewModal, FragmentCashBackBinding>() {
    companion object {
        fun newInstance() = CashBackFragment().apply {
            arguments = bundleOf()
        }
    }






    override fun initViews() {


    }

    override fun setUpObserver() {
        viewModel.cashBackLiveData.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS->{
                    if(it.data!!.data.items.isNotEmpty()){
                        constraintCashback.visibility=View.VISIBLE
                        rvCashBackItem.visibility=View.VISIBLE
                        binding.rvCashBackItem.layoutManager = LinearLayoutManager(activity)
                        binding.rvCashBackItem.adapter = CashBackItemAdapter(it.data!!.data.items)
                        binding.tvTotal.text= it.data.data.cashbackAmount.roundToInt().toString()
                        noData.visibility=View.GONE
                    }else{
                        constraintCashback.visibility=View.GONE
                        rvCashBackItem.visibility=View.GONE
                        noData.visibility=View.VISIBLE
                        noData.noDataLottie.setAnimation(R.raw.cashback)
                        noData.noDataLottie.loop(true)
                        noData.noDataLottie.playAnimation()
                        noData.tvNoData.text="Add money to Wallet using purchase order"

                    }
                }
                Status.LOADING->{

                }
                Status.ERROR->{

                }
                Status.NO_INTERNET->{

                }
            }
        })
    }

    override fun setOnClick() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpObserver()
        viewModel.getCashbackLiveData()

    }

    override fun getViewBinding(): FragmentCashBackBinding {
        return FragmentCashBackBinding.inflate(layoutInflater)
    }


}
