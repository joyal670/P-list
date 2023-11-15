package com.iroid.jeetmeet.modal.parent.chat_teacher_selected

data class ParentChatTeacherSelectedChat(
    val chat: String,
    val created_at: String,
    val deleted: Int,
    val id: Int,
    val message: String,
    val parent: Int,
    val parent_read: Int,
    val teacher: Int,
    val teacher_read: Int,
    val updated_at: String
)