package com.iroid.healthdomain.data.model_class.index

data class ActiveChallenge(
    val challengeeScore: Any,
    val challengerScore: Int,
    val daysLeft: String,
    val id: Int,
    val initiated_user: Int,
    val oponentID: Int,
    val oponentname: String,
    val opponent_points: String,
    val opponent_profile_pic: String,
    val result: String,
    val sex: String,
    val status: String
)