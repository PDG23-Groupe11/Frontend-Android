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
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentHomeBinding
import ch.heigvd.pdg_grocerypal.list.ListFragment
import ch.heigvd.pdg_grocerypal.recipes.RecipeAdapter2
import ch.heigvd.pdg_grocerypal.recipes.RecipeCard
import android.os.Handler



class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var groceryList: MutableList<GroceryItem>
    private lateinit var recipeList1: List<RecipeCard>
    private lateinit var adapter1: RecipeAdapter2
    private lateinit var adapter2: LittleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        recipeList1 = listOf(
            RecipeCard(R.drawable.crepes_image, "Crêpes", "30 min"),
            RecipeCard(R.drawable.lasagne_image, "Lasagnes", "90 min"),
            RecipeCard(R.drawable.burger_image1, "Burger", "30 min")
        )

        groceryList = mutableListOf(
            GroceryItem("Farine", "g", "100"),
            GroceryItem("Lait", "l", "4"),
            GroceryItem("Oeuf", "pcs", "6"),
            GroceryItem("Chocolat noir", "g", "200"),
            GroceryItem("Chocolat au lait", "g", "200"),
        )


        adapter1 = RecipeAdapter2(recipeList1)
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