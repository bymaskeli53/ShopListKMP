package com.gundogar.shoplist.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingList
import com.gundogar.shoplist.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Shopping List screen
 * Handles business logic for displaying and managing shopping lists
 */
class ShoppingListViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAllShoppingLists()
    }

    private fun loadAllShoppingLists() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllShoppingLists().collect { fetchedLists ->
                _shoppingLists.value = fetchedLists
                _isLoading.value = false
            }
        }
    }

    fun toggleShoppingListCompletion(shoppingList: ShoppingList) {
        viewModelScope.launch {
            repository.toggleShoppingListCompletion(shoppingList.id, !shoppingList.isCompleted)
        }
    }

    fun toggleItemCompletion(shoppingList: ShoppingList, shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.toggleItemCompletion(shoppingList.id, shoppingItem.id, !shoppingItem.isCompleted)
        }
    }

    fun deleteShoppingList(listId: String) {
        viewModelScope.launch {
            repository.deleteShoppingList(listId)
        }
    }

    fun restoreShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            repository.restoreShoppingList(shoppingList)
        }
    }
}
