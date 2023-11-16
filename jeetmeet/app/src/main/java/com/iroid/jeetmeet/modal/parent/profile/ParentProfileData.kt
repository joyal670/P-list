package com.iroid.jeetmeet.modal.parent.profile

data class ParentProfileData(
    val address: String,
    val countries: ParentProfileCountries,
    val country: Int,
    val email: String,
    val first_name: String,
    val image_url: String,
    val last_name: String,
    val nationalities: ParentProfileNationalities,
    val nationality: String,
    val phone: String,
    val phone2: String,
    val photo: String,
    val place: String,
    val state: Int,
    val states: ParentProfileStates,
    val zip: String
)