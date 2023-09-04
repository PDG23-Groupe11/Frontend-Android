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


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var groceryList: MutableList<GroceryItem>
    private lateinit var recipeList1: List<RecipeCard>
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
        val recipePreparationText = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor 
            incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
            exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure 
            dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
            Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt 
            mollit anim id est laborum.
        """.trimIndent()

        recipeList1 = listOf(
            RecipeCard(R.drawable.crepes_image, "Crêpes", "30 min", groceryList),
            RecipeCard(R.drawable.lasagne_image, "Lasagnes", "90 min", groceryList),
            RecipeCard(R.drawable.burger_image1, "Burger", "30 min", groceryList)
        )


        adapter1 = RecipeAdapterVertical(recipeList1)
        binding.recyclerView1.adapter = adapter1
        binding.recyclerView1.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter2 = LittleListAdapter(groceryList)
        binding.recyclerView2.adapter = adapter2
        binding.recyclerView2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        val modifButton = view.findViewById<Button>(R.id.modifButton)

        modifButton.setOnClickListener {
            val navController = Navigation.findNavController(view)

            navController.popBackStack()
            navController.navigate(R.id.listFragment)
        }

        val seeAllButton = view.findViewById<Button>(R.id.seeAllButton)
        seeAllButton.setOnClickListener {
            val navController = Navigation.findNavController(view)

            navController.popBackStack()
            navController.navigate(R.id.recipesFragment)
        }

        return view
    }

}