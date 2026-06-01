package com.gundogar.shoplist.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingItemFormState
import com.gundogar.shoplist.domain.model.ShoppingList
import com.gundogar.shoplist.presentation.add.ItemEntryCard
import com.gundogar.shoplist.ui.strings.LocalStrings
import com.gundogar.shoplist.util.share.ShareManager
import com.gundogar.shoplist.util.tts.TextToSpeechManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    shoppingList: ShoppingList,
    onNavigateBack: () -> Unit,
    onSave: suspend (String, String, List<ShoppingItem>) -> Unit,
    onToggleItemCompletion: suspend (String, String, Boolean) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current

    // Use remember with key to reset when shopping list changes
    var shoppingItems by remember(shoppingList.id, shoppingList.items) {
        mutableStateOf(shoppingList.items.map { item ->
            ShoppingItemFormState(
                id = item.id,
                title = item.title,
                quantity = item.quantity,
                unit = item.unit,
                isCompleted = item.isCompleted
            )
        })
    }

    val initialItem = remember { ShoppingItemFormState() }

    var listTitle by remember(shoppingList.id) { mutableStateOf(shoppingList.title) }

    var itemAnimationStates by remember { mutableStateOf(mapOf(initialItem.id to true)) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current



    val scope = rememberCoroutineScope()
    val ttsManager: TextToSpeechManager = koinInject()
    val shareManager: ShareManager = koinInject()

    // Clean up TTS when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    // High-contrast color scheme matching AddItemScreen
    val backgroundColor = Color(0xFF000000)
    val surfaceColor = Color(0xFF1A1A1A)
    val accentColor = Color(0xFF00E676)
    val textPrimary = Color(0xFFFFFFFF)
    val textSecondary = Color(0xFFB0B0B0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        strings.screenTitleEditList,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = strings.contentDescBack,
                            tint = textPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    // Share button
                    IconButton(
                        onClick = {
                            val shareText = buildString {
                                if (listTitle.isNotBlank()) {
                                    append("*$listTitle*\n\n")
                                }
                                shoppingItems.filter { it.title.isNotBlank() }
                                    .forEachIndexed { index, item ->
                                        append("${index + 1}. ")
                                        if (item.quantity.isNotBlank() && item.unit.isNotBlank()) {
                                            append("${item.quantity} ${item.unit} ${item.title}")
                                        } else if (item.quantity.isNotBlank()) {
                                            append("${item.quantity} ${item.title}")
                                        } else if (item.unit.isNotBlank()) {
                                            append("${item.unit} ${item.title}")
                                        } else {
                                            append(item.title)
                                        }
                                        append("\n")
                                    }
                            }
                            shareManager.shareToWhatsApp(shareText)
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = strings.contentDescShare,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // TTS button
                    IconButton(
                        onClick = {
                            val spokenText = buildString {
                                append("${strings.ttsListPrefix} ")
                                shoppingItems.filter { it.title.isNotBlank() }
                                    .forEachIndexed { index, item ->
                                        if (item.quantity.isNotBlank() && item.unit.isNotBlank()) {
                                            append("${item.quantity} ${item.unit} ${item.title}")
                                        } else if (item.quantity.isNotBlank()) {
                                            append("${item.quantity} ${item.title}")
                                        } else if (item.unit.isNotBlank()) {
                                            append("${item.unit} ${item.title}")
                                        } else {
                                            append(item.title)
                                        }
                                        if (index < shoppingItems.size - 1) {
                                            append(", ")
                                        }
                                    }
                                append(" ${strings.ttsItemExists}")
                            }
                            ttsManager.speak(spokenText)
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = strings.contentDescReadList,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Add new item button
                    IconButton(
                        onClick = {
                            val newItem = ShoppingItemFormState()
                            shoppingItems = shoppingItems + newItem

                            itemAnimationStates = itemAnimationStates + (newItem.id to false)

                            scope.launch {
                                delay(50)
                                itemAnimationStates = itemAnimationStates + (newItem.id to true)
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = strings.contentDescAddItem,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
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
            val hasValidItems = shoppingItems.any { it.title.isNotBlank() && it.quantityError == null && it.unitError == null }
            if (hasValidItems) {
                FloatingActionButton(
                    onClick = {
                        isPressed = !isPressed
                        scope.launch {
                            val updatedShoppingItems = shoppingItems
                                .filter { it.title.isNotBlank() && it.quantityError == null && it.unitError == null }
                                .map { item ->
                                    ShoppingItem(
                                        id = item.id,
                                        title = item.title,
                                        quantity = item.quantity,
                                        unit = item.unit,
                                        isCompleted = item.isCompleted
                                    )
                                }
                            // Wait for the save to complete
                            onSave(shoppingList.id, listTitle, updatedShoppingItems)
                            // Navigate back after saving completes
                            onNavigateBack()
                        }
                    },
                    containerColor = accentColor,
                    contentColor = Color.Black,
                    modifier = Modifier.size(size)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = strings.contentDescSave,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // Header with instruction
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor)
                    .padding(20.dp)
            ) {
                Text(
                    text = strings.formatItemCount(shoppingItems.size),
                    style = MaterialTheme.typography.labelLarge,
                    color = textPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = strings.instructionAddNewItem,
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondary
                )
            }

            // List Title Input
            OutlinedTextField(
                value = listTitle,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                onValueChange = { listTitle = it },
                label = { Text(strings.labelListTitle, color = textSecondary) },
                placeholder = {
                    Text(
                        strings.placeholderListTitle,
                        color = textSecondary.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .focusRequester(focusRequester),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = accentColor,
                    unfocusedLabelColor = textSecondary,
                    cursorColor = accentColor,
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor,
                    errorTextColor = MaterialTheme.colorScheme.error,

                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))


            // Item list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(
                    items = shoppingItems,
                    key = { _, item -> item.id }
                ) { index, item ->
                    AnimatedVisibility(
                        visible = itemAnimationStates[item.id] ?: true,
                        enter = fadeIn(
                            animationSpec = tween(300)) + slideInVertically(
                            initialOffsetY = { -it / 2 },
                            animationSpec = tween(300)
                            ),
                        exit = fadeOut(
                            animationSpec = tween(300)) + slideOutVertically(
                                targetOffsetY = { it / 2 },
                            animationSpec = tween(300)
                        )
                    ) {
                        ItemEntryCard(
                            item = item,
                            index = index,
                            onTitleChange = { newTitle ->
                                shoppingItems = shoppingItems.toMutableList().apply {
                                    this[index] = this[index].copy(title = newTitle)
                                }
                            },
                            onQuantityChange = { newQuantity ->
                                val error = if (newQuantity.isNotBlank() && !newQuantity.matches(Regex("^[0-9]+(\\.[0-9]+)?$"))) {
                                    strings.errorQuantityMustBeNumeric
                                } else null
                                shoppingItems = shoppingItems.toMutableList().apply {
                                    this[index] = this[index].copy(quantity = newQuantity, quantityError = error)
                                }
                            },
                            onUnitChange = { newUnit ->
                                val error = if (newUnit.isNotBlank() && !newUnit.matches(Regex("^[a-zA-Z\\s]+$"))) {
                                    strings.errorUnitMustBeString
                                } else null
                                shoppingItems = shoppingItems.toMutableList().apply {
                                    this[index] = this[index].copy(unit = newUnit, unitError = error)
                                }
                            },
                            onDelete = {
                                if (shoppingItems.size > 1) {
                                    itemAnimationStates = itemAnimationStates + (item.id to false)

                                    scope.launch {
                                        delay(300)
                                        shoppingItems = shoppingItems.filterIndexed { i, _ -> i != index }

                                    }
                                }
                            },
                            canDelete = shoppingItems.size > 1,
                            strings = strings,
                            surfaceColor = surfaceColor,
                            accentColor = accentColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            focusRequester = focusRequester,
                            focusManager = focusManager,
                            isCompleted = item.isCompleted,
                            onToggleComplete = {
                                val newValue = !item.isCompleted
                                // Optimistically update local state...
                                shoppingItems = shoppingItems.toMutableList().apply {
                                    this[index] = this[index].copy(isCompleted = newValue)
                                }
                                // ...and persist immediately so it survives navigating back
                                scope.launch {
                                    onToggleItemCompletion(shoppingList.id, item.id, newValue)
                                }
                            }
                        )
                    }

                }

                // Bottom padding for FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
