package com.property.propertyuser.ui.main.side_menu.refer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bolaware.walk_through_guider.AnchorView
import com.bolaware.walk_through_guider.WalkThroughDialog
import com.flaviofaria.kenburnsview.RandomTransitionGenerator
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentReferAFriendBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.ui.main.side_menu.SideMenuActivity
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.copyToClipboard
import com.property.propertyuser.utils.isConnected
import kotlinx.android.synthetic.main.fragment_refer_a_friend.*
import kotlinx.android.synthetic.main.layout_no_network.*


class ReferAFriendFragment : BaseFragment() {
    private lateinit var activityListener: ActivityListener
    private lateinit var binding: FragmentReferAFriendBinding
    private lateinit var referAFriendViewModel: ReferAFriendViewModel
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReferAFriendBinding.inflate(inflater!!, container, false)
        val view = binding.root
        return view
        /* return inflater?.inflate(R.layout.fragment_refer_a_friend,container,false)*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener = activity as SideMenuActivity

    }

    override fun initData() {
        activityListener.setTitle(getString(R.string.refer))
        referAFriendViewModel.fetchReferralCode()
    }

    override fun setupUI() {

        val interpolator = AccelerateDecelerateInterpolator()
        val generator = RandomTransitionGenerator(3500, interpolator)
        ivReferal.setTransitionGenerator(generator)

    }

    override fun setupViewModel() {
        referAFriendViewModel = ReferAFriendViewModel()
    }

    override fun setupObserver() {
        referAFriendViewModel.getReferralCode().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    includeNoInternet.visibility = View.GONE
                    if (it.data != null) {
                        constraintMainRefer.visibility = View.VISIBLE
                        linearNoDataFound.visibility = View.GONE
                        Log.e("response", Gson().toJson(it))
                        tvReferalCode.text = it.data.referral_code

                        //setWalkThrough()
                        /*  if(!isReferTutorial){
                              isReferTutorial = true
                              setWalkThrough()
                          }*/

                    } else {
                        constraintMainRefer.visibility = View.GONE
                        linearNoDataFound.visibility = View.VISIBLE
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        requireContext(), it.data!!.status.toString(),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(), getString(R.string.something_wrong),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternet.visibility = View.VISIBLE
                        constraintMainRefer.visibility = View.GONE
                        linearNoDataFound.visibility = View.GONE
                    }
                }

            }
        })
    }

    private fun setWalkThrough() {
        WalkThroughDialog(
            requireActivity(),
            listOf(
                AnchorView(
                    binding.tvReferalCode,
                    "Long click to copy text to clipboard"
                )
            )
        ).setAroundColor(R.color.transparentColor) //optional, advisable to make transparent
            .setContentTintColor(android.R.color.white) //optional
            .setHighLighterColor(android.R.color.white) //optional
            .setStepsPageIndicatorTextColor(android.R.color.darker_gray) //optional
            .show() //compulsory
    }

    override fun onClicks() {
        tvReferalCode.setOnLongClickListener {
            Log.e("copied", tvReferalCode.text.toString())
            requireContext().copyToClipboard(tvReferalCode.text)
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.copied),
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        btnTryAgain.setOnClickListener {
            if (requireContext().isConnected) {
                includeNoInternet.visibility = View.GONE
                constraintMainRefer.visibility = View.VISIBLE
                linearNoDataFound.visibility = View.GONE
                referAFriendViewModel.fetchReferralCode()
            }
        }
        btnRefer.setOnClickListener {
            btnRefer.startAnimation()
            Handler(Looper.getMainLooper()).postDelayed({
                val stringBuilder = java.lang.StringBuilder()
                stringBuilder.append("Install Beybaan using this link :")
                stringBuilder.append("\nhttps://admin.siaaha.com")
                stringBuilder.append("\nRedeem code :\t " + tvReferalCode.text.toString())
                shareTest(stringBuilder.toString())
                btnRefer.revertAnimation()
            }, 1500)

        }

    }

    private fun shareTest(content: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/*"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}