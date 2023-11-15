package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.exam.exam_schedule

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.FragmentStudentExamCompleteBinding
import com.iroid.jeetmeet.ui.main.student_panel.home.activity.StudentDashboardActivity
import com.iroid.jeetmeet.utils.AppPreferences.prefStudentImg
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StudentExamCompleteFragment(private var totalQuestion: Int) : DialogFragment() {

    private lateinit var binding: FragmentStudentExamCompleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentExamCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme_two
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            binding.lottieAnimation.animate()
            pauseAnimation()
        }

        Glide.with(requireContext()).load(prefStudentImg).into(binding.img)

        binding.tvTotalQues.text = totalQuestion.toString()

        binding.backToHomeButton.setOnClickListener {
            val intent = Intent(requireContext(), StudentDashboardActivity::class.java)
            startActivity(intent)
            finishAffinity(requireActivity())
            //this.dismiss()
        }
    }

    suspend fun pauseAnimation() {
        delay(1000L)
        binding.frameLayout.setBackgroundResource(R.drawable.celebration_drawable)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }
    }

}