package com.iroid.healthdomain.data.model_class.index

data class PastChallenge(
    val challengeeScore: Int,
    val challengerScore: Int,
    val daysLeft: String,
    val id: Int,
    val initiated_user: Int,
    val oponentID: Int,
    val oponentname: String,
    val opponent_points: Any,
    val opponent_profile_pic: String,
    val result: String,
    val sex: String,
    val status: String
)