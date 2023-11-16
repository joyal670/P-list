package com.iroid.healthdomain.data.model_class

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountModel(
        val name : String ?= null,
        val age : String ?= null,
        val gender : String ?= null,
        val blood_group : String ?= null,
        val weight : String ?= null,
        val height : String ?= null,
) : Parcelable
