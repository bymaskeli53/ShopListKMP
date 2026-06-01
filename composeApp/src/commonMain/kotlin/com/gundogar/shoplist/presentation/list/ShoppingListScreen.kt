package com.gundogar.shoplist.presentation.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingList
import com.gundogar.shoplist.presentation.list.components.ListRow
import com.gundogar.shoplist.ui.strings.LocalStrings
import com.gundogar.shoplist.util.tts.TextToSpeechManager
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    shoppingLists: List<ShoppingList>,
    isLoading: Boolean = false,
    onToggleCompleted: (ShoppingList) -> Unit = {},
    onToggleItemCompleted: (ShoppingList, ShoppingItem) -> Unit = { _, _ -> },
    onListClick: (ShoppingList) -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onDeleteList: (ShoppingList) -> Unit = {},
    onRestoreList: (ShoppingList) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val ttsManager: TextToSpeechManager = koinInject()

    // Search state
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Filter shopping lists based on search query
    val filteredShoppingLists = remember(shoppingLists, searchQuery) {
        if (searchQuery.isBlank()) {
            shoppingLists
        } else {
            shoppingLists.filter { shoppingList ->
                shoppingList.title.contains(searchQuery, ignoreCase = true) ||
                shoppingList.items.any { shoppingItem ->
                    shoppingItem.title.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

    // Track recently deleted shopping list for undo
    var lastDeletedShoppingList by remember { mutableStateOf<ShoppingList?>(null) }

    // Clean up TTS when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    // High-contrast color scheme
    val backgroundColor = Color(0xFF000000) // Pure black
    val surfaceColor = Color(0xFF1A1A1A) // Dark gray
    val accentColor = Color(0xFF00E676) // Bright green
    val textPrimary = Color(0xFFFFFFFF) // White text
    val textSecondary = Color(0xFFB0B0B0) // Light gray text
    val deleteColor = Color(0xFFFF5252) // Red for delete

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        strings.screenTitleShoppingList,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor,
                    titleContentColor = textPrimary
                )
            )
        },
        floatingActionButton = {
            var isPressed by remember { mutableStateOf(false) }

            val size by animateDpAsState(
                targetValue = if (isPressed) 76.dp else 64.dp
            )

            FloatingActionButton(
                onClick = {
                    isPressed = !isPressed
                    onNavigateToAdd.invoke()
                },
                containerColor = accentColor,
                contentColor = Color.Black,
                modifier = Modifier.size(size) // Minimum 48dp touch target
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = strings.contentDescAddItem,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        snackbarHost = {
            AnimatedSnackbarHost(hostState = snackbarHostState)
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text(strings.placeholderSearch, color = textSecondary.copy(alpha = 0.6f)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = strings.contentDescSearch,
                        tint = accentColor
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = strings.actionDelete,
                                tint = textSecondary
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                    focusedLeadingIconColor = accentColor,
                    unfocusedLeadingIconColor = textSecondary,
                    cursorColor = accentColor,
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Loading indicator
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = accentColor,
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else {
                // List count indicator
                if (shoppingLists.isNotEmpty()) {
                    Text(
                        text = if (searchQuery.isEmpty()) {
                            strings.formatListCount(shoppingLists.size)
                        } else {
                            strings.formatSearchResults(filteredShoppingLists.size)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = textSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                } else {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = strings.messageNoListsYet,
                            style = MaterialTheme.typography.bodyLarge,
                            color = textSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = strings.instructionAddFirstList,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textSecondary.copy(alpha = 0.7f)
                        )
                    }
                }

                // Shopping lists
                LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredShoppingLists,
                    key = { shoppingList -> shoppingList.id }
                ) { shoppingList ->
//                    AnimatedVisibility(
//                        visible = true,
//                        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = {it / 2}),
//                        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(targetOffsetY = {-it / 2})
//
//                    ) {


                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                // Store the deleted shopping list
                                lastDeletedShoppingList = shoppingList

                                // Delete the shopping list
                                onDeleteList(shoppingList)

                                // Show snackbar with undo option
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = strings.messageListDeleted,
                                        actionLabel = strings.actionUndo,
                                        duration = SnackbarDuration.Short
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        // Undo: restore the shopping list
                                        lastDeletedShoppingList?.let { deletedList ->
                                            onRestoreList(deletedList)
                                        }
                                    }
                                    lastDeletedShoppingList = null
                                }
                                true
                            } else {
                                false
                            }
                        },
                        positionalThreshold = { distance -> distance * 0.12f }

                    )

                    val dismissBackgroundColor by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            SwipeToDismissBoxValue.EndToStart -> deleteColor
                            else -> Color.Transparent
                        },
                        animationSpec = tween(300)
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(dismissBackgroundColor, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = strings.contentDescDelete,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true
                    ) {
                        ListRow(
                            shoppingList = shoppingList,
                            onClick = { onListClick(it) },
                            onToggle = { onToggleCompleted(it) },
                            onToggleItem = { list, item -> onToggleItemCompleted(list, item) },
                            onReadAloud = { selectedList ->
                                // Create a spoken text from the shopping list items
                                val spokenText = buildString {
                                    selectedList.items.forEachIndexed { index, shoppingItem ->
                                        if (shoppingItem.quantity.isNotBlank() && shoppingItem.unit.isNotBlank()) {
                                            append("${shoppingItem.quantity} ${shoppingItem.unit} ${shoppingItem.title}")
                                        } else if (shoppingItem.quantity.isNotBlank()) {
                                            append("${shoppingItem.quantity} ${shoppingItem.title}")
                                        } else if (shoppingItem.unit.isNotBlank()) {
                                            append("${shoppingItem.unit} ${shoppingItem.title}")
                                        } else {
                                            append(shoppingItem.title)
                                        }
                                        if (index < selectedList.items.size - 1) {
                                            append(", ")
                                        }
                                    }
                                }
                                ttsManager.speak(spokenText)
                            },
                            backgroundColor = surfaceColor,
                            textColor = textPrimary,
                            accentColor = accentColor
                        )
                    }
                }
           //  }

                // Bottom padding for FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
            }
        }
    }
}

// Already using SnackbarHost, but can enhance with custom animation
@Composable
fun AnimatedSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { data ->
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Snackbar(containerColor = Color(0xFF33B186),snackbarData = data)
            }
        }
    )
}