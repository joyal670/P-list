package com.iroid.jeetmeet.ui.main.student_panel.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentDashboardBinding
import com.iroid.jeetmeet.dialogs.FullScreenImageDialogFragment
import com.iroid.jeetmeet.dialogs.NoticeDialogFragment
import com.iroid.jeetmeet.dialogs.TutorialDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.listeners.ProfileUpdate
import com.iroid.jeetmeet.modal.student.dashboard.StudentDashboardNotice
import com.iroid.jeetmeet.ui.main.student_panel.home.activity.StudentDashboardActivity
import com.iroid.jeetmeet.ui.main.student_panel.home.adapter.StudentDashboardAdapter
import com.iroid.jeetmeet.ui.main.student_panel.home.viewmodel.StudentDashboardViewModel
import com.iroid.jeetmeet.utils.AppPreferences.prefIsStudentTutorial
import com.iroid.jeetmeet.utils.AppPreferences.prefStudentFirstName
import com.iroid.jeetmeet.utils.AppPreferences.prefStudentImg
import com.iroid.jeetmeet.utils.AppPreferences.prefStudentName
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster


class StudentDashboardFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentDashboardBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var profileUpdate: ProfileUpdate
    private lateinit var studentDashboardViewModel: StudentDashboardViewModel
    private var noticeList = ArrayList<StudentDashboardNotice>()

    var studImgUrl: String? = null
    var studName: String? = null
    var parntImgUrl: String? = null
    var parntName: String? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentDashboardBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* set title */
        fragmentTransInterface = activity as StudentDashboardActivity
        profileUpdate = activity as StudentDashboardActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.jeetmeet))
        fragmentTransInterface.setTitleinCenter(true)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(true)

    }

    override fun setupUI() {

        /* show tutorial dialog */
        /* only for one time */
        if (!prefIsStudentTutorial) {
            val dialog = TutorialDialogFragment()
            dialog.show(parentFragmentManager, "TAG")
            prefIsStudentTutorial = true
        }

        /* recyclerview */
        binding.rvStudentDashboard.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        binding.swipeRefreshLayout.setRefreshing(false)
        studentDashboardViewModel = StudentDashboardViewModel()
        studentDashboardViewModel.studentDashboard()
        studentDashboardViewModel.studentDashboardData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    binding.rvStudentDashboard.hideShimmer()

                    /* load data */

                    studImgUrl = it.data!!.student.profile_image_url
                    parntImgUrl = it.data.student.parents.image_url

                    Glide.with(requireActivity()).load(it.data.student.profile_image_url).into(binding.ivProfilePic)

                    Glide.with(requireActivity()).load(it.data.student.parents.image_url).into(binding.ivParentPic)

                    studName = it.data.student.first_name + " " + it.data.student.middle_name + " " + it.data.student.last_name

                    binding.tvStudentProfileName.text = studName

                    binding.tvStudentProfileRegNo.text = "Reg.No : " + it.data.student.reg_number
                    binding.tvStudentProfileClass.text = "Class - " + it.data.student.classname.name
                    binding.tvStudentProfileDivsion.text = "Division - " + it.data.student.divisions.name
                    binding.tvStudentProfileRollNo.text = "Roll No - " + it.data.student.roll_number
                    binding.tvStudentProfileDob.text = "DOB - " + it.data.student.dob

                    parntName = it.data.student.parents.first_name + " " + it.data.student.parents.last_name
                    binding.tvParentname.text = parntName

                    prefStudentImg = it.data.student.profile_image_url
                    prefStudentName = studName.toString()
                    prefStudentFirstName = it.data.student.first_name

                    noticeList.clear()
                    noticeList.addAll(it.data.notice)
                    binding.rvStudentDashboard.adapter = StudentDashboardAdapter(noticeList) {viewNotice(it)}

                    if(noticeList.size == 0)
                    {
                        binding.lvNoData.visibility = View.VISIBLE
                        binding.rvStudentDashboard.visibility = View.GONE
                    }

                    profileUpdate.refreshProfile()
                }
                Status.LOADING -> {
                    showProgress()

                    binding.rvStudentDashboard.showShimmer()

                    /* load no values */
                    studImgUrl = null
                    parntImgUrl = null
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivProfilePic)
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivParentPic)
                    binding.tvStudentProfileName.text = "Nill"
                    binding.tvStudentProfileRegNo.text = "Reg.No : "
                    binding.tvStudentProfileClass.text = "Class - "
                    binding.tvStudentProfileDivsion.text = "Division - "
                    binding.tvStudentProfileRollNo.text = "Roll No. - "
                    binding.tvStudentProfileDob.text = "DOB - "
                    binding.tvParentname.text = "Nill"
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()

                    /* load no values */
                    studImgUrl = null
                    parntImgUrl = null
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivProfilePic)
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivParentPic)
                    binding.tvStudentProfileName.text = "Nill"
                    binding.tvStudentProfileRegNo.text = "Reg.No : "
                    binding.tvStudentProfileClass.text = "Class - "
                    binding.tvStudentProfileDivsion.text = "Division - "
                    binding.tvStudentProfileRollNo.text = "Roll No. - "
                    binding.tvStudentProfileDob.text = "DOB - "
                    binding.tvParentname.text = "Nill"

                    binding.rvStudentDashboard.hideShimmer()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )

                    /* load no values */
                    studImgUrl = null
                    parntImgUrl = null
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivProfilePic)
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivParentPic)
                    binding.tvStudentProfileName.text = "Nill"
                    binding.tvStudentProfileRegNo.text = "Reg.No : "
                    binding.tvStudentProfileClass.text = "Class - "
                    binding.tvStudentProfileDivsion.text = "Division - "
                    binding.tvStudentProfileRollNo.text = "Roll No. - "
                    binding.tvStudentProfileDob.text = "DOB - "
                    binding.tvParentname.text = "Nill"

                    binding.rvStudentDashboard.hideShimmer()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()

                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )

                    /* load no values */
                    studImgUrl = null
                    parntImgUrl = null
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivProfilePic)
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivParentPic)
                    binding.tvStudentProfileName.text = "Nill"
                    binding.tvStudentProfileRegNo.text = "Reg.No : "
                    binding.tvStudentProfileClass.text = "Class - "
                    binding.tvStudentProfileDivsion.text = "Division - "
                    binding.tvStudentProfileRollNo.text = "Roll No. - "
                    binding.tvStudentProfileDob.text = "DOB - "
                    binding.tvParentname.text = "Nill"

                    binding.rvStudentDashboard.hideShimmer()
                }

            }
        })
    }

    /* View notice details */
    private fun viewNotice(noticeID: Int) {
        studentDashboardViewModel = StudentDashboardViewModel()
        studentDashboardViewModel.studentNotice(noticeID)
        studentDashboardViewModel.studentNoticeData.observe(this, Observer {
            when (it.status)
            {
                Status.SUCCESS ->{
                    dismissProgress()

                    val dialog = NoticeDialogFragment(it.data!!.data.title, it.data.data.description)
                    dialog.show(parentFragmentManager, "TAG")
                }
                Status.LOADING ->{
                    showProgress()
                }
                Status.DATA_EMPTY ->{
                    dismissProgress()
                }
                Status.ERROR ->{
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET ->{
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

        /* swipe to refresh */
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }

        /* student profile picture */
        binding.ivProfilePic.setOnClickListener {
            if (studImgUrl != null) {
                val dialog = FullScreenImageDialogFragment(studImgUrl!!, studName!!)
                dialog.show(parentFragmentManager, "TAG")
            }
        }

        /* parent profile picture */
        binding.ivParentPic.setOnClickListener {
            if (parntImgUrl != null) {
                val dialog = FullScreenImageDialogFragment(
                    parntImgUrl!!,
                    parntName!!
                )
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
}