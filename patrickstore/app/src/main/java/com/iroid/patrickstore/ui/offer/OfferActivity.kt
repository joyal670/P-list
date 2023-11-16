package com.iroid.patrickstore.ui.offer

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityOfferBinding
import com.iroid.patrickstore.ui.offer.adapter.*
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.MarginGrid3Decoration
import com.iroid.patrickstore.utils.MarginGridDecoration
import com.iroid.patrickstore.utils.Status

class OfferActivity : BaseActivity<OfferViewModal,ActivityOfferBinding>() {


    override val layoutId: Int
        get() = R.layout.activity_offer
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinding(): ActivityOfferBinding {
        return ActivityOfferBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        binding.tvToolbarTitle.text=getString(R.string.home_festival_offers)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val offerAdapter = OfferMainAdapter(this)
        binding.viewpagerOffer.adapter = offerAdapter
        binding.dotIndicator.setViewPager2(binding.viewpagerOffer)

        val offerMiniAdapter = OfferMiniAdapter(this)
        binding.rvOfferMini.layoutManager =
            GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        binding.rvOfferMini.addItemDecoration(
            MarginGridDecoration(
                resources.getDimension(R.dimen.margin_medium).toInt()
            )
        )
        binding.rvOfferMini.adapter = offerMiniAdapter
        CommonUtils.setImageBase(this,"http://drive.google.com/uc?export=view&id=1YeU-6VYNk5X_lNpJ59M0ZnTm74Sprg2-",binding.ivBanner)






        viewModel.getFestivalLiveData()

        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.festivalLiveData.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    dismissProgress()
                    if(it.data!!.data.sellers.isNotEmpty()){
                        val brandAdapter = FeaturedBrandAdapter(this,it.data!!.data.sellers)
                        binding.rvBrand.layoutManager =
                            GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false)
                        binding.rvBrand.addItemDecoration(
                            MarginGridDecoration(
                                resources.getDimension(R.dimen.margin_medium).toInt()
                            )
                        )
                        binding.rvBrand.adapter=brandAdapter
                    }
                    if(it.data!!.data.products.isNotEmpty()){
                        val campignAdapter = CampignAdapter(this,it.data!!.data.products)
                        binding.rvCampaign.layoutManager =
                            GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
                        binding.rvCampaign.adapter=campignAdapter
                    }
                    if(it.data!!.data.categories.isNotEmpty()){
                        val categoryAdapter = CategoryAdapter(this,it.data!!.data.categories)
                        binding.rvCategory.layoutManager =
                            GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
                        binding.rvCategory.adapter=categoryAdapter
                    }

                }
                Status.LOADING->{
                    showProgress()
                }
                Status.ERROR->{
                    dismissProgress()
                    binding.noData.visibility= View.VISIBLE
                    binding.mainData.visibility=View.GONE
                }
                Status.NO_INTERNET->{
                    dismissProgress()
                }
            }
        })
    }


}
