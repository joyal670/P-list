package com.mytodo.mytodo.ui.main.sidemenu.realm.model

import io.realm.RealmObject
import java.util.*

open class TaskModel : RealmObject()
{
    var id = 0
    var title: String? = null
    var startDate: String? = null
    var endDate: String? = null
    var startTime: String? = null
    var endTime: String? = null
    var description: String? = null
    var isCompleted: Boolean? = false

    var userId : String? = null
    var userEmail : String? =null
    var userDisplayName : String? = null
    var userProfilePic : String? = null
}