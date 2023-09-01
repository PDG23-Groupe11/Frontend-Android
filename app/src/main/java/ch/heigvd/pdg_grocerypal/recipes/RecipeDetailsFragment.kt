package ch.heigvd.pdg_grocerypal.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentDetailRecipeBinding

class RecipeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailRecipeBinding
    private lateinit var groceryList: MutableList<GroceryItem>
    private lateinit var adapter: RecipeAdapterIngredients

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Retrieve the recipe argument from the fragment's arguments
        val recipe = arguments?.getParcelable<RecipeCard>("recipe")

        // Now, you can set the values to your views if the recipe is not null
        recipe?.let {
            // Set the recipeImage, recipeName, and recipeDuration values here
            binding.recipeImage.setImageResource(it.recipeImage)
            binding.recipeTitle.text = it.recipeName
            binding.itemDuration.text = it.recipeDuration
        }

        groceryList = mutableListOf(
            GroceryItem("Farine", "g", "100"),
            GroceryItem("Lait", "l", "4"),
            GroceryItem("Oeuf", "pcs", "6"),
            GroceryItem("Chocolat noir", "g", "200"),
            GroceryItem("Chocolat au lait", "g", "200")
        )

        adapter = RecipeAdapterIngredients(groceryList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val returnButton = view.findViewById<ImageView>(R.id.blackArrowReturn)

        returnButton.setOnClickListener {
            // Navigate to the desired fragment using the NavController
            val navController = Navigation.findNavController(view)
            // Use the appropriate action ID from your navigation graph
            navController.navigate(R.id.returnToRecipeView)
        }

        return view
    }

    companion object {
        fun newInstance(recipe: RecipeCard): RecipeDetailsFragment {
            val fragment = RecipeDetailsFragment()
            val args = Bundle()
            args.putParcelable("recipe", recipe)
            fragment.arguments = args
            return fragment
        }
    }
}