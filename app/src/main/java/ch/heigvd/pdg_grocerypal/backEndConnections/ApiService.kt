package ch.heigvd.pdg_grocerypal.backEndConnections

import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.recipes.RecipeCard
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/recipes")
    fun fetchRecipes(): Call<MutableList<RecipeCard>>
    @GET("/ingredients")
    fun fetchIngredients(): Call<MutableList<Ingredient>>
    @GET("/ingredients/from_recipe/{id}")
    fun fetchIngredientsForRecipe(@Path("id") recipeId: String): Call<MutableList<Ingredient_Quantity>>
}

