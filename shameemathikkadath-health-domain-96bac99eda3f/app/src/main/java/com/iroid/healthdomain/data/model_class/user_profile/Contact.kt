package com.iroid.healthdomain.data.model_class.user_profile

data class Contact(
    val hashed_mobile: String,
    val hds: String,
    val id: Int,
    val is_follower: Int,
    val is_following: Int,
    val name: String,
    val points: String,
    val profile_image_url: String
)