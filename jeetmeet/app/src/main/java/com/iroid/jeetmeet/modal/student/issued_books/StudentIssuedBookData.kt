package com.iroid.jeetmeet.modal.student.issued_books

data class StudentIssuedBookData(
    val author: String,
    val category: String,
    val id: Int,
    val name: String,
    val pdf: String,
    val price: Int,
    val rack: Int,
    val request_id: Any,
    val requested: Boolean,
    val status: String,
    val subject: String
)