package com.iroid.healthdomain.data.model_class.user_challenge

import java.util.*

data class DataX(
    val challenge_end_date: Any,
    val challenge_score: Int,
    val challenge_start_date: String,
    val challenge_win_user: Int,
    val challenged_user: Int,
    val challenger_end_date: String,
    val challenger_score: Int,
    val challenger_start_date: String,
    val created_at: String,
    val current_state: String,
    val deleted: Any,
    val id: Int,
    val initiated_user: Int,
    val notes: Any,
    val updated_at: String,
    val user_from: UserFrom,
    val user_to: UserTo
)