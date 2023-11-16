package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textview.MaterialTextView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentExamResultDeatilsBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.exam_result_details.StudentExamResultDetailsData
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentExamResultDetailsViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.iroid.jeetmeet.utils.replaceFragment


class StudentExamResultDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentExamResultDeatilsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentExamResultDetailsViewModel: StudentExamResultDetailsViewModel
    private var examDetailsList = ArrayList<StudentExamResultDetailsData>()

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentExamResultDeatilsBinding.inflate(inflater!!, container, false)
        return binding.root
    }


    companion object {

        private const val TAG = "StudentFragment"
        private var examId: Int? = null

        @JvmStatic
        fun newInstance(id: Int) =
            StudentExamResultDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", id)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            examId = it.getInt("id")
        }

    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.exam_result))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    override fun setupUI() {

        /* recyclerview */
        binding.rvStudentExamResultDetails.layoutManager = LinearLayoutManager(context)

        setupObserver()
    }

    override fun setupViewModel() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        /* api call */
        studentExamResultDetailsViewModel = StudentExamResultDetailsViewModel()
        studentExamResultDetailsViewModel.studentResult(examId!!)
        studentExamResultDetailsViewModel.studentExamResultDetailsData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvTypeOfExam).text =
                        ": " + it.data!!.exam_details.exams_category.name
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvExamTitle).text =
                        ": " + it.data.exam_details.name
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvTotalQues).text =
                        ": " + it.data.total_questions.toString()
                    binding.expandable.secondLayout.findViewById<MaterialTextView>(R.id.tvTotalMark).text =
                        ": " + it.data.total_mark.toString()

                    examDetailsList.clear()
                    examDetailsList.addAll(it.data.data)
                    binding.rvStudentExamResultDetails.adapter =
                        StudentExamResultDetailsAdapter(examDetailsList) { totalmark: Int, mark: Int, question: String, ans: String ->
                            viewResults(
                                totalmark,
                                mark,
                                question,
                                ans
                            )
                        }

                    binding.tvGrade.text = "Grade - " + it.data.grade.toString().take(6)
                    binding.tvTotalMar.text = "Total Mark - " + it.data.total_mark.toString()


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

    private fun viewResults(totalmark: Int, mark: Int, question: String, ans: String) {
        appCompatActivity?.replaceFragment(
            fragment = StudentExamResultQuestionDetailsFragment.newInstance(
                totalmark,
                mark,
                question,
                ans
            ), addToBackStack = true
        )
    }

    override fun onClicks() {

        /* expand and collapse */
        var clicked = false
        binding.expandable.parentLayout.setOnClickListener {
            if (!clicked) {
                binding.expandable.expand()
                clicked = true
            } else {
                binding.expandable.collapse()
                clicked = false
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