package com.property.propertyuser.modal.legal_informations

data class LegalInformationData(
    val current_page: String,
    val legal_informations: List<LegalInformation>,
    val total_page_count: Int
)