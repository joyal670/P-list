package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.students

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentStudentProfileBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.parent_panel.home.activity.ParentDashboardActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentStudentViewViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster

class ParentStudentProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentParentStudentProfileBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentStudentViewViewModel: ParentStudentViewViewModel

    companion object {

        private var studId: Int? = null

        @JvmStatic
        fun newInstance(studentId: Int) =
            ParentStudentProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt("studentId", studentId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            studId = it.getInt("studentId")
        }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentStudentProfileBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as ParentDashboardActivity
        fragmentTransInterface.setTitleFromFragment("")
        fragmentTransInterface.setTitleinCenter(true)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(true)
    }

    override fun setupUI() {

        setupObserver()
    }

    override fun setupViewModel() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        /* student details */
        parentStudentViewViewModel = ParentStudentViewViewModel()
        parentStudentViewViewModel.parentStudentView(studId!!)
        parentStudentViewViewModel.parentStudentViewData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    Glide.with(requireContext()).load(it.data!!.data.profile_image_url)
                        .into(binding.ivStudentProfile)
                    binding.tvStudentName.text =
                        it.data.data.first_name + " " + it.data.data.middle_name + " " + it.data.data.last_name
                    binding.tvStudentGender.text = ": " + it.data.data.gender
                    binding.tvStudentClass.text = ": " + it.data.data.classname.name
                    binding.tvStudentDivision.text = ": " + it.data.data.divisions.name
                    binding.tvStudentRollNo.text = ": " + it.data.data.code
                    binding.tvStudentDob.text = ": " + it.data.data.dob
                    binding.tvStudentPhone.text = ": " + it.data.data.phone
                }
                Status.LOADING -> {
                    showProgress()
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