package com.iroid.emergency.main.settings.faq

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentFaqBinding
import com.iroid.emergency.main.settings.SettingsViewModal
import com.iroid.emergency.main.settings.faq.adapter.FaqAdapter
import com.iroid.emergency.modal.common.faq.Faq
import com.iroid.emergency.start_up.utils.Status
import com.iroid.emergency.start_up.utils.Toaster
import com.iroid.emergency.start_up.utils.isConnected
import com.iroid.emergency.start_up.utils.netDialog

class FaqFragment:BaseFragment<SettingsViewModal,FragmentFaqBinding>() {
    override fun initViews() {
        viewModel.getFaq()
    }

    override fun setUpObserver() {
        viewModel.faqLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setFaqAdapter(it.data!!.faq)
                }
                Status.ERROR -> {
                    dismissProgress()
                }

                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (requireActivity().isConnected) {
                        Toaster.showToast(
                            requireContext(),
                            "Something went wrong",
                            Toaster.State.ERROR
                        )
                    } else {
                        requireActivity().netDialog(lifecycle)
                    }
                }
            }
        })
    }

    private fun setFaqAdapter(faq: List<Faq>) {
        binding.rvFaq.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvFaq.adapter=FaqAdapter(requireContext(),faq)
        binding.rvFaq.setHasFixedSize(true)
    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentFaqBinding {
        return FragmentFaqBinding.inflate(layoutInflater)
    }
}
