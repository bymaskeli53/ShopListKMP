package com.gundogar.shoplist.domain.model

import com.benasher44.uuid.uuid4

/**
 * Form state for creating/editing shopping items
 * Represents transient data during item input/editing before saving
 * Used in AddItemScreen and DetailScreen
 */
data class ShoppingItemFormState(
    val id: String = uuid4().toString(),
    val title: String = "",
    val quantity: String = "",
    val unit: String = "",
    val isCompleted: Boolean = false,
    val quantityError: String? = null,
    val unitError: String? = null
)
