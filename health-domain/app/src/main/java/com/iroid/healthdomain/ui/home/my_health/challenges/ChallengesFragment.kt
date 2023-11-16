package com.iroid.healthdomain.ui.home.my_health.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.data.model_class.index.PendingChallenge
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.databinding.ChallengesFragmentBinding
import com.iroid.healthdomain.ui.adaptor.ChallengesAdaptor
import com.iroid.healthdomain.ui.adptorInterface.AdaptorInterface
import com.iroid.healthdomain.ui.base.BaseFragment

class ChallengesFragment(private val challenges: List<PendingChallenge>) :
        BaseFragment<ChallengesViewModel, ChallengesFragmentBinding, ChallengesRepository>(), AdaptorInterface, View.OnClickListener {

    private val challengesAdaptor: ChallengesAdaptor by lazy { ChallengesAdaptor(this) }

    companion object {
        private const val TAG = "ChallengesFragment"
        fun newInstance(challenges: List<PendingChallenge>) = ChallengesFragment(challenges)

    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): ChallengesFragmentBinding {
        return ChallengesFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<ChallengesViewModel> = ChallengesViewModel::class.java

    override fun getFragmentRepository(): ChallengesRepository {
        return ChallengesRepository(remoteDataSource.buildApi(ApiServices::class.java))
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if (challenges != null) {
            if(challenges.isNotEmpty()){
                binding.challengViewPage.adapter = challengesAdaptor
                challengesAdaptor.list = challenges
                binding.materialTextView13.visibility=View.GONE
                binding.materialTextView14.visibility=View.GONE
                binding.challengViewPage.visibility=View.VISIBLE
                binding.nextButton.visibility=View.VISIBLE
                binding.preButton.visibility=View.VISIBLE
            }else{
                binding.materialTextView13.visibility=View.VISIBLE
                binding.materialTextView14.visibility=View.VISIBLE
                binding.challengViewPage.visibility=View.GONE
                binding.nextButton.visibility=View.GONE
                binding.preButton.visibility=View.GONE

            }

        }



        binding.challengViewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position > 0 && positionOffset == 0.0f && positionOffsetPixels == 0) {
                    binding.challengViewPage.layoutParams.height =
                            binding.challengViewPage.getChildAt(0).height
                }
            }
        })

        binding.challengViewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        })

        binding.nextButton.setOnClickListener(this)
        binding.preButton.setOnClickListener(this)


    }



    override fun onItemClick(contactModel: ContactData) {

    }

    override fun favorite(id: Int, following: Int) {

    }

    override fun refreshAllContact() {

    }

    override fun onClick(v: View?) {
        var itemPosition: Int = binding.challengViewPage.currentItem
        when (v?.id) {
            R.id.nextButton -> {
                //Toast.makeText(requireContext(), "$itemPosition", Toast.LENGTH_SHORT).show()
                binding.challengViewPage.currentItem = itemPosition + 1
            }
            R.id.preButton -> {
                //Toast.makeText(requireContext(), "$itemPosition", Toast.LENGTH_SHORT).show()
                binding.challengViewPage.currentItem = itemPosition - 1
            }
        }
    }


}