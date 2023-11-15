package com.property.propertyagent.agent_panel.ui.main.sidemenu.feedback

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.ProfileActivity
import com.property.propertyagent.base.BaseFragment
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.listeners.FragmentTransInterface
import com.property.propertyagent.modal.agent.agent_feedback.Feedback
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.addOnScrolledToEnd
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.fragment_feedback.*

class FeedbckFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var feedBackViewModel: FeedBackViewModel

    private var feedBackList = ArrayList<Feedback>()
    private lateinit var feedbackAdapter: FeedbackAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater?.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as ProfileActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.feedback))

        feedBackViewModel.agentFeedBack(page.toString())
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        feedBackViewModel = FeedBackViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        feedBackViewModel.getAgentFeedBackResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        if (page == 1) {
                            showProgress()
                        }
                    }
                    Status.SUCCESS -> {
                        if (page == 1) {
                            dismissProgress()
                        }
                        if (!it.data!!.data.feedbacks.equals(null)) {
                            totalPages = it.data.data.total_page_count
                            if (feedBackList.size != 0) {
                                isLoading = false
                                page += 1
                                feedBackList.removeAt(feedBackList.size - 1)
                                feedbackAdapter.notifyItemRemoved(feedBackList.size)
                                feedBackList.addAll(it.data.data.feedbacks)
                                feedbackAdapter.notifyDataSetChanged()
                            } else {
                                if (it.data.data.feedbacks.isNotEmpty()) {
                                    page += 1
                                    feedBackList =
                                        it.data.data.feedbacks as ArrayList<Feedback>
                                    setFeedBackSellAllRecyclerView()
                                } else {
                                    llEmptyData.isVisible = true
                                    feedbackfrgRecylerview.isVisible = false
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(), getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            requireContext(),
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (requireContext().isConnected) {
                            Toaster.showToast(
                                requireContext(), getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(requireActivity())
                            dialog.show(parentFragmentManager, "TAG")
                        }
                    }
                }
            })
    }

    override fun onClicks() {

    }

    private fun setFeedBackSellAllRecyclerView() {
        feedbackfrgRecylerview.layoutManager = LinearLayoutManager(context)
        feedbackAdapter = FeedbackAdapter(requireContext(), feedBackList)
        feedbackfrgRecylerview.adapter = feedbackAdapter
        feedbackfrgRecylerview.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    fun loadData() {
        if (page <= totalPages) {
            feedBackList.add(Feedback("", "", -1, "", " ", -1))
            feedbackAdapter.notifyItemInserted(feedBackList.size - 1)
            feedBackViewModel.agentFeedBack(page.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            feedBackViewModel.agentFeedBack(page.toString())
        }
    }
}