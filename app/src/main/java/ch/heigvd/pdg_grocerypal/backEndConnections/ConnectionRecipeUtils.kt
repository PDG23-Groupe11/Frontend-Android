package ch.heigvd.pdg_grocerypal.backEndConnections
import android.content.Context
import android.util.Log
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.config.Configuration
import ch.heigvd.pdg_grocerypal.data.model.Credentials
import ch.heigvd.pdg_grocerypal.data.model.In_Shopping_List
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.data.model.TokenResponse
import ch.heigvd.pdg_grocerypal.data.model.UserData
import ch.heigvd.pdg_grocerypal.data.model.UserInfos
import ch.heigvd.pdg_grocerypal.recipes.RecipeCard
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


/**
 * Cet objet utilitaire contient des fonctions pour gérer les connexions avec le backend de l'application.
 */
object ConnectionRecipeUtils {

    private val BASE_URL = Configuration.BaseURL

    // Crée une instance OkHttpClient pour log les requêtes réseau si besoin

    val okHttpClient = OkHttpClient.Builder()
        // Add an HttpLoggingInterceptor for logging network requests
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set the logging level to log the request body
        })
        .build()


    private val retrofitJson = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiServiceJson = retrofitJson.create(ApiService::class.java)

    private val retrofitImage = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create()) // Use ScalarsConverterFactory for non-JSON responses
        .build()

    /**
     * Création d'un compte utilisateur
     */
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


    /**
     * Gestion de la connection de l'utilisateur
     */
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
                        if (tokenResponse.token.isNotEmpty()) {
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


    /**
     * Récupérer les informations de l'utilisateur
     */
    fun fetchUserInfos(
        token: String,
        onSuccess: (UserInfos) -> Unit,
        onError: (String) -> Unit
    ) {
        val userInfosCall = apiServiceJson.fetchUserInfos("Bearer $token")
        userInfosCall.enqueue(object : Callback<UserInfos> {
            override fun onResponse(call: Call<UserInfos>, response: Response<UserInfos>) {
                if (response.isSuccessful) {
                    val userInfos = response.body()
                    if (userInfos != null) {
                        onSuccess(userInfos)
                    } else {
                        Log.e("fetchUserInfos", "UserInfos is null")
                        onError("Failed to fetch user information")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Failed to fetch user information"
                    Log.e("fetchUserInfos", "Failed to fetch user information: $errorMessage")
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<UserInfos>, t: Throwable) {
                Log.e("fetchUserInfos", "Network or other error occurred: ${t.message}")
                onError("Network or other error occurred")
            }
        })
    }


    /**
     * Mettre à jour les information utilisateurs de la back-end
     */
    fun postUserInfos(
        token: String,
        userInfos: UserInfos,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val postShoppingListCall = apiServiceJson.postUserInfos("Bearer $token", userInfos)
        postShoppingListCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to user infos: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Network or other error occurred: ${t.message}")
            }
        })
    }

    /**
     * Récupération de la liste d'achat de l'utilisateur
     */
    fun fetchSchoppingList(
        token: String,
        onSuccess: (MutableList<In_Shopping_List>) -> Unit,
        onError: (String) -> Unit
    ) {
        val schoppingListCall = apiServiceJson.fetchInSchoppingList("Bearer $token")
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

    /**
     * Mettre à jour la liste d'achat de l'utilisateur dans la back-end
     */
    fun postInShoppingList(
        token: String,
        shoppingList: List<In_Shopping_List>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val postShoppingListCall = apiServiceJson.postInSchoppingList("Bearer $token", shoppingList)
        postShoppingListCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to post shopping list: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Network or other error occurred: ${t.message}")
            }
        })
    }

    /**
     * Récupération des recettes de l'utilisateur
     */
    fun fetchUserRecipes(
        token: String,
        onSuccess: (MutableList<RecipeCard>) -> Unit,
        onError: (String) -> Unit
    ) {
        val schoppingListCall = apiServiceJson.fetchUserRecipes("Bearer $token")
        schoppingListCall.enqueue(object : Callback<MutableList<RecipeCard>> {
            override fun onResponse(call: Call<MutableList<RecipeCard>>, response: Response<MutableList<RecipeCard>>) {
                if (response.isSuccessful) {
                    val recipeList = response.body() ?: mutableListOf()
                    onSuccess(recipeList)
                } else {
                    onError("Failed to fetch user recipe list: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MutableList<RecipeCard>>, t: Throwable) {
                onError("Network or other error occurred: ${t.message}")
            }
        })
    }

    /**
     * Récupération de la liste d'ingrédients d'une recette donnée
     */
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

    /**
     * Récupération des recettes publiques de l'application
     */
    fun fetchRecipes(
        recipeList: MutableList<RecipeCard>,
        minToAdd: Int,
        onSuccess: (MutableList<RecipeCard>) -> Unit,
        onError: (String) -> Unit
    ) {
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

    /**
     * Récupération des ingrédients de l'application
     */
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

    /**
     * Récupère le token d'identification et met à jour la shoping list de l'utilisateur en back-end
     */
    fun postShoppingListWithAuthToken(context: Context) {
        val dbHelper = GroceryPalDBHelper(context)

        val shoppingList = dbHelper.getAllInShoppingListItems()
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", null)

        if (authToken != null) {
            postInShoppingList(
                authToken,
                shoppingList,
                onSuccess = {
                    Log.e("postShoppingListWithAuthToken", "Successful insert")
                },
                onError = { errorMessage ->
                    showError(errorMessage)
                }
            )
        }
    }

    /**
     * Log l'erreur reçue
     */
    fun showError(errorMessage: String) {
        Log.e("Error", errorMessage)
    }
}
