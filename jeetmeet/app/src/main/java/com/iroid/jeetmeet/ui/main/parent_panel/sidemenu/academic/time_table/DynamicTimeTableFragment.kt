package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.academic.time_table

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentDynamicTimeTableBinding
import com.iroid.jeetmeet.modal.parent.time_table.ParentTimeTable
import java.util.*

class DynamicTimeTableFragment : BaseFragment() {

    private lateinit var binding: FragmentParentDynamicTimeTableBinding

    companion object {

        private var dayId = 0
        private const val TAG = "DynamicTimeTableFragmen"
        val PAGE_ID = "PAGE_NUM"

        fun newInstance(page: ArrayList<ParentTimeTable>?): DynamicTimeTableFragment {
            val fragment = DynamicTimeTableFragment()
            val args = Bundle()
            args.putParcelableArrayList(PAGE_ID, page)
            fragment.arguments = args
            return fragment
        }
    }


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentDynamicTimeTableBinding.inflate(
            inflater!!, container, false
        )
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        dayId = arguments?.getInt(PAGE_ID)!!

    }

    override fun initData() {

        val list = requireArguments().get(PAGE_ID) as ArrayList<ParentTimeTable>

        if (list.size == 0) {
            binding.lvNoData.visibility = View.VISIBLE
            binding.rvTimeTable.visibility = View.GONE
        }

        /* recyclerview */
        binding.rvTimeTable.layoutManager = LinearLayoutManager(context)
        binding.rvTimeTable.adapter = ParentTimeTableAdapter(list)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }
}