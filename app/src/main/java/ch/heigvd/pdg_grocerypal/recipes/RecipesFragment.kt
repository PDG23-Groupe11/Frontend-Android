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

/**
 * Fragment qui affiche une liste de recettes.
 */
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


        // Accéder au contexte de l'activité pour obtenir les préférences partagées
        val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        // Lecture du jeton d'authentification depuis les préférences partagées
        val authToken = sharedPreferences.getString("auth_token", null)

        if (authToken != null) {
            // Récupération des recettes personnelles de l'utilisateur
            ConnectionRecipeUtils.fetchUserRecipes(authToken,
                onSuccess = { personnalRecipes ->
                    ownRecipeList.addAll(personnalRecipes)
                    adapterOwnRecipe.notifyDataSetChanged()
                },
                onError = { errorMessage ->
                    ConnectionRecipeUtils.showError(errorMessage)
                }
            )
        }

        // Récupération des recettes publiques de l'application
        ConnectionRecipeUtils.fetchRecipes(recipeList1, 10,
            onSuccess = { updatedRecipeList ->
                adapter1.notifyDataSetChanged()
                adapter2.notifyDataSetChanged()
            },
            onError = { errorMessage ->
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