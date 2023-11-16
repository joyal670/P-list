package com.iroid.jeetmeet.ui.main.parent_panel.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentdashboardBinding
import com.iroid.jeetmeet.dialogs.FullScreenImageDialogFragment
import com.iroid.jeetmeet.dialogs.NoDataDialogFragment
import com.iroid.jeetmeet.dialogs.TutorialDialogFragment
import com.iroid.jeetmeet.listeners.AdaptorListener
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListData
import com.iroid.jeetmeet.ui.main.parent_panel.home.activity.ParentDashboardActivity
import com.iroid.jeetmeet.ui.main.parent_panel.home.viewmodel.ParentDashboardViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.students.ParentStudentProfileFragment
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.students.ParentStudentsAdapter
import com.iroid.jeetmeet.utils.AppPreferences
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment


class ParentDashboardFragment : BaseFragment(), AdaptorListener {
    private lateinit var binding: FragmentParentdashboardBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentDashboardViewModel: ParentDashboardViewModel
    var parentImgUrl: String? = null
    var parentName: String? = null
    private var studentList = ArrayList<ParentStudentsListData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentdashboardBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as ParentDashboardActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.jeetmeet))
        fragmentTransInterface.setTitleinCenter(true)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(true)
    }

    override fun setupUI() {

       /* *//* show tutorial dialog *//*
        *//* only for one time *//*
        if (!AppPreferences.prefIsStudentTutorial) {
            val dialog = TutorialDialogFragment()
            dialog.show(parentFragmentManager, "TAG")
            AppPreferences.prefIsStudentTutorial = true
        }*/

        setupObserver()
    }

    override fun setupViewModel() {
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* Dashboard api */
        parentDashboardViewModel = ParentDashboardViewModel()
        parentDashboardViewModel.parentDashboard()
        parentDashboardViewModel.parentDashboardData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    /* binding.tvStudents.text = it.data!!.data.count_student.toString()
                     binding.tvTeachers.text = it.data.data.count_teacher.toString()
                     binding.tvParents.text = it.data.data.count_parents.toString()
                     binding.tvClass.text = it.data.data.count_class.toString()*/
                    binding.tvParentName.text =
                        it.data!!.data.parent.first_name + " " + it.data.data.parent.last_name

                    Glide.with(requireContext()).load(it.data.data.parent.image_url)
                        .into(binding.ivProfilePic)

                    parentImgUrl = it.data.data.parent.image_url
                    parentName =
                        it.data.data.parent.first_name + " " + it.data.data.parent.last_name

                    AppPreferences.prefStudentImg = parentImgUrl!!
                    AppPreferences.prefStudentName = parentName.toString()

                    setUpStudentObserver()
                }
                Status.LOADING -> {
                    showProgress()
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
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
            }
        })
    }

    private fun setUpStudentObserver() {
        parentDashboardViewModel = ParentDashboardViewModel()
        parentDashboardViewModel.parentStudentsList()
        parentDashboardViewModel.parentStudentsListData.observe(this, {
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

        /* view profile pic */
        binding.ivProfilePic.setOnClickListener {
            if (parentImgUrl != null) {
                val dialog = FullScreenImageDialogFragment(parentImgUrl!!, parentName!!)
                dialog.show(parentFragmentManager, "TAG")
            }
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
      /*  appCompatActivity?.replaceFragment(
            fragment = ParentStudentProfileFragment.newInstance(id),
            addToBackStack = true
        )*/
    }

}