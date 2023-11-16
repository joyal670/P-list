package com.iroid.jeetmeet.ui.main.parent_panel.chat

import com.google.firebase.Timestamp

data class ParentTeacherChatModelClass(
    var chat_id: String? = "",
    var date: Timestamp? = Timestamp.now(),
    var from_teacher: String? = "",
    var from_parent: String? = "",
    var message: String? = "",
)
