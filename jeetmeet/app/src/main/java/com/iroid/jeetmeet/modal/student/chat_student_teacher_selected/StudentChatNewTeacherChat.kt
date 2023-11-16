package com.iroid.jeetmeet.modal.student.chat_student_teacher_selected

data class StudentChatNewTeacherChat(
    val chat: String,
    val created_at: String,
    val deleted: Int,
    val id: Int,
    val message: String,
    val student: Int,
    val student_read: Int,
    val teacher: Int,
    val teacher_read: Int,
    val updated_at: String
)