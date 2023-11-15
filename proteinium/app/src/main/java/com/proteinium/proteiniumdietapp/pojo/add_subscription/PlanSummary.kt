package com.proteinium.proteiniumdietapp.pojo.add_subscription

data class PlanSummary(
    val carbs: Carbs,
    val duration: Duration,
    val non_stop: NonStop,
    val plan: Plan,
    val protein: Protein,
    val total: Total,
    val extras: List<Extra>,
    val current_subscription_id : Int
)