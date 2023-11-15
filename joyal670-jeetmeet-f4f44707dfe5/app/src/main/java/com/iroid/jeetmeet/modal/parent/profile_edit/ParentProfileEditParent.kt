package com.iroid.jeetmeet.modal.parent.profile_edit

data class ParentProfileEditParent(
    val address: String,
    val countries: ParentProfileEditCountries,
    val country: Int,
    val email: String,
    val id: Int,
    val nationalities: ParentProfileEditNationalities,
    val nationality: String,
    val phone: Int,
    val place: String,
    val state: Int,
    val states: ParentProfileEditStates,
    val std_code: Int,
    val zip: String
)