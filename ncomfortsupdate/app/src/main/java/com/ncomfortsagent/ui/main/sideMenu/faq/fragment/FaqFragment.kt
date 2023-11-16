package com.ncomfortsagent.ui.main.sideMenu.faq.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseFragment
import com.ncomfortsagent.databinding.FragmentFaqBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.listeners.FragmentTransInterface
import com.ncomfortsagent.model.faq.AgentFaqFaq
import com.ncomfortsagent.ui.main.sideMenu.activity.SideMenuActivity
import com.ncomfortsagent.ui.main.sideMenu.faq.adapter.FaqAdapter
import com.ncomfortsagent.ui.main.sideMenu.faq.viewmodel.FaqViewModel
import com.ncomfortsagent.utils.CommonUtils
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.addOnScrolledToEnd
import com.ncomfortsagent.utils.isConnected

class FaqFragment : BaseFragment() {

    private lateinit var binding: FragmentFaqBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var faqViewModel: FaqViewModel
    private var faqList = ArrayList<AgentFaqFaq>()
    private lateinit var faqAdapter: FaqAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPages: Int = 0

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFaqBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as SideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.faq))

        faqViewModel.faq("1", page.toString())
    }

    private fun setupRecyclerView() {
        faqAdapter = FaqAdapter(requireContext(), faqList)
        binding.rvFAQ.adapter = faqAdapter
        binding.rvFAQ.scheduleLayoutAnimation()
        binding.rvFAQ.addOnScrolledToEnd {
            if (!isLoading) {
                loadData()
                isLoading = true
            }
        }
    }

    private fun loadData() {
        if (page <= totalPages) {
            faqList.add(AgentFaqFaq("", -1, ""))
            faqAdapter.notifyItemInserted(faqList.size - 1)
            faqViewModel.faq("1", page.toString())
        }
    }

    override fun setupUI() {
        /* set layout manager for recyclerview */
        binding.rvFAQ.layoutManager = LinearLayoutManager(context)
    }

    override fun setupViewModel() {
        faqViewModel = FaqViewModel(requireActivity())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        faqViewModel.getAgentFaqResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    if (it.data!!.faq_data != null) {
                        totalPages = it.data.faq_data.total_page_count
                        if (faqList.size != 0) {
                            isLoading = false
                            page += 1
                            faqList.removeAt(faqList.size - 1)
                            faqAdapter.notifyItemRemoved(faqList.size)
                            faqList.addAll(it.data.faq_data.faqs)
                            faqAdapter.notifyDataSetChanged()
                        } else {
                            page += 1
                            faqList = it.data.faq_data.faqs as ArrayList<AgentFaqFaq>
                            setupRecyclerView()
                        }
                    }

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireContext().isConnected) {
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    CommonUtils.showCookieBar(
                        requireActivity(),
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        })
    }

    override fun onClicks() {

    }

}