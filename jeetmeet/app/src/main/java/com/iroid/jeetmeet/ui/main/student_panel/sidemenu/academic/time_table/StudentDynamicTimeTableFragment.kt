package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.academic.time_table

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentDynamicTimeTableBinding
import com.iroid.jeetmeet.modal.student.timetable.StudentTimeTableX1
import java.util.*

class StudentDynamicTimeTableFragment : BaseFragment() {

    private lateinit var binding: FragmentParentDynamicTimeTableBinding
    private lateinit var studentTimeTableAdapter: StudentTimeTableAdapter

    companion object {

        private var dayId = 0
        private const val TAG = "DynamicTimeTableFragmen"
        val PAGE_ID = "PAGE_NUM"

        fun newInstance(page: ArrayList<StudentTimeTableX1>?): StudentDynamicTimeTableFragment {
            val fragment = StudentDynamicTimeTableFragment()
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
        /* recyclerview */
        val list = requireArguments().get(PAGE_ID) as ArrayList<StudentTimeTableX1>

        if (list.size == 0) {
            binding.lvNoData.visibility = View.VISIBLE
            binding.rvTimeTable.visibility = View.GONE
        }

        Log.e(TAG, "initData: $list")
        binding.rvTimeTable.layoutManager = LinearLayoutManager(context)
        studentTimeTableAdapter = StudentTimeTableAdapter(list)
        binding.rvTimeTable.adapter = studentTimeTableAdapter
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {

    }


}