package com.ncomfortsagent.ui.main.chat.model

import com.google.firebase.Timestamp

data class ChatModelClass(
    var msg: String,
    var time:String,
    var from_id: String,
    var to_id: String,
    var type: String,
    var image:String
)