package ch.heigvd.pdg_grocerypal.backEndConnections

import ch.heigvd.pdg_grocerypal.data.model.Credentials
import ch.heigvd.pdg_grocerypal.data.model.In_Shopping_List
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.data.model.TokenResponse
import ch.heigvd.pdg_grocerypal.data.model.UserData
import ch.heigvd.pdg_grocerypal.recipes.RecipeCard
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ApiService {
    @GET("/recipes")
    fun fetchRecipes(): Call<MutableList<RecipeCard>>
    @GET("/ingredients")
    fun fetchIngredients(): Call<MutableList<Ingredient>>
    @GET("/list")
    fun fetchInSchoppingList(@Header("Authorization") authorization: String): Call<MutableList<In_Shopping_List>>

    @GET("/ingredients/from_recipe/{id}")
    fun fetchIngredientsForRecipe(@Path("id") recipeId: String): Call<MutableList<Ingredient_Quantity>>

    @POST("/account/createAccount")
    fun signUp(@Body userData: UserData): Call<Void>

    @POST("/account/login")
    fun login(@Body credentials: Credentials): Call<TokenResponse>

    @GET("/static/recipeImages/{recipeId}")
    @Streaming // Add this annotation to indicate that the response should be streamed
    fun fetchRecipeImage(@Path("recipeId") recipeId: String): Call<ResponseBody>
}

