package ch.heigvd.pdg_grocerypal.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.databinding.FragmentDetailRecipeBinding
import androidx.navigation.fragment.findNavController
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils.showError
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.NutritionalValue
import ch.heigvd.pdg_grocerypal.data.model.NutritionalValueType
import ch.heigvd.pdg_grocerypal.data.model.NutritionalValues
import kotlin.reflect.KProperty


class RecipeDetailsFragment() : Fragment() {

    private lateinit var ingredientList: MutableList<Ingredient>
    private lateinit var binding: FragmentDetailRecipeBinding
    private lateinit var adapter: RecipeAdapterIngredients
    private var currentQuantity = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        val recipe = arguments?.getParcelable<RecipeCard>("recipe")

        val imagePlaceholderResId = arguments?.getInt("imagePlaceholder") ?: R.drawable.image_placeholder

        ingredientList = mutableListOf()

        binding.recipeImage.setImageResource(imagePlaceholderResId)

        if (recipe != null) {
            val recipeIdString = recipe.id.toString()
            ConnectionRecipeUtils.fetchIngredientsForRecipe(
                recipeIdString,
                onSuccess = { ingredientsList ->
                    ingredientList.addAll(ingredientsList)

                    for (ingredient in ingredientsList) {
                        Log.d("ReceivedIngredient", "Ingredient ID: ${ingredient.id}")
                        Log.d("ReceivedIngredient", "Ingredient Name: ${ingredient.name}")
                        Log.d("ReceivedIngredient", "Ingredient Quantity: ${ingredient.quantity}")
                    }
                    adapter.notifyDataSetChanged()
                },
                onError = { errorMessage ->
                    showError(errorMessage)
                }
            )
        }

        // Now, you can set the values to your views if the recipe is not null
        recipe?.let {
            binding.recipeTitle.text = it.name
            binding.itemDuration.text = it.prep_time
            binding.recipePreparationText.text = it.instruction
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

        binding.PlusButtonRecipe.setOnClickListener {
            currentQuantity++
            binding.quantityEditText.setText(currentQuantity.toString())
            // Update the currentQuantity value and notify the adapter of the change
            adapter.updateCurrentQuantity(currentQuantity )
            adapter.notifyDataSetChanged()
        }

        adapter = RecipeAdapterIngredients(ingredientList, currentQuantity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        // TODO lier a la quantité profil
        binding.quantityEditText.setText("1")




        // Initialize the nutritionalValueTextView
        val nutritionalValueTextView = view.findViewById<TextView>(R.id.nutritionalValueTextView)


        val returnButton = view.findViewById<ImageView>(R.id.blackArrowReturn)

        returnButton.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.returnToRecipeView)
        }

        return view
    }

    private fun appendNutritionalValue(value: NutritionalValue, builder: StringBuilder) {
        val formattedValue = "${value.type.displayName} : ${value.quantity} ${value.unit}\n"
        builder.append(formattedValue)
    }
}