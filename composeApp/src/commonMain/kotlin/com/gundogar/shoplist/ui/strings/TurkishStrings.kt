package com.gundogar.shoplist.ui.strings

object TurkishStrings : Strings {
    // Screen Titles
    override val screenTitleShoppingList = "Alışveriş Listem"
    override val screenTitleAddItem = "Ürün Ekle"
    override val screenTitleEditList = "Listeyi Düzenle"

    // Navigation & Actions
    override val actionBack = "Geri"
    override val actionSave = "Kaydet"
    override val actionDelete = "Sil"
    override val actionUndo = "Geri Al"
    override val actionSearch = "Ara"
    override val actionAddItem = "Ürün ekle"
    override val actionShareWhatsApp = "WhatsApp'ta paylaş"
    override val actionReadList = "Listeyi oku"

    // Form Labels
    override val labelListTitle = "Liste Başlığı"
    override val labelItemName = "Ürün Adı"
    override val labelQuantity = "Miktar"
    override val labelUnit = "Birim"

    // Placeholders
    override val placeholderSearch = "Liste ara..."
    override val placeholderListTitle = "Örn: Haftalık Alışveriş"
    override val placeholderItemName = "Örn: Süt"
    override val placeholderQuantity = "Örn: 2"
    override val placeholderUnit = "Örn: adet"

    // Messages & Instructions
    override val messageListDeleted = "Liste silindi"
    override val messageNoListsYet = "Henüz liste eklenmedi"
    override val instructionAddFirstList = "Sağ alttaki + butonuna tıklayarak ilk listenizi oluşturun"
    override val instructionAddNewItem = "Sağ üstteki + ile yeni ürün ekleyin"

    // Status & Info
    override val statusCompleted = "Tamamlandı"
    override val statusNotCompleted = "Tamamlanmadı"
    override val infoItemCount = "ürün"
    override val infoListCount = "adet listeniz var"
    override val infoSearchResults = "sonuç bulundu"

    // Text-to-Speech Content
    override val ttsListPrefix = "Alışveriş listenizde."
    override val ttsItemExists = "Bulunmaktadır"

    // Content Descriptions (Accessibility)
    override val contentDescBack = "Geri"
    override val contentDescSave = "Kaydet"
    override val contentDescDelete = "Sil"
    override val contentDescSearch = "Ara"
    override val contentDescAddItem = "Ürün ekle"
    override val contentDescShare = "WhatsApp'ta paylaş"
    override val contentDescReadList = "Listeyi oku"
    override val contentDescMarkComplete = "Ürünü tamamlandı olarak işaretle"
    override val contentDescMarkIncomplete = "Ürünü tamamlanmadı olarak işaretle"

    // Error Messages
    override val errorQuantityMustBeNumeric = "Miktar sayı olmalıdır"
    override val errorUnitMustBeString = "Birim sadece harf içermelidir"

    // List Title Suggestions
    override val listTitleSuggestions = listOf(
        "Haftalık Alışveriş",
        "Günlük Alışveriş",
        "Aylık Alışveriş",
        "Market Listesi",
        "Parti Malzemeleri",
        "Mangal Alışverişi"
    )

    // Format Functions
    override fun formatItemCount(count: Int): String = "$count $infoItemCount"
    override fun formatListCount(count: Int): String = "$count $infoListCount"
    override fun formatSearchResults(count: Int): String = "$count $infoSearchResults"
    override fun formatCompletedCount(completed: Int, total: Int): String = "$completed/$total $infoItemCount"
}
