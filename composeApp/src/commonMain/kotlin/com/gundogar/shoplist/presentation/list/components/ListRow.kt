package com.gundogar.shoplist.presentation.list.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingList
import com.gundogar.shoplist.ui.strings.LocalStrings


@Composable
fun ListRow(
    shoppingList: ShoppingList,
    onClick: (ShoppingList) -> Unit,
    onToggle: (ShoppingList) -> Unit,
    onToggleItem: (ShoppingList, ShoppingItem) -> Unit = { _, _ -> },
    onReadAloud: (ShoppingList) -> Unit = {},
    backgroundColor: Color,
    textColor: Color,
    accentColor: Color
) {
    val strings = LocalStrings.current
    var isSpeaking by remember { mutableStateOf(false) }

    val iconScale by animateFloatAsState(
        targetValue = if (isSpeaking) 1.4f else 1f,
        animationSpec = repeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Restart,
            iterations = 1
        ),
        finishedListener = {
            isSpeaking = false
        }
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp) // Minimum touch target
            .clickable { onClick(shoppingList) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // List content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .alpha(if (shoppingList.isCompleted) 0.5f else 1f)
            ) {
                // Show shopping list title if available
                if (shoppingList.title.isNotBlank()) {
                    Text(
                        text = shoppingList.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textDecoration = if (shoppingList.isCompleted) TextDecoration.LineThrough else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Show all shopping items vertically
                shoppingList.items.forEachIndexed { index, shoppingItem ->
                    // An item reads as done when the whole list is completed or
                    // the item itself has been individually marked completed.
                    val itemCompleted = shoppingList.isCompleted || shoppingItem.isCompleted
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Per-item checkbox: toggle a single item done without
                        // opening the detail screen. Disabled when the whole list
                        // is already completed (the list-level toggle governs that).
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clickable(enabled = !shoppingList.isCompleted) {
                                    onToggleItem(shoppingList, shoppingItem)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (itemCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                contentDescription = if (shoppingItem.isCompleted) strings.contentDescMarkIncomplete else strings.contentDescMarkComplete,
                                tint = if (itemCompleted) accentColor else textColor.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = buildString {
                                if (shoppingItem.quantity.isNotBlank() && shoppingItem.unit.isNotBlank()) {
                                    append("${shoppingItem.quantity} ${shoppingItem.unit} ")
                                } else if (shoppingItem.quantity.isNotBlank()) {
                                    append("${shoppingItem.quantity} ")
                                } else if (shoppingItem.unit.isNotBlank()) {
                                    append("${shoppingItem.unit} ")
                                }
                                append(shoppingItem.title)
                            },
                            modifier = Modifier.alpha(if (itemCompleted) 0.5f else 1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor.copy(alpha = 0.9f),
                            textDecoration = if (itemCompleted) TextDecoration.LineThrough else null
                        )
                    }

                    // Add spacing between items (except after the last one)
                    if (index < shoppingList.items.size - 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = strings.formatCompletedCount(
                        completed = shoppingList.items.count { it.isCompleted },
                        total = shoppingList.items.size
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.6f)
                )
            }

            // Action buttons row
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TTS button
                IconButton(
                    onClick = {
                        onReadAloud(shoppingList)
                        isSpeaking = !isSpeaking
                              },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = strings.contentDescReadList,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp).scale(iconScale)
                    )
                }

                // Checkbox icon with minimum touch target
                Box(
                    modifier = Modifier
                        .size(48.dp) // Minimum 48dp touch target
                        .clickable(onClick = { onToggle(shoppingList) }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (shoppingList.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = if (shoppingList.isCompleted) strings.statusCompleted else strings.statusNotCompleted,
                        tint = if (shoppingList.isCompleted) accentColor else textColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}