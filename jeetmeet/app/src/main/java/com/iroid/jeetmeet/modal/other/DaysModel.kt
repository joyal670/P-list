package com.iroid.jeetmeet.modal.other

data class DaysModel(
    val day: String,
    val id: Int
)

object DayModelObject {
    val daysModel = listOf<DaysModel>(
        DaysModel(id = 1, day = "Monday"),
        DaysModel(id = 2, day = "Tuesday"),
        DaysModel(id = 3, day = "Wednesday"),
        DaysModel(id = 4, day = "Thursday"),
        DaysModel(id = 5, day = "Friday"),
        DaysModel(id = 6, day = "Saturday"),
        DaysModel(id = 7, day = "Sunday"),
    )
}

