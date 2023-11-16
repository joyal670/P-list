package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_scheduled

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentTestScheduleInstructionsBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentMockTestPreviewViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment


class StudentTestScheduleInstructionsFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentTestScheduleInstructionsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentMockTestPreviewViewModel: StudentMockTestPreviewViewModel

    var studentExamId = 0
    var examTimeinMilliSeconds: Long? = null
    var examTimeinString = ""
    var totQues = 0

    companion object {

        private const val TAG = "StudentMockTestFragment"
        private var testId: Int? = null

        @JvmStatic
        fun newInstance(type: Int) =
            StudentTestScheduleInstructionsFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", type)
                }
            }
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            testId = it.getInt("id")
        }
    }


    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentStudentTestScheduleInstructionsBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.test_scheduled))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        setupObserver()
    }

    override fun setupViewModel() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        /* test preview */
        studentMockTestPreviewViewModel = StudentMockTestPreviewViewModel()
        studentMockTestPreviewViewModel.studentDiaries(testId!!)
        studentMockTestPreviewViewModel.studentMockTestPreviewData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    Glide.with(requireContext()).load(it.data!!.student.pic_url)
                        .into(binding.ivProfilePic)
                    binding.tvSubject.text = it.data.test.subjectname.name
                    binding.tvExamCategory.text = it.data.test.name
                    binding.tvClass.text =
                        it.data.student.classname.name + " " + it.data.student.divisions.name
                    binding.tvques.text = it.data.test.question_count.toString()
                    binding.tvMark.text = it.data.totalmark
                    binding.tvTime.text = it.data.test.total_time
                    binding.tvInstructions.text = it.data.test.instructionname.content

                    studentExamId = it.data.student.id
                    totQues = it.data.test.question_count

                    val hr = it.data.test.total_time.substring(0, 2)
                    val min = it.data.test.total_time.substring(3, 5)

                    val temp1 = hr.toInt()
                    val hrSec = (temp1 * 60 * 60 * 1000)

                    val temp = min.toInt()
                    val minSec = (temp * 60000)

                    val currentMilliSec = hrSec + minSec

                    examTimeinMilliSeconds = currentMilliSec.toLong()
                    examTimeinString = it.data.test.total_time
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

    override fun onClicks() {
        binding.startExamButton.setOnClickListener {
            if (totQues == 0) {
                Toaster.showToast(
                    requireContext(),
                    "Oops no questions are available",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                appCompatActivity?.replaceFragment(
                    fragment = StudentTestFragment.newInstance(
                        testId!!,
                        examTimeinMilliSeconds!!,
                        examTimeinString
                    ),
                    addToBackStack = true
                )
            }
        }
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }

}