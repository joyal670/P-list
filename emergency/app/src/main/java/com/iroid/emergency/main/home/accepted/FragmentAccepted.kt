package com.iroid.emergency.main.home.accepted

import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentAcceptedBinding
import com.iroid.emergency.main.home.HomeViewModal
import com.iroid.emergency.main.home.accepted.adapter.AcceptedAdapter
import com.iroid.emergency.modal.common.home.AcceptedRequest
import com.iroid.emergency.preference.ConstantPreference

class FragmentAccepted : BaseFragment<HomeViewModal, FragmentAcceptedBinding>() {
    private lateinit var acceptedAdapter: AcceptedAdapter


    companion object {
        fun newInstance(acceptedList: List<AcceptedRequest>) = FragmentAccepted().apply {
            arguments = bundleOf()
            arguments!!.putParcelableArrayList(
                ConstantPreference.ARG_ACCEPTED,
                acceptedList as ArrayList<out Parcelable>
            )
        }
    }

    override fun initViews() {
        val acceptedList: ArrayList<AcceptedRequest> =
            requireArguments().getParcelableArrayList(ConstantPreference.ARG_ACCEPTED)!!
        if (acceptedList.isNotEmpty()) {
            binding.rvAccepted.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            acceptedAdapter = AcceptedAdapter(requireContext(), acceptedList)
            binding.rvAccepted.adapter = acceptedAdapter
            binding.noData.visibility = View.GONE
            binding.rvAccepted.visibility = View.VISIBLE
        } else {
            binding.noData.visibility = View.VISIBLE
            binding.rvAccepted.visibility = View.GONE
        }

    }

    override fun setUpObserver() {

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentAcceptedBinding {
        return FragmentAcceptedBinding.inflate(layoutInflater)
    }
}
