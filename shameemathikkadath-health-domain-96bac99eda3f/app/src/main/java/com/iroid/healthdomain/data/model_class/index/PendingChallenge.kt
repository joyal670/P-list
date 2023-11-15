package com.iroid.healthdomain.data.model_class.index

data class PendingChallenge(
    val challengeScore: Int,
    val daysLeft: String,
    val id: Int,
    val initiated_user: Int,
    val oponentID: Int,
    val oponentname: String,
    val opponent_points: Any,
    val opponent_profile_pic: String,
    val sex: String,
    val status: String
)
