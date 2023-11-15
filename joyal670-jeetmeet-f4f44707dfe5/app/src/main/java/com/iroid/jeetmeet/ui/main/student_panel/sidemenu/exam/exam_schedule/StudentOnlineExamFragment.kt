package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentOnlineExamBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentExamSchedulePreviewViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class StudentOnlineExamFragment : BaseFragment() {

    private lateinit var binding: FragmentStudentOnlineExamBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentOnlineExamSchedulePreviewViewModel: StudentExamSchedulePreviewViewModel
    var studentExamId = 0
    var examTimeinMilliSeconds: Long? = null
    var examTimeinString = ""
    var totQues = 0

    companion object {

        private const val TAG = "StudentExamFragment"
        private var user: Int? = null

        @JvmStatic
        fun newInstance(type: Int) =
            StudentOnlineExamFragment().apply {
                arguments = Bundle().apply {
                    putInt("user", type)
                }
            }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentOnlineExamBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.online_exam))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {
        /* recyclerview */
        //binding.rvParentStudents.layoutManager = LinearLayoutManager(context)
        //binding.rvParentStudents.adapter = ParentStudentsAdapter()

        setupExamObserver(user!!)
    }

    @SuppressLint("SetTextI18n")
    private fun setupExamObserver(exam_id: Int) {

        Log.e(TAG, "setupExamObserver:$exam_id")

        /* api call */
        studentOnlineExamSchedulePreviewViewModel = StudentExamSchedulePreviewViewModel()
        studentOnlineExamSchedulePreviewViewModel.studentExamSchedule(exam_id)
        studentOnlineExamSchedulePreviewViewModel.studentExamSchedulePreviewData.observe(
            this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()

                        Glide.with(requireContext()).load(it.data!!.student.image_url)
                            .into(binding.ivProfilePic)
                        binding.tvSubject.text = ": " + it.data.exam.subjects.name
                        binding.tvExamCategory.text = ": " + it.data.exam.exams_category.name
                        binding.tvClass.text =
                            ": " + it.data.exam.classes.name + " " + it.data.exam.divisions.name
                        binding.tvTypeOfExam.text = ": " + it.data.exam.name
                        binding.tvques.text = ": " + it.data.total_qustion
                        binding.tvMark.text = ": " + it.data.totalmark.toString()
                        binding.tvTime.text = ": " + it.data.totalDuration + " Hr"
                        binding.tvInstructions.text = it.data.exam.instructions.content
                        totQues = it.data.total_qustion

                        studentExamId = it.data.exam.id

                        val inputTimeFormat: DateFormat =
                            SimpleDateFormat("HH:mm", Locale.getDefault())
                        val inputTimeFormat1: DateFormat =
                            SimpleDateFormat("mm", Locale.getDefault())
                        val inputTime: Date? = inputTimeFormat.parse(it.data.totalDuration.take(4))
                        val inputTime1: Date? =
                            inputTimeFormat1.parse(it.data.totalDuration.substring(3, 5))

                        val hr = it.data.totalDuration.substring(0, 2)
                        val min = it.data.totalDuration.substring(3, 5)
                        Log.e("TAG", "setupExamObserver: " + hr)
                        Log.e("TAG", "setupExamObserver: " + min)

                        val temp1 = hr.toInt()
                        val hrSec = (temp1 * 60 * 60 * 1000)
                        Log.e(TAG, "setupExamObserver: hrSec " + hrSec)

                        val temp = min.toInt()
                        val minSec = (temp * 60000)
                        Log.e(TAG, "setupExamObserver: minSec " + minSec)

                        val currentMilliSec = hrSec + minSec
                        Log.e(TAG, "setupExamObserver: currentMilliSec " + currentMilliSec)


                        examTimeinMilliSeconds = currentMilliSec.toLong()
                        examTimeinString = it.data.totalDuration

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

                        binding.startExamButton.visibility = View.GONE
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
                        binding.startExamButton.visibility = View.GONE
                    }
                }
            })
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {


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
                    fragment = StudentExamFragment.newInstance(
                        studentExamId,
                        examTimeinMilliSeconds!!,
                        examTimeinString
                    ),
                    addToBackStack = true
                )
            }

            /*val intent= Intent(requireContext(), StudentExamScheduleActivity::class.java)
            startActivity(intent)*/
        }

    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            user = it.getInt("user")
        }
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }
}