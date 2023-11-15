package com.iroid.jeetmeet.ui.main.student_panel.chat.model

import com.google.firebase.Timestamp

data class AdminChatModelClass(
    var chat_id: String? = "",
    var date: Timestamp? = Timestamp.now(),
    var from_admin: String? = "",
    var from_student: String? = "",
    var message: String? = "",
)
