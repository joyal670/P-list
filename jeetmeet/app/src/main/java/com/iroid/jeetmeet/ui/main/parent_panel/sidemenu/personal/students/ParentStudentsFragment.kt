package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.students

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentStudentsBinding
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.listeners.AdaptorListener
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListData
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentStudentsListViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment

class ParentStudentsFragment : BaseFragment(), AdaptorListener {

    private lateinit var binding: FragmentParentStudentsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentStudentsListViewModel: ParentStudentsListViewModel
    private var studentList = ArrayList<ParentStudentsListData>()

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentStudentsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentStudentsBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.students))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        binding.rvParentStudents.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* students list api */
        parentStudentsListViewModel = ParentStudentsListViewModel()
        parentStudentsListViewModel.parentStudentsList()
        parentStudentsListViewModel.parentStudentsListData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    studentList.clear()
                    studentList.addAll(it.data!!.data)
                    binding.rvParentStudents.adapter = ParentStudentsAdapter(this, studentList)

                    if (studentList.size == 0) {
                        val dialog = NoDataDialogFragment()
                        dialog.show(parentFragmentManager, "TAG")
                    }
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })

    }

    override fun onClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }

    override fun onItemViewClick(id: Int) {
        appCompatActivity?.replaceFragment(
            fragment = ParentStudentProfileFragment.newInstance(id),
            addToBackStack = true
        )
    }

}