package com.iroid.jeetmeet.modal.student.chat_admin


data class StudentChatAdminData(
    val admin: Int,
    val admin_read: Int,
    val admins: StudentChatAdminAdmins,
    val chat: String,
    val created_at: String,
    val deleted: Int,
    val id: Int,
    val message: String,
    val student: Int,
    val student_read: Int,
    val updated_at: String
)