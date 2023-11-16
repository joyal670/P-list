package com.iroid.patrickstore.model.update_profile

data class ProfileUpdateData(
    val __v: Int,
    val addressIds: List<String>,
    val deviceId: String,
    val email: String,
    val firstName: String,
    val id: String,
    val isVerified: Boolean,
    val lastName: String,
    val location: ProfileUpdateLocation,
    val mobile: String,
    val passwordHash: String,
    val productReviewIds: List<String>,
    val sellerReviewIds: List<String>,
    val tsLastLocationUpdateAt: Long
)