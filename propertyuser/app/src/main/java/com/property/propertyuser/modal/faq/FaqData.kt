package com.property.propertyuser.modal.faq

data class FaqData(
    val current_page: String,
    val faqs: List<Faq>,
    val total_page_count: Int
)