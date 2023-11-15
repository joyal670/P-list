package com.iroid.jeetmeet.modal.student.chat_student_teacher_selected

data class StudentChatNewTeacherResponse(
    val chat: StudentChatNewTeacherChat,
    val message: String,
    val status: Int,
    val teacher: StudentChatNewTeacherTeacher
)