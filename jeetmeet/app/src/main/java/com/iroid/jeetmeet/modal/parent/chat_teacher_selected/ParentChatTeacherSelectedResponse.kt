package com.iroid.jeetmeet.modal.parent.chat_teacher_selected

data class ParentChatTeacherSelectedResponse(
    val chat: ParentChatTeacherSelectedChat,
    val message: String,
    val status: Int,
    val teacher: ParentChatTeacherSelectedTeacher
)