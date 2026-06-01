package com.gundogar.shoplist.ui.strings

object EnglishStrings : Strings {
    // Screen Titles
    override val screenTitleShoppingList = "My Shopping List"
    override val screenTitleAddItem = "Add Item"
    override val screenTitleEditList = "Edit List"

    // Navigation & Actions
    override val actionBack = "Back"
    override val actionSave = "Save"
    override val actionDelete = "Delete"
    override val actionUndo = "Undo"
    override val actionSearch = "Search"
    override val actionAddItem = "Add item"
    override val actionShareWhatsApp = "Share on WhatsApp"
    override val actionReadList = "Read list"

    // Form Labels
    override val labelListTitle = "List Title"
    override val labelItemName = "Item Name"
    override val labelQuantity = "Quantity"
    override val labelUnit = "Unit"

    // Placeholders
    override val placeholderSearch = "Search list..."
    override val placeholderListTitle = "e.g., Weekly Shopping"
    override val placeholderItemName = "e.g., Milk"
    override val placeholderQuantity = "e.g., 2"
    override val placeholderUnit = "e.g., pcs"

    // Messages & Instructions
    override val messageListDeleted = "List deleted"
    override val messageNoListsYet = "No lists yet"
    override val instructionAddFirstList = "Tap the + button at the bottom right to create your first list"
    override val instructionAddNewItem = "Tap the + at the top right to add new items"

    // Status & Info
    override val statusCompleted = "Completed"
    override val statusNotCompleted = "Not completed"
    override val infoItemCount = "items"
    override val infoListCount = "lists"
    override val infoSearchResults = "results found"

    // Text-to-Speech Content
    override val ttsListPrefix = "In your shopping list."
    override val ttsItemExists = "Available"

    // Content Descriptions (Accessibility)
    override val contentDescBack = "Back"
    override val contentDescSave = "Save"
    override val contentDescDelete = "Delete"
    override val contentDescSearch = "Search"
    override val contentDescAddItem = "Add item"
    override val contentDescShare = "Share on WhatsApp"
    override val contentDescReadList = "Read list"
    override val contentDescMarkComplete = "Mark item as completed"
    override val contentDescMarkIncomplete = "Mark item as not completed"

    // Error Messages
    override val errorQuantityMustBeNumeric = "Quantity must be a number"
    override val errorUnitMustBeString = "Unit must contain only letters"

    // List Title Suggestions
    override val listTitleSuggestions = listOf(
        "Weekly Shopping",
        "Daily Shopping",
        "Monthly Shopping",
        "Grocery List",
        "Party Supplies",
        "BBQ Shopping"
    )

    // Format Functions
    override fun formatItemCount(count: Int): String = "$count $infoItemCount"
    override fun formatListCount(count: Int): String = "You have $count $infoListCount"
    override fun formatSearchResults(count: Int): String = "$count $infoSearchResults"
    override fun formatCompletedCount(completed: Int, total: Int): String = "$completed/$total $infoItemCount"
}
