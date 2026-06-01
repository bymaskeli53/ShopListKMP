package com.gundogar.shoplist.data.model

import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingList

/**
 * Data layer entity representing a shopping list with its items
 * This is used internally in the data layer
 */
data class ShoppingListEntity(
    val listId: String,
    val title: String,
    val isCompleted: Boolean,
    val createdAt: Long,
    val items: List<ShoppingItemEntity>
)

/**
 * Data layer entity representing a shopping item
 */
data class ShoppingItemEntity(
    val id: String,
    val title: String,
    val quantity: String,
    val unit: String,
    val isCompleted: Boolean
)

/**
 * Mapper extension functions to convert between data and domain layers
 */
fun ShoppingListEntity.toDomain(): ShoppingList {
    return ShoppingList(
        id = listId,
        title = title,
        items = items.map { it.toDomain() },
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

fun ShoppingItemEntity.toDomain(): ShoppingItem {
    return ShoppingItem(
        id = id,
        title = title,
        quantity = quantity,
        unit = unit,
        isCompleted = isCompleted
    )
}

fun ShoppingItem.toEntity(): ShoppingItemEntity {
    return ShoppingItemEntity(
        id = id,
        title = title,
        quantity = quantity,
        unit = unit,
        isCompleted = isCompleted
    )
}

fun ShoppingList.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        listId = id,
        title = title,
        isCompleted = isCompleted,
        createdAt = createdAt,
        items = items.map { it.toEntity() }
    )
}
