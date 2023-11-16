package com.iroid.jeetmeet.modal.parent.profile_state

data class ParentProfileStateResponse(
    val state_list: List<ParentProfileStateState>,
    val status: Int,
    val std_code: ParentProfileStateStdCode
)