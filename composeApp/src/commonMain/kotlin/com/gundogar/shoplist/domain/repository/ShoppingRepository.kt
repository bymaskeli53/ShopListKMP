package com.gundogar.shoplist.domain.repository

import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingList
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for shopping list operations
 * This follows the Dependency Inversion Principle - domain layer defines the contract
 */
interface ShoppingRepository {

    /**
     * Get all shopping lists with their items as a Flow
     */
    fun getAllShoppingLists(): Flow<List<ShoppingList>>

    /**
     * Get a single shopping list by ID
     */
    suspend fun getShoppingListById(listId: String): ShoppingList?

    /**
     * Create a new shopping list with items
     */
    suspend fun createShoppingList(
        listId: String,
        title: String,
        shoppingItems: List<Triple<String, String, String>> // title, quantity, unit triples
    )

    /**
     * Update an existing shopping list (title and items)
     */
    suspend fun updateShoppingList(
        listId: String,
        title: String,
        shoppingItems: List<ShoppingItem>
    )

    /**
     * Toggle the completed status of a shopping list
     */
    suspend fun toggleShoppingListCompletion(listId: String, isCompleted: Boolean)

    /**
     * Toggle the completed status of a single item within a list
     */
    suspend fun toggleItemCompletion(listId: String, itemId: String, isCompleted: Boolean)

    /**
     * Delete a shopping list
     */
    suspend fun deleteShoppingList(listId: String)

    /**
     * Restore a deleted shopping list (for undo functionality)
     */
    suspend fun restoreShoppingList(shoppingList: ShoppingList)
}
