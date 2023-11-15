package com.iroid.jeetmeet.modal.other

import com.iroid.jeetmeet.R


data class QuestionModel(
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val question: Int,
    val date: String? = null,
    val lastMag: Boolean = false
)

object QuestionDataSupplier {
    val questionModeData = listOf(
        QuestionModel(
            id = 0,
            description = "Descriptive Type",
            question = R.string.sample_event_title,
            lastMag = true
        ),
        QuestionModel(
            id = 1, description = "Descriptive Type", question = R.string.forgot_your_password,
            lastMag = true
        ),
        QuestionModel(
            id = 2,
            description = "Objective Type",
            question = R.string.on_20_11_2018_will_held_a_programming_contest_on_the,
        ),
        QuestionModel(
            id = 3, description = "Descriptive Type", question = R.string.programming_contest,
            lastMag = true
        ),
        QuestionModel(
            id = 4,
            description = "Descriptive Type",
            question = R.string.sample_event_title,
            lastMag = false
        ),


        )
}


