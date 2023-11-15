package com.iroid.patrickstore.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iroid.patrickstore.R
import com.iroid.patrickstore.ui.home.adapter.ProductAdpater
import com.iroid.patrickstore.utils.MarginGridDecoration


class ShppingFragment : Fragment() {

    private var rvCategoryProduct: RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        rvCategoryProduct = root.findViewById(R.id.rvCategoryProduct)
        setProductAdapter()
        return root
    }

    private fun setProductAdapter() {
        rvCategoryProduct!!.addItemDecoration(
            MarginGridDecoration(
                resources.getDimension(R.dimen.margin_small).toInt()
            )
        )
        rvCategoryProduct!!.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        rvCategoryProduct!!.adapter = activity?.let { ProductAdpater(it) }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(sectionNumber: Int): ShppingFragment {
            return ShppingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}