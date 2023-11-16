package com.property.propertyagent.modal.object_Model

data class Furnished(
    val type_id : Int ,
    val type_name : String
)

object furnishedSupplier {
    val furnishedItems = listOf<Furnished>(
        Furnished(0 , "Not Furnished") ,
        Furnished(1 , "Semi Furnished") ,
        Furnished(2 , "Fully Furnished")
    )
}