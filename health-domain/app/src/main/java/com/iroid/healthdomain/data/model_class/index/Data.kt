package com.iroid.healthdomain.data.model_class.index

data class Data(
    val HDS: Any,
    val challenges: List<Challenge>,
    val pending_challenges: List<PendingChallenge>,
    val active_challenges: List<ActiveChallenge>,
    val past_challenges: List<PastChallenge>,
    val contacts: List<Contact>,
    val points: Int,
    val stars: Int,
    val targets: List<Target>,
    val tip: String,
    val userID: Int,
    val version: String
)