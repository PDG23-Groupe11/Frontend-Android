package ch.heigvd.pdg_grocerypal.data.model


enum class NutritionalValueType(val displayName: String) {
    FIBER("Fiber"),
    PROTEIN("Protein"),
    ENERGY("Energy"),
    CARBS("Carbs"),
    FAT("Fat")
}

data class NutritionalValue(
    val type: NutritionalValueType,
    val unit: String,
    var quantity: String
)

data class NutritionalValues(
    val fiber: NutritionalValue,
    val protein: NutritionalValue,
    val energy: NutritionalValue,
    val carbs: NutritionalValue,
    val fat: NutritionalValue
)