package ch.heigvd.pdg_grocerypal.data.model

data class Ingredient(
    val id: Int,
    val name: String,
    val fiber: Float,
    val protein: Float,
    val energy: Int,
    val carb: Float,
    val fat : Float
)