package com.gundogar.shoplist

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gundogar.shoplist.data.local.DatabaseDriverFactory
import com.gundogar.shoplist.data.repository.ShoppingRepositoryImpl
import com.gundogar.shoplist.domain.repository.ShoppingRepository
import com.gundogar.shoplist.presentation.add.AddItemScreen
import com.gundogar.shoplist.presentation.add.AddItemViewModel
import com.gundogar.shoplist.presentation.detail.DetailScreen
import com.gundogar.shoplist.presentation.detail.DetailViewModel
import com.gundogar.shoplist.presentation.list.ShoppingListScreen
import com.gundogar.shoplist.presentation.list.ShoppingListViewModel
import com.gundogar.shoplist.presentation.theme.ShopListTheme
import com.gundogar.shoplist.ui.strings.LocalizationProvider
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    LocalizationProvider {
        ShopListTheme {
            val navController = rememberNavController()


        // Initialize ViewModels
        val listViewModel: ShoppingListViewModel = koinViewModel()
        val addViewModel: AddItemViewModel = koinViewModel()
        val detailViewModel: DetailViewModel = koinViewModel()

        // Observe shopping lists from ViewModel
        val shoppingLists by listViewModel.shoppingLists.collectAsState()
        val isLoading by listViewModel.isLoading.collectAsState()

        NavHost(
            navController = navController,
            startDestination = "shopping_list",
        ) {
            composable("shopping_list") {
                ShoppingListScreen(
                    shoppingLists = shoppingLists,
                    isLoading = isLoading,
                    onToggleCompleted = { shoppingList -> listViewModel.toggleShoppingListCompletion(shoppingList) },
                    onToggleItemCompleted = { shoppingList, shoppingItem -> listViewModel.toggleItemCompletion(shoppingList, shoppingItem) },
                    onListClick = { shoppingList -> navController.navigate("detail/${shoppingList.id}") },
                    onNavigateToAdd = { navController.navigate("add_item") },
                    onDeleteList = { shoppingList -> listViewModel.deleteShoppingList(shoppingList.id) },
                    onRestoreList = { shoppingList -> listViewModel.restoreShoppingList(shoppingList) }
                )
            }

            composable("add_item") {
                AddItemScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCreateList = { listTitle, shoppingItems ->
                        addViewModel.createShoppingList(listTitle, shoppingItems)
                    }
                )
            }

            composable(
                route = "detail/{listId}"
            ) { backStackEntry ->
                val listId = backStackEntry.savedStateHandle.get<String>("listId")
                val selectedList = shoppingLists.find { it.id == listId }

                if (selectedList != null) {
                    DetailScreen(
                        shoppingList = selectedList,
                        onNavigateBack = { navController.popBackStack() },
                        onSave = { id, listTitle, shoppingItems ->
                            detailViewModel.updateShoppingList(id, listTitle, shoppingItems)
                        },
                        onToggleItemCompletion = { id, itemId, isCompleted ->
                            detailViewModel.toggleItemCompletion(id, itemId, isCompleted)
                        }
                    )
                }
            }
        }
        }
    }
}
