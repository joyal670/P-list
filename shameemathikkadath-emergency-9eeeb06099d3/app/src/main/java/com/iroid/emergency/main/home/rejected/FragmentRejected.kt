package com.iroid.emergency.main.home.rejected

import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.emergency.base.BaseFragment
import com.iroid.emergency.databinding.FragmentAcceptedBinding
import com.iroid.emergency.main.home.HomeViewModal
import com.iroid.emergency.main.home.rejected.adapter.RejectedAdapter
import com.iroid.emergency.modal.common.RejectedRequest
import com.iroid.emergency.preference.ConstantPreference
import java.util.ArrayList

class FragmentRejected : BaseFragment<HomeViewModal, FragmentAcceptedBinding>() {
    private lateinit var rejectedAdapter: RejectedAdapter

    companion object {
        fun newInstance(acceptedList: List<RejectedRequest>) = FragmentRejected().apply {
            arguments = bundleOf()
            arguments!!.putParcelableArrayList(ConstantPreference.ARG_ACCEPTED,acceptedList  as ArrayList<out Parcelable>)
        }
    }

    override fun initViews() {
        val rejectedList:ArrayList<RejectedRequest> = requireArguments().getParcelableArrayList(ConstantPreference.ARG_ACCEPTED)!!
        if(rejectedList.isNotEmpty()){
            binding.rvAccepted.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rejectedAdapter = RejectedAdapter(requireContext(),rejectedList)
            binding.rvAccepted.adapter = rejectedAdapter
            binding.noData.visibility= View.GONE
            binding.rvAccepted.visibility=View.VISIBLE
        }else{
            binding.noData.visibility= View.VISIBLE
            binding.rvAccepted.visibility=View.GONE
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
