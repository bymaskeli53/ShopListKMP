package com.gundogar.shoplist.presentation.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gundogar.shoplist.domain.model.ShoppingItemFormState
import com.gundogar.shoplist.ui.strings.LocalStrings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    onNavigateBack: () -> Unit,
    onCreateList: (String, List<Triple<String, String, String>>) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    var listTitle by remember { mutableStateOf("") }
    val initialItem = remember { ShoppingItemFormState() }
    var shoppingItems by remember { mutableStateOf(listOf(initialItem)) }
    var itemAnimationStates by remember { mutableStateOf(mapOf(initialItem.id to true)) }
    val scope = rememberCoroutineScope()
    var showSuggestions by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    // High-contrast color scheme matching ShoppingListScreen
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
                        strings.screenTitleAddItem,
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
                    // Add new item button
                    IconButton(
                        onClick = {
                            val newItem = ShoppingItemFormState()
                            // Add to items list
                            shoppingItems = shoppingItems + newItem
                            // Start with invisible
                            itemAnimationStates = itemAnimationStates + (newItem.id to false)
                            // Then make visible after a frame to trigger animation
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


            val hasValidItems =
                shoppingItems.any { it.title.isNotBlank() && it.quantityError == null && it.unitError == null }
            if (hasValidItems) {
                FloatingActionButton(
                    onClick = {
                        val itemsToAdd = shoppingItems
                            .filter { it.title.isNotBlank() && it.quantityError == null && it.unitError == null }
                            .map { Triple(it.title, it.quantity, it.unit) }
                        onCreateList(listTitle, itemsToAdd)
                        onNavigateBack()
                    },
                    containerColor = accentColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(64.dp)
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            showSuggestions = false
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    value = listTitle,
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
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            showSuggestions = focusState.isFocused && listTitle.isEmpty()
                        },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = textSecondary,
                        cursorColor = accentColor,
                        focusedContainerColor = surfaceColor,
                        unfocusedContainerColor = surfaceColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    singleLine = true
                )

                // Suggestions dropdown
                AnimatedVisibility(
                    visible = showSuggestions,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 4 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 4 })
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = surfaceColor
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            strings.listTitleSuggestions.forEach { suggestion ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            listTitle = suggestion
                                            showSuggestions = false
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = suggestion,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = textPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = Color.DarkGray)

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
                        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                            initialOffsetY = { -it / 2 },
                            animationSpec = tween(300)
                        ),
                        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
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
                                val error =
                                    if (newQuantity.isNotBlank() && !newQuantity.matches(Regex("^[0-9]+(\\.[0-9]+)?$"))) {
                                        strings.errorQuantityMustBeNumeric
                                    } else null
                                shoppingItems = shoppingItems.toMutableList().apply {
                                    this[index] = this[index].copy(
                                        quantity = newQuantity,
                                        quantityError = error
                                    )
                                }
                            },
                            onUnitChange = { newUnit ->
                                val error =
                                    if (newUnit.isNotBlank() && !newUnit.matches(Regex("^[a-zA-Z\\s]+$"))) {
                                        strings.errorUnitMustBeString
                                    } else null
                                shoppingItems = shoppingItems.toMutableList().apply {
                                    this[index] =
                                        this[index].copy(unit = newUnit, unitError = error)
                                }
                            },
                            onDelete = {
                                if (shoppingItems.size > 1) {
                                    // Hide with animation first
                                    itemAnimationStates = itemAnimationStates + (item.id to false)
                                    // Remove from list after animation completes
                                    scope.launch {
                                        delay(300) // Match animation duration
                                        shoppingItems =
                                            shoppingItems.filterIndexed { i, _ -> i != index }
                                    }
                                }
                            },
                            canDelete = shoppingItems.size > 1,
                            strings = strings,
                            surfaceColor = surfaceColor,
                            accentColor = accentColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            focusManager = focusManager,
                            focusRequester = focusRequester
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

@Composable
fun ItemEntryCard(
    item: ShoppingItemFormState,
    index: Int,
    onTitleChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onDelete: () -> Unit,
    canDelete: Boolean,
    strings: com.gundogar.shoplist.ui.strings.Strings,
    surfaceColor: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    isCompleted: Boolean = false,
    onToggleComplete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with number and delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tap-to-toggle completion (only shown when a handler is provided)
                    if (onToggleComplete != null) {
                        IconButton(
                            onClick = onToggleComplete,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                contentDescription = if (isCompleted) strings.contentDescMarkIncomplete else strings.contentDescMarkComplete,
                                tint = if (isCompleted) accentColor else textSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        text = "#${index + 1}",
                        style = MaterialTheme.typography.labelLarge,
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (canDelete) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = strings.contentDescDelete,
                            tint = textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title input
            OutlinedTextField(
                value = item.title,
                onValueChange = onTitleChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                label = { Text(strings.labelItemName, color = textSecondary) },
                placeholder = {
                    Text(
                        strings.placeholderItemName,
                        color = textSecondary.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .focusRequester(focusRequester = focusRequester),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = accentColor,
                    unfocusedLabelColor = textSecondary,
                    cursorColor = accentColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity and Unit Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                // Quantity input
                OutlinedTextField(
                    value = item.quantity,
                    onValueChange = onQuantityChange,
                    label = { Text(strings.labelQuantity, color = textSecondary) },
                    placeholder = {
                        Text(
                            strings.placeholderQuantity,
                            color = textSecondary.copy(alpha = 0.6f)
                        )
                    },
                    isError = item.quantityError != null,
                    supportingText = item.quantityError?.let { error ->
                        { Text(error, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = textSecondary,
                        cursorColor = accentColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error,
                        errorTextColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true
                )

                // Unit input
                OutlinedTextField(
                    value = item.unit,
                    onValueChange = onUnitChange,
                    label = { Text(strings.labelUnit, color = textSecondary) },
                    placeholder = {
                        Text(
                            strings.placeholderUnit,
                            color = textSecondary.copy(alpha = 0.6f)
                        )
                    },
                    isError = item.unitError != null,
                    supportingText = item.unitError?.let { error ->
                        { Text(error, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = textSecondary,
                        cursorColor = accentColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error,
                        errorTextColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true
                )
            }
        }
    }
}
