package ch.heigvd.pdg_grocerypal.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipeList1: List<RecipeCard>
    private lateinit var adapter1: RecipeAdapter1
    private lateinit var adapter2: RecipeAdapter2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val view = binding.root

        recipeList1 = listOf(
            RecipeCard(R.drawable.crepes_image, "Crêpes", "30 min"),
            RecipeCard(R.drawable.crepes_image2, "Crêpes", "30 min"),
            RecipeCard(R.drawable.lasagne_image, "Lasagnes", "90 min"),
            RecipeCard(R.drawable.burger_image1, "Burger", "30 min"),
            RecipeCard(R.drawable.burger_image2, "Burger", "30 min"),
            RecipeCard(R.drawable.burger_image3, "Burger", "30 min"),
            RecipeCard(R.drawable.burger_image4, "Burger", "30 min")
        )

        adapter1 = RecipeAdapter1(recipeList1)
        binding.recyclerView1.adapter = adapter1
        binding.recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter2 = RecipeAdapter2(recipeList1)
        binding.recyclerView2.adapter = adapter2
        binding.recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return view
    }

}