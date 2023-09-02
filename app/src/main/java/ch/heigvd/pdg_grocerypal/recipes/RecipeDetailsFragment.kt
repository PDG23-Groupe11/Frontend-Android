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

class RecipeDetailsFragment() : Fragment() {

    private lateinit var binding: FragmentDetailRecipeBinding
    private lateinit var adapter: RecipeAdapterIngredients
    private var currentQuantity = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Retrieve the recipe argument from the fragment's arguments
        val recipe = arguments?.getParcelable<RecipeCard>("recipe")

        // Initialize the groceryList by getting it from the recipe if not null
        val groceryList = recipe?.groceryList ?: emptyList()

        // Now, you can set the values to your views if the recipe is not null
        recipe?.let {
            // Set the recipeImage, recipeName, and recipeDuration values here
            binding.recipeImage.setImageResource(it.recipeImage)
            binding.recipeTitle.text = it.recipeName
            binding.itemDuration.text = it.recipeDuration
        }

        binding.MinusButtonRecipe.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                binding.quantityEditText.setText(currentQuantity.toString())
                // Update the currentQuantity value and notify the adapter of the change
                adapter.updateCurrentQuantity(currentQuantity)
                adapter.notifyDataSetChanged()
            }
        }

        binding.PlusbuttonRecipe.setOnClickListener {
            currentQuantity++
            binding.quantityEditText.setText(currentQuantity.toString())
            // Update the currentQuantity value and notify the adapter of the change
            adapter.updateCurrentQuantity(currentQuantity )
            adapter.notifyDataSetChanged()
        }

        adapter = RecipeAdapterIngredients(groceryList, currentQuantity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        // TODO lier a la quantité profil
        binding.quantityEditText.setText("1")

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