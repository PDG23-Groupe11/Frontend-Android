package ch.heigvd.pdg_grocerypal.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.backEndConnections.ApiResponse
import ch.heigvd.pdg_grocerypal.backEndConnections.ApiService
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentRecipesBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.converter.gson.GsonConverterFactory


class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipeList1: MutableList<RecipeCard>
    private lateinit var ownRecipeList: MutableList<RecipeCard>
    private lateinit var adapter1: RecipeAdapterHorizontal
    private lateinit var adapter2: RecipeAdapterVertical
    private lateinit var groceryList: MutableList<GroceryItem>
    private lateinit var adapterOwnRecipe: RecipeAdapterVertical




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val view = binding.root


        groceryList = mutableListOf(
            GroceryItem("Farine", "g", "100"),
            GroceryItem("Lait", "l", "4"),
            GroceryItem("Oeuf", "pcs", "6"),
            GroceryItem("Chocolat noir", "g", "200"),
            GroceryItem("Chocolat au lait", "g", "200")
        )

        val recipePreparationText = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor 
            incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
            exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure 
            dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
            Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt 
            mollit anim id est laborum.
        """.trimIndent()

        recipeList1 = mutableListOf(
            RecipeCard(2, "Lasagnes", 4,"60 min", recipePreparationText)
        )


        ownRecipeList = mutableListOf(
            RecipeCard(4, "Poulet au curry", 3,"45 min", recipePreparationText)
        )


        // Define your base URL
        val BASE_URL = "http://10.0.2.2:8080"

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the ApiService
        val apiService = retrofit.create(ApiService::class.java)

        // Fetch recipes
        val recipesCall = apiService.fetchRecipes()
        recipesCall.enqueue(object : Callback<MutableList<RecipeCard>> {
            override fun onResponse(call: Call<MutableList<RecipeCard>>, response: Response<MutableList<RecipeCard>>) {
                if (response.isSuccessful) {
                    val recipes = response.body() ?: mutableListOf()

                        recipeList1.addAll(recipes)

                        for (recipe in recipeList1) {
                            Log.d("RecipeList1", "Recipe ID: ${recipe.id}")
                            Log.d("RecipeList1", "Recipe Name: ${recipe.name}")
                            Log.d("RecipeList1", "nb per: ${recipe.nb_per}")
                            Log.d("RecipeList1", "temps: ${recipe.prep_time}")
                            Log.d("RecipeList1", "instructions: ${recipe.instruction}")
                        }

                    adapter1.notifyDataSetChanged()
                    adapter2.notifyDataSetChanged()
                } else {
                    showError("Failed to fetch recipes: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MutableList<RecipeCard>>, t: Throwable) {
                showError("Network or other error occurred: ${t.message}")
            }
        })


        adapter1 = RecipeAdapterHorizontal(recipeList1)
        binding.recyclerView1.adapter = adapter1
        binding.recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter2 = RecipeAdapterVertical(recipeList1)
        binding.recyclerView2.adapter = adapter2
        binding.recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapterOwnRecipe = RecipeAdapterVertical(ownRecipeList)
        binding.recyclerView3.adapter = adapterOwnRecipe
        binding.recyclerView3.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        return view
    }

    fun showError(errorMessage: String) {

        // Log the error message to Logcat
        Log.e("Error", errorMessage)

        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

}