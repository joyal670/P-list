package com.iroid.jeetmeet.modal.student.request_book

data class StudentRequestBookData(
    val author: String,
    val category: String,
    val id: Int,
    val name: String,
    val pdf: String,
    val price: Int,
    val rack: Int,
    val request_id: Int,
    val requested: Boolean,
    val status: String,
    val subject: String
)