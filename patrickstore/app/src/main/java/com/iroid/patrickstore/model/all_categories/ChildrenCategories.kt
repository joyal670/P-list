package com.iroid.patrickstore.model.all_categories

data class ChildrenCategories(
    val _id: String,
    val name: String,
    val parentId: Any,
    val uniqueName: String,
    var isChecked:Boolean,
    var isPerishable:Boolean,
    var expanded: Boolean = false
)