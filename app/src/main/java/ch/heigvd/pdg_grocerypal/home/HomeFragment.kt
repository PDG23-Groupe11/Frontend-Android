package ch.heigvd.pdg_grocerypal.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentHomeBinding
import ch.heigvd.pdg_grocerypal.recipes.RecipeAdapterVertical
import ch.heigvd.pdg_grocerypal.recipes.RecipeCard
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils.showError

/**
 * Fragment qui affiche la page home
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var groceryList: MutableList<GroceryItem>
    private lateinit var recipeList1: MutableList<RecipeCard>
    private lateinit var adapter1: RecipeAdapterVertical
    private lateinit var adapter2: LittleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val dbHelper = GroceryPalDBHelper(requireContext())
        groceryList = dbHelper.getAllShoppingListItems()

        recipeList1 = mutableListOf()

        ConnectionRecipeUtils.fetchRecipes(recipeList1, 3,
            onSuccess = { updatedRecipeList ->
                adapter1.notifyDataSetChanged()
                adapter2.notifyDataSetChanged()
            },
            onError = { errorMessage ->
                showError(errorMessage)
            }
        )

        adapter1 = RecipeAdapterVertical(recipeList1)
        binding.recyclerView1.adapter = adapter1
        binding.recyclerView1.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter2 = LittleListAdapter(groceryList)
        binding.recyclerView2.adapter = adapter2
        binding.recyclerView2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        // Navigation vers la liste de courses
        val modifButton = view.findViewById<Button>(R.id.modifButton)
        modifButton.setOnClickListener {
            val navController = Navigation.findNavController(view)

            navController.popBackStack()
            navController.navigate(R.id.listFragment)
        }

        // Navigation vers les recettes
        val seeAllButton = view.findViewById<Button>(R.id.seeAllButton)
        seeAllButton.setOnClickListener {
            val navController = Navigation.findNavController(view)

            navController.popBackStack()
            navController.navigate(R.id.recipesFragment)
        }

        return view
    }

}