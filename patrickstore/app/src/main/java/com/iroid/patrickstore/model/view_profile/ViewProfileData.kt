package com.iroid.patrickstore.model.view_profile

data class ViewProfileData(
    val __v: Int,
    val addressIds: List<String>,
    val deviceId: String,
    val email: String,
    val firstName: String,
    val id: String,
    val isVerified: Boolean,
    val lastName: String,
    val location: ViewProfileLocation,
    val mobile: String,
    val passwordHash: Any,
    val productReviewIds: List<String>,
    val profileImageId: ProfileImageId,
    val sellerReviewIds: List<String>,
    val tsLastLocationUpdateAt: Long
)
