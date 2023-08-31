package ch.heigvd.pdg_grocerypal.data.model

data class GroceryItem(
    val name: String,
    val unit: String,
    var quantity: String,
    var isPurchased: Boolean = false
)