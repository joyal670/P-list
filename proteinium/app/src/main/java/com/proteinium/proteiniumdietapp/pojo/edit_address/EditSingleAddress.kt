package com.proteinium.proteiniumdietapp.pojo.edit_address

import com.proteinium.proteiniumdietapp.pojo.areas.Area
import com.proteinium.proteiniumdietapp.pojo.governorate.Governorates

data class EditSingleAddress(
    val address: Address,
    val areas: List<Area>,
    val governorates: List<Governorates>
)