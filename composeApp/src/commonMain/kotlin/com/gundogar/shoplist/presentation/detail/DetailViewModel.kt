package com.gundogar.shoplist.presentation.detail

import androidx.lifecycle.ViewModel
import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.repository.ShoppingRepository

/**
 * ViewModel for the Detail screen
 * Handles business logic for editing shopping lists
 */
class DetailViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    suspend fun updateShoppingList(listId: String, title: String, shoppingItems: List<ShoppingItem>) {
        repository.updateShoppingList(listId, title, shoppingItems)
    }

    suspend fun toggleItemCompletion(listId: String, itemId: String, isCompleted: Boolean) {
        repository.toggleItemCompletion(listId, itemId, isCompleted)
    }
}
