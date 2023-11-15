package com.proteinium.proteiniumdietapp.pojo.dislike_tags

data class TagsData(
    val dislike_duration: Int,
    val tags: List<Tag>,
    val plan_active_status:Boolean
)