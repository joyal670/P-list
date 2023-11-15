package com.iroid.patrickstore.ui.seller


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager

import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.seller.adapter.SellerListAdapter
import kotlinx.android.synthetic.main.fragment_selller_list.*

/**
 * A simple [Fragment] subclass.
 */
class SellerListFragment : Fragment() {

    companion object{
        private const val TITLE = "title"
        fun newInstance(title: String) = SellerListFragment().apply {
            arguments = bundleOf(TITLE to title)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_selller_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title = arguments?.getString(TITLE)
        rvSellerList.layoutManager = LinearLayoutManager(context)
        rvSellerList.adapter = activity?.let { SellerListAdapter(it) }
    }


}
