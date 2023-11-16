package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.mock_test.test_scheduled

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.databinding.RecycleMockTestListItemBinding
import com.iroid.jeetmeet.modal.student.mock_test_start.StudentMockTestStartOption
import com.iroid.jeetmeet.modal.student.mock_test_start.StudentMockTestStartQuestion
import com.iroid.jeetmeet.utils.Toaster
import org.aviran.cookiebar2.CookieBar


class StudentMockTestQuestionsAdapter(
    private var questionList: ArrayList<StudentMockTestStartQuestion>,
    private var selected: (Int, Int) -> Unit
) :
    RecyclerView.Adapter<StudentMockTestQuestionsAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: RecycleMockTestListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view =
            RecycleMockTestListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.binding.materialTextView5.text = questionList[position].question

        val opsize = questionList[position].options.size
        val optionList = ArrayList<StudentMockTestStartOption>()
        optionList.addAll(questionList[position].options)

        optionList.forEach {
            val radioButton1 = RadioButton(context)
            radioButton1.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            radioButton1.text = it.name
            radioButton1.id = it.id
            holder.binding.radioGroup.addView(radioButton1)
        }

        holder.binding.submitBtn.setOnClickListener {
            if (holder.binding.radioGroup.checkedRadioButtonId == -1) {
                Toaster.showToast(
                    context!!,
                    "Select any option",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                optionList.forEach {
                    if (it.id == holder.binding.radioGroup.checkedRadioButtonId) {
                        if (it.answer == 1) {

                            CookieBar.build(context as Activity)
                                .setTitle("Correct Answer")
                                .setCookiePosition(CookieBar.BOTTOM)
                                .setBackgroundColor(R.color.de_york)
                                .setTitleColor(R.color.white)
                                .setAnimationIn(
                                    android.R.anim.slide_in_left,
                                    android.R.anim.slide_in_left
                                )
                                .setAnimationOut(
                                    android.R.anim.slide_out_right,
                                    android.R.anim.slide_out_right
                                )
                                .setEnableAutoDismiss(true)
                                .setSwipeToDismiss(true)
                                .setDuration(3000)
                                .show()

                            selected.invoke(questionList[position].id, it.id)
                            holder.binding.submitBtn.isEnabled = false
                            holder.binding.submitBtn.isClickable = false
                            holder.binding.submitBtn.backgroundTintList =
                                ContextCompat.getColorStateList(
                                    context!!,
                                    R.color.secundaryTextColor
                                )
                        } else {
                            var ans = ""
                            optionList.forEach{
                                if(it.answer == 1)
                                {
                                   ans = it.name
                                }
                            }

                            CookieBar.build(context as Activity)
                                .setTitle("Wrong Answer")
                                .setMessage("Correct Answer is $ans")
                                .setCookiePosition(CookieBar.BOTTOM)
                                .setBackgroundColor(R.color.burnt_sienna)
                                .setTitleColor(R.color.white)
                                .setAnimationIn(
                                    android.R.anim.slide_in_left,
                                    android.R.anim.slide_in_left
                                )
                                .setAnimationOut(
                                    android.R.anim.slide_out_right,
                                    android.R.anim.slide_out_right
                                )
                                .setEnableAutoDismiss(true)
                                .setSwipeToDismiss(true)
                                .setDuration(3000)
                                .show()


                            selected.invoke(questionList[position].id, it.id)
                            holder.binding.submitBtn.isEnabled = false
                            holder.binding.submitBtn.isClickable = false
                            holder.binding.submitBtn.backgroundTintList =
                                ContextCompat.getColorStateList(
                                    context!!,
                                    R.color.secundaryTextColor
                                )
                        }
                    }
                }
            }
        }
    }

}
