package com.ncomfortsagent.ui.main.sideMenu.feedback.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentFeedbackBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.listeners.FragmentTransInterface
import com.ncomfortsagent.ui.main.home.home.activity.HomeActivity
import com.ncomfortsagent.ui.main.sideMenu.activity.SideMenuActivity
import com.ncomfortsagent.ui.main.sideMenu.feedback.viemodel.FeedbackViewModel
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.CommonUtils.Companion.dismissKeyboard
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected
import java.util.*


class FeedbackFragment : BaseFragment() {
    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var feedbackViewModel: FeedbackViewModel

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as SideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.feedback))
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        feedbackViewModel = FeedbackViewModel(requireActivity())
    }

    override fun setupObserver() {

        feedbackViewModel.getAgentFeedbackResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.success),
                        it.data!!.response,
                        R.color.de_york
                    )
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                        }
                    }, 1000)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireActivity().isConnected) {
                        CommonUtils.showCookieBar(
                            requireActivity(),
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(requireActivity())
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
            }
        }
    }

    override fun onClicks() {

        binding.sendFeedbackBtn.setOnClickListener {
            val feedback = binding.tvFeedback.text.toString()
            if (feedback.isBlank()) {
                binding.tvMain.error = getString(R.string.required)
            } else {
                requireActivity().dismissKeyboard()
                feedbackViewModel.feedback(feedback)
            }
        }

        binding.tvFeedback.doOnTextChanged { text, start, before, count ->
            binding.tvMain.isErrorEnabled = false
        }
    }


}