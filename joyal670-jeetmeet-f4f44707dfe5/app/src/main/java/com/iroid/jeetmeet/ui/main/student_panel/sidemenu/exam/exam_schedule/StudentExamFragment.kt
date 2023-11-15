package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentExamBinding
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.student.exam_start.StudentExamStartQuestion
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentExamSaveViewModel
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel.StudentExamStartViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster

class StudentExamFragment : BaseFragment() {

    private lateinit var binding: FragmentStudentExamBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentExamStartViewModel: StudentExamStartViewModel
    private var questionList = ArrayList<StudentExamStartQuestion>()
    var totalQuestion = 0
    var currentqes = 0
    val singleAnswerHashMap: HashMap<Int, String> = HashMap()
    val descriptiveAnswerHashMap: HashMap<Int, String> = HashMap()
    val objectiveAnswerHashMap: HashMap<Int, Int> = HashMap()
    private lateinit var studentExamSaveViewModel: StudentExamSaveViewModel

    companion object {

        private const val TAG = "StudentFragment"
        private var examId: Int? = null
        private var examTimeinMilliSeconds: Long? = null
        private var examTimeinMilliString: String? = null

        @JvmStatic
        fun newInstance(type: Int, examTimeMilliSeconds: Long, examTimeString: String) =
            StudentExamFragment().apply {
                arguments = Bundle().apply {
                    putInt("user", type)
                    putLong("examTimeMilliseconds", examTimeMilliSeconds)
                    putString("examTimeStrings", examTimeString)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            examId = it.getInt("user")
            examTimeinMilliSeconds = it.getLong("examTimeMilliseconds")
            examTimeinMilliString = it.getString("examTimeStrings")
        }

        /* disable back press */
        requireActivity().onBackPressedDispatcher.addCallback(this) {
        }

    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentExamBinding.inflate(inflater!!, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.exam))
        fragmentTransInterface.setTitleinCenter(false)
        fragmentTransInterface.setFontFamily(R.font.barlow_semibold)
        fragmentTransInterface.setTitleCaptial(false)
        /* disable back arrow */
        appCompatActivity!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }

    override fun setupUI() {

        /* disable viewpager swipe */
        binding.questionsViewPager.isUserInputEnabled = false

        setupObserver()
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
        studentExamStartViewModel = StudentExamStartViewModel()
        studentExamStartViewModel.studentExam(examId!!)
        studentExamStartViewModel.studentExamStartData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    questionList.clear()
                    questionList.addAll(it.data!!.question)


                    binding.questionsViewPager.adapter = StudentExamQuestionsAdapter(questionList,
                        { quesId: Int, selectedText: String -> singleAns(quesId, selectedText) },
                        { quesId: Int, selectedText: String ->
                            descriptiveAns(
                                quesId,
                                selectedText
                            )
                        },
                        { quesId: Int, selectedId: Int -> objectiveAns(quesId, selectedId) })

                    totalQuestion = it.data.total_qustion

                    binding.rvStep.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.rvStep.adapter = TestAdapter(totalQuestion, currentqes)

                    setUpTimer()

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

    /* start exam timer */
    private fun setUpTimer() {

        var i = 0
        val timer = object : CountDownTimer(examTimeinMilliSeconds!!, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                i++
                binding.progressBar.progress = (i * 100) / (examTimeinMilliSeconds!! / 1000).toInt()

                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                binding.tvTimer.text = "$elapsedHours:$elapsedMinutes:$elapsedSeconds"
            }

            override fun onFinish() {
                setUpExamCompleteObserver()
            }
        }
        timer.start()

    }

    private fun singleAns(quesId: Int, selectedText: String) {
        singleAnswerHashMap[quesId] = selectedText
    }

    private fun descriptiveAns(quesId: Int, selectedText: String) {
        descriptiveAnswerHashMap[quesId] = selectedText
    }

    private fun objectiveAns(quesId: Int, selectedId: Int) {
        objectiveAnswerHashMap[quesId] = selectedId
    }

    override fun onClicks() {

        binding.nextButton.setOnClickListener {
            binding.questionsViewPager.currentItem = binding.questionsViewPager.currentItem + 1
            currentqes += 1
            binding.rvStep.adapter = TestAdapter(totalQuestion, currentqes)
        }

        binding.completeButton.setOnClickListener {
            setUpExamCompleteObserver()
        }

        binding.questionsViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if ((position + 1) == totalQuestion) {
                    binding.nextButton.visibility = View.GONE
                    binding.completeButton.visibility = View.VISIBLE
                }
            }

        })
    }

    /* exam complete api */
    private fun setUpExamCompleteObserver() {

        /* convert hashmap to arrayList */
        val singleAnswerEntrySet: Set<Map.Entry<Int, String>> = singleAnswerHashMap.entries
        val descriptiveAnswerEntrySet: Set<Map.Entry<Int, String>> =
            descriptiveAnswerHashMap.entries
        val objectiveAnswerEntrySet: Set<Map.Entry<Int, Int>> = objectiveAnswerHashMap.entries

        val singleListOfEntry: ArrayList<Map.Entry<Int, String>> = ArrayList(singleAnswerEntrySet)
        val descriptiveListOfEntry: ArrayList<Map.Entry<Int, String>> =
            ArrayList(descriptiveAnswerEntrySet)
        val objectiveListOfEntry: ArrayList<Map.Entry<Int, Int>> =
            ArrayList(objectiveAnswerEntrySet)

        val mergedArray = singleListOfEntry + descriptiveListOfEntry + objectiveListOfEntry
        val keysArray = ArrayList<Int>()
        val ansArray = ArrayList<String>()

        mergedArray.forEach {
            keysArray.addAll(listOf(it.key))
            ansArray.addAll(listOf(it.value.toString()))
        }

        studentExamSaveViewModel = StudentExamSaveViewModel()
        studentExamSaveViewModel.studentExamSave(examId!!, keysArray, ansArray)
        studentExamSaveViewModel.studentExamSaveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val dialogFragment = StudentExamCompleteFragment(totalQuestion)
                    dialogFragment.show(childFragmentManager, "sd")
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

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = false

    }

}