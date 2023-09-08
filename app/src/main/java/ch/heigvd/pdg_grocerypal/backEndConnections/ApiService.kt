package ch.heigvd.pdg_grocerypal.backEndConnections

import ch.heigvd.pdg_grocerypal.data.model.Credentials
import ch.heigvd.pdg_grocerypal.data.model.In_Shopping_List
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.data.model.TokenResponse
import ch.heigvd.pdg_grocerypal.data.model.UserData
import ch.heigvd.pdg_grocerypal.data.model.UserInfos
import ch.heigvd.pdg_grocerypal.recipes.RecipeCard
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/recipes")
    fun fetchRecipes(): Call<MutableList<RecipeCard>>
    @GET("/ingredients")
    fun fetchIngredients(): Call<MutableList<Ingredient>>
    @GET("/list")
    fun fetchInSchoppingList(@Header("Authorization") authorization: String): Call<MutableList<In_Shopping_List>>

    @POST("/list")
    fun postInSchoppingList(@Header("Authorization") authorization: String, @Body shoppingList: List<In_Shopping_List>): Call<Void>

    @GET("/ingredients/from_recipe/{id}")
    fun fetchIngredientsForRecipe(@Path("id") recipeId: String): Call<MutableList<Ingredient_Quantity>>

    @POST("/account/createAccount")
    fun signUp(@Body userData: UserData): Call<Void>

    @POST("/account/login")
    fun login(@Body credentials: Credentials): Call<TokenResponse>

    @GET("/user")
    fun fetchUserInfos(@Header("Authorization") authorization: String): Call<UserInfos>

    @POST("/user")
    fun postUserInfos(@Header("Authorization") authorization: String, @Body userInfos: UserInfos): Call<Void>

    @GET("/recipe/personnal")
    fun fetchUserRecipes(@Header("Authorization") authorization: String): Call<MutableList<RecipeCard>>

    @POST("/recipe/personnal")
    fun postUserRecipes(@Header("Authorization") authorization: String, @Body recipe: RecipeCard): Call<Void>

}

