package com.gundogar.shoplist.ui.strings

sealed interface Strings {
    // Screen Titles
    val screenTitleShoppingList: String
    val screenTitleAddItem: String
    val screenTitleEditList: String

    // Navigation & Actions
    val actionBack: String
    val actionSave: String
    val actionDelete: String
    val actionUndo: String
    val actionSearch: String
    val actionAddItem: String
    val actionShareWhatsApp: String
    val actionReadList: String

    // Form Labels
    val labelListTitle: String
    val labelItemName: String
    val labelQuantity: String
    val labelUnit: String

    // Placeholders
    val placeholderSearch: String
    val placeholderListTitle: String
    val placeholderItemName: String
    val placeholderQuantity: String
    val placeholderUnit: String

    // Messages & Instructions
    val messageListDeleted: String
    val messageNoListsYet: String
    val instructionAddFirstList: String
    val instructionAddNewItem: String

    // Status & Info
    val statusCompleted: String
    val statusNotCompleted: String
    val infoItemCount: String // Format: "{count} ürün"
    val infoListCount: String // Format: "{count} adet listeniz var"
    val infoSearchResults: String // Format: "{count} sonuç bulundu"

    // Text-to-Speech Content
    val ttsListPrefix: String // "Alışveriş listenizde."
    val ttsItemExists: String // "Bulunmaktadır"

    // Content Descriptions (Accessibility)
    val contentDescBack: String
    val contentDescSave: String
    val contentDescDelete: String
    val contentDescSearch: String
    val contentDescAddItem: String
    val contentDescShare: String
    val contentDescReadList: String
    val contentDescMarkComplete: String
    val contentDescMarkIncomplete: String

    // Error Messages
    val errorQuantityMustBeNumeric: String
    val errorUnitMustBeString: String

    // List Title Suggestions
    val listTitleSuggestions: List<String>

    // Format Functions
    fun formatItemCount(count: Int): String
    fun formatListCount(count: Int): String
    fun formatSearchResults(count: Int): String
    fun formatCompletedCount(completed: Int, total: Int): String // Format: "3/5 items"
}
