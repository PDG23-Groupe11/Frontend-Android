package ch.heigvd.pdg_grocerypal.backEndConnections
import android.util.Log
import ch.heigvd.pdg_grocerypal.data.model.Credentials
import ch.heigvd.pdg_grocerypal.data.model.In_Shopping_List
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.data.model.TokenResponse
import ch.heigvd.pdg_grocerypal.data.model.UserData
import ch.heigvd.pdg_grocerypal.recipes.RecipeCard
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


data class ApiResponse(
    val message: String // You can customize this according to your server response
)

object ConnectionRecipeUtils {

    private val BASE_URL = "http://10.0.2.2:8080"


    private val retrofitJson = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiServiceJson = retrofitJson.create(ApiService::class.java)

    private val retrofitImage = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create()) // Use ScalarsConverterFactory for non-JSON responses
        .build()

    // Create a new ApiService interface for image requests
    private val apiServiceImage = retrofitImage.create(ApiService::class.java)

    fun fetchRecipes(
        recipeList: MutableList<RecipeCard>,
        minToAdd: Int,
        onSuccess: (MutableList<RecipeCard>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Fetch recipes
        val recipesCall = apiServiceJson.fetchRecipes()
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

    fun createAccount(
        userData: UserData,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val signUpCall = apiServiceJson.signUp(userData)
        signUpCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    Log.e("createAccount", "Failed to create account: ${response.message()}")
                    onError()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("createAccount", "Network or other error occurred: ${t.message}")
                onError()
            }
        })
    }

    fun fetchIngredientsForRecipe(
        recipeId: String,
        onSuccess: (MutableList<Ingredient_Quantity>) -> Unit,
        onError: (String) -> Unit
    ) {
        val ingredientsCall = apiServiceJson.fetchIngredientsForRecipe(recipeId)
        ingredientsCall.enqueue(object : Callback<MutableList<Ingredient_Quantity>> {
            override fun onResponse(call: Call<MutableList<Ingredient_Quantity>>, response: Response<MutableList<Ingredient_Quantity>>) {
                if (response.isSuccessful) {
                    val ingredientsQuantity = response.body() ?: mutableListOf()
                    onSuccess(ingredientsQuantity)
                } else {
                    onError("Failed to fetch ingredients: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MutableList<Ingredient_Quantity>>, t: Throwable) {
                onError("Network or other error occurred: ${t.message}")
            }
        })
    }

    fun fetchIngredients(
        onSuccess: (MutableList<Ingredient>) -> Unit,
        onError: (String) -> Unit
    ) {
        val ingredientsCall = apiServiceJson.fetchIngredients()
        ingredientsCall.enqueue(object : Callback<MutableList<Ingredient>> {
            override fun onResponse(call: Call<MutableList<Ingredient>>, response: Response<MutableList<Ingredient>>) {
                if (response.isSuccessful) {
                    val ingredients = response.body() ?: mutableListOf()

                    onSuccess(ingredients)
                } else {
                    onError("Failed to fetch ingredients: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MutableList<Ingredient>>, t: Throwable) {
                onError("Network or other error occurred: ${t.message}")
            }
        })
    }

    fun login(
        credentials: Credentials,
        onSuccess: (TokenResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val loginCall = apiServiceJson.login(credentials)
        loginCall.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    if (tokenResponse != null) {
                        // Check if the token is present in the response
                        if (!tokenResponse.token.isNullOrEmpty()) {
                            onSuccess(tokenResponse)
                        } else {
                            Log.e("login", "Login failed: Token is empty")
                            onError("Login failed")
                        }
                    } else {
                        Log.e("login", "Login failed: Response body is null")
                        onError("Login failed")
                    }
                } else {
                    // Handle error response and extract error message from the response body
                    val errorMessage = response.errorBody()?.string() ?: "Login failed"
                    Log.e("login", "Login failed: $errorMessage")
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.e("login", "Network or other error occurred: ${t.message}")
                onError("Network or other error occurred")
            }
        })
    }


    fun fetchSchoppingList(
        token: TokenResponse,
        onSuccess: (MutableList<In_Shopping_List>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Assuming tokenResponse.token contains the token string
        val authorizationHeader = "Bearer ${token.token}" // Format the header as needed

        val schoppingListCall = apiServiceJson.fetchInSchoppingList(authorizationHeader)
        schoppingListCall.enqueue(object : Callback<MutableList<In_Shopping_List>> {
            override fun onResponse(call: Call<MutableList<In_Shopping_List>>, response: Response<MutableList<In_Shopping_List>>) {
                if (response.isSuccessful) {
                    val shoppingList = response.body() ?: mutableListOf()
                    onSuccess(shoppingList)
                } else {
                    onError("Failed to fetch shoppingList: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MutableList<In_Shopping_List>>, t: Throwable) {
                onError("Network or other error occurred: ${t.message}")
            }
        })
    }
    fun fetchRecipeImage(
        recipeId: String,
        onSuccess: (ResponseBody) -> Unit,
        onError: (String) -> Unit
    ) {
        val imageCall = apiServiceJson.fetchRecipeImage(recipeId)
        imageCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    val errorMessage = "Failed to fetch recipe image: ${response.message()}"
                    onError(errorMessage)
                    Log.e("fetchRecipeImage", errorMessage)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errorMessage = "Network or other error occurred: ${t.message}"
                onError(errorMessage)
                Log.e("fetchRecipeImage", errorMessage)
            }
        })
    }

    fun showError(errorMessage: String) {
        Log.e("Error", errorMessage)
    }
}
