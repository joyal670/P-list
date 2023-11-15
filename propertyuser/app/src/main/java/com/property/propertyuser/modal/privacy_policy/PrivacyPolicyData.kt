package com.property.propertyuser.modal.privacy_policy

data class PrivacyPolicyData(
    val current_page: String,
    val privacy_policies: List<PrivacyPolicy>,
    val total_page_count: Int
)