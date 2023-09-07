package ch.heigvd.pdg_grocerypal.data.model

data class In_Shopping_List(
    val id: Int,
    val unitId: Int,
    val quantity: Int,
    val buy: Boolean
)
