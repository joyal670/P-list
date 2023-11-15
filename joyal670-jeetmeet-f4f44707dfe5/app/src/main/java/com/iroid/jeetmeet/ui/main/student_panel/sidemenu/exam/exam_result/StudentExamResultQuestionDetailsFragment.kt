package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentExamResultQuestionDetailsBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity


class StudentExamResultQuestionDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentExamResultQuestionDetailsBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentStudentExamResultQuestionDetailsBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    companion object {

        private const val TAG = "StudentFragment"
        private var total: Int? = null
        private var mark: Int? = null
        private var question: String? = null
        private var ans: String? = null

        @JvmStatic
        fun newInstance(totalM: Int, mark: Int, question: String, ans: String) =
            StudentExamResultQuestionDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("total", totalM)
                    putInt("mark", mark)
                    putString("question", question)
                    putString("ans", ans)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            total = it.getInt("total")
            mark = it.getInt("mark")
            question = it.getString("question")
            ans = it.getString("ans")
        }

    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.qanda))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
    }

    @SuppressLint("SetTextI18n")
    override fun setupUI() {
        binding.tvQuesMark.text = "Question Mark - " + total.toString()
        binding.tvMark.text = "Mark - " + mark.toString()
        binding.tvques.text = question
        binding.tvAns.text = ans
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
        item2.isVisible = false

    }
}