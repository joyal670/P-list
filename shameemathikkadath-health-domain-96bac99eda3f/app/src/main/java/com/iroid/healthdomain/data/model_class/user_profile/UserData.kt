package com.iroid.healthdomain.data.model_class.user_profile

data class UserData(
    val age: Int ?= null,
    val blood_group: String ?= null,
    val created_at: String ?= null,
    val gender: String ?= null,
    val height: String ?= null,
    val id: Int ?= null,
    val points:String ?= null,
    val mobile: String ?= null,
    val name: String ?= null,
    val otp_validated: Int ?= null,
    val profile_image_url: String ?= null,
    val updated_at: String ?= null,
    val weight: String ?= null,
    val contacts: List<Contact>
)