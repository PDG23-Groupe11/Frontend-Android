package ch.heigvd.pdg_grocerypal.backEndConnections


import android.util.Log
import android.widget.Toast
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.recipes.RecipeCard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConnectionRecipeUtils {

    private val BASE_URL = "http://10.0.2.2:8080"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    fun fetchRecipes(
        recipeList: MutableList<RecipeCard>,
        minToAdd: Int,
        onSuccess: (MutableList<RecipeCard>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Fetch recipes
        val recipesCall = apiService.fetchRecipes()
        recipesCall.enqueue(object : Callback<MutableList<RecipeCard>> {
            override fun onResponse(call: Call<MutableList<RecipeCard>>, response: Response<MutableList<RecipeCard>>) {
                if (response.isSuccessful) {
                    val recipes = response.body() ?: mutableListOf()

                    val recipesToAdd = minOf(minToAdd - recipeList.size, recipes.size)
                    for (i in 0 until recipesToAdd) {
                        recipeList.add(recipes[i])
                    }

                    onSuccess(recipeList)
                } else {
                    onError("Failed to fetch recipes: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MutableList<RecipeCard>>, t: Throwable) {
                onError("Network or other error occurred: ${t.message}")
            }
        })
    }

    fun fetchIngredientsForRecipe(
        recipeId: String,
        onSuccess: (MutableList<Ingredient_Quantity>) -> Unit,
        onError: (String) -> Unit
    ) {
        val ingredientsCall = apiService.fetchIngredientsForRecipe(recipeId)
        ingredientsCall.enqueue(object : Callback<MutableList<Ingredient_Quantity>> {
            override fun onResponse(call: Call<MutableList<Ingredient_Quantity>>, response: Response<MutableList<Ingredient_Quantity>>) {
                if (response.isSuccessful) {
                    val ingredients = response.body() ?: mutableListOf()
                    onSuccess(ingredients)
                } else {
                    onError("Failed to fetch ingredients: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MutableList<Ingredient_Quantity>>, t: Throwable) {
                onError("Network or other error occurred: ${t.message}")
            }
        })
    }

    fun showError(errorMessage: String) {
        Log.e("Error", errorMessage)

    }
}