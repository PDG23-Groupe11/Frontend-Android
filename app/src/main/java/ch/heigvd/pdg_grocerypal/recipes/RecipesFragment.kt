package ch.heigvd.pdg_grocerypal.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipeList1: List<RecipeCard>
    private lateinit var ownRecipeList: List<RecipeCard>
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
            RecipeCard(1, "Crêpes", 2,"30 min", recipePreparationText),
                RecipeCard(2, "Lasagnes", 4,"60 min", recipePreparationText),
                RecipeCard(3, "Burger", 1,"30 min", recipePreparationText),
                RecipeCard(3, "Burger", 1,"30 min", recipePreparationText),
                RecipeCard(3, "Burger", 1,"30 min", recipePreparationText),
                RecipeCard(1, "Crêpes", 2,"30 min", recipePreparationText)
        )

        ownRecipeList = mutableListOf(
            RecipeCard(4, "Poulet au curry", 3,"45 min", recipePreparationText)
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