package com.iroid.jeetmeet.modal.parent.profile_edit

data class ParentProfileEditResponse(
    val countries: List<ParentProfileEditCountry>,
    val parent: ParentProfileEditParent,
    val states: List<ParentProfileEditState>,
    val status: Int
)