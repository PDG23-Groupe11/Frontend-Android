package ch.heigvd.pdg_grocerypal.recipes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.databinding.FragmentRecipesBinding


class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipeList1: MutableList<RecipeCard>
    private lateinit var ownRecipeList: MutableList<RecipeCard>
    private lateinit var adapter1: RecipeAdapterHorizontal
    private lateinit var adapter2: RecipeAdapterVertical
    private lateinit var adapterOwnRecipe: RecipeAdapterVertical

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val view = binding.root



        recipeList1 = mutableListOf()


        ownRecipeList = mutableListOf()


        // Access the activity's context to get SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        // Now you can use sharedPreferences to read or write data
        val authToken = sharedPreferences.getString("auth_token", null)

        if (authToken != null) {

            ConnectionRecipeUtils.fetchUserRecipes(authToken,
                onSuccess = { personnalRecipes ->
                    // Handle success, for example, update your adapter and UI here
                    ownRecipeList.addAll(personnalRecipes)
                    adapterOwnRecipe.notifyDataSetChanged()
                },
                onError = { errorMessage ->
                    // Handle error, for example, show a Toast or log the error
                    ConnectionRecipeUtils.showError(errorMessage)
                }
            )
        }

        ConnectionRecipeUtils.fetchRecipes(recipeList1, 10,
            onSuccess = { updatedRecipeList ->
                // Handle success, for example, update your adapter and UI here
                adapter1.notifyDataSetChanged()
                adapter2.notifyDataSetChanged()
            },
            onError = { errorMessage ->
                // Handle error, for example, show a Toast or log the error
                ConnectionRecipeUtils.showError(errorMessage)
            }
        )

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

}