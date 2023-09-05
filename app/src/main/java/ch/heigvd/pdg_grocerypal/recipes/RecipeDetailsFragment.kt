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
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils.showError
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.data.model.NutritionalValue


class RecipeDetailsFragment() : Fragment() {

    private lateinit var ingredientQuantityList: MutableList<Ingredient_Quantity>
    private lateinit var binding: FragmentDetailRecipeBinding
    private lateinit var adapter: RecipeAdapterIngredients
    private var currentQuantity = 1


    var totalFiber = 0.0f
    var totalProtein = 0.0f
    var totalEnergy = 0
    var totalCarb = 0.0f
    var totalFat = 0.0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        val recipe = arguments?.getParcelable<RecipeCard>("recipe")

        val imagePlaceholderResId = arguments?.getInt("imagePlaceholder") ?: R.drawable.image_placeholder

        ingredientQuantityList = mutableListOf()

        binding.recipeImage.setImageResource(imagePlaceholderResId)


        if (recipe != null) {
            val recipeIdString = recipe.id.toString()
            ConnectionRecipeUtils.fetchIngredientsForRecipe(
                recipeIdString,
                onSuccess = { ingredientsList ->
                    ingredientQuantityList.addAll(ingredientsList)

                    for (ingredient in ingredientsList) {

                        Log.d("IngredientDebug", "ID: ${ingredient.id}")
                        Log.d("IngredientDebug", "Name: ${ingredient.name}")
                        Log.d("IngredientDebug", "Fiber: ${ingredient.fiber}")
                        Log.d("IngredientDebug", "Protein: ${ingredient.protein}")
                        Log.d("IngredientDebug", "Energy: ${ingredient.energy}")
                        Log.d("IngredientDebug", "Carb: ${ingredient.carb}")
                        Log.d("IngredientDebug", "Fat: ${ingredient.fat}")
                        Log.d("IngredientDebug", "Unit ID: ${ingredient.unitId}")
                        Log.d("IngredientDebug", "Quantity: ${ingredient.quantity}")

                        // Convert quantity to grams
                        val quantityInGrams = convertToGrams(ingredient.unitId, ingredient.quantity)

                        // Calculate the contribution of this ingredient to the total
                        val contributionFactor = quantityInGrams / 100.0f

                        // Add the contribution to the total nutritional values
                        totalFiber += ingredient.fiber * contributionFactor
                        totalProtein += ingredient.protein * contributionFactor
                        totalEnergy += (ingredient.energy * contributionFactor).toInt()
                        totalCarb += ingredient.carb * contributionFactor
                        totalFat += ingredient.fat * contributionFactor

                    }

                    // Format the total nutritional values as strings
                    val formattedFiber = String.format("Fiber: %.1f g", totalFiber)
                    val formattedProtein = String.format("Protein: %.1f g", totalProtein)
                    val formattedEnergy = String.format("Energy: %d kcal", totalEnergy)
                    val formattedCarb = String.format("Carbs: %.1f g", totalCarb)
                    val formattedFat = String.format("Fat: %.1f g", totalFat)

                    val totalNutritionalValuesText = """
                        $formattedFiber
                        $formattedProtein
                        $formattedEnergy
                        $formattedCarb
                        $formattedFat
                    """.trimIndent()

                    val nutritionalValueTextView = view.findViewById<TextView>(R.id.nutritionalValueTextView)
                    nutritionalValueTextView.text = totalNutritionalValuesText

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
            binding.itemDuration.text = it.prep_time + " min"
            val formattedInstruction = it.instruction.replace("\\n", "\n")
            binding.recipePreparationText.text = formattedInstruction
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

        adapter = RecipeAdapterIngredients(ingredientQuantityList, currentQuantity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        // TODO lier a la quantité profil
        binding.quantityEditText.setText("1")

        val returnButton = view.findViewById<ImageView>(R.id.blackArrowReturn)

        returnButton.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.returnToRecipeView)
        }

        return view
    }


    private fun convertToGrams(unitId: Int, quantity: Int): Float {
        // Define conversion factors for units to grams
        val unitToGrams = mapOf(
            1 to 1.0f,   // grams (1 gram = 1 gram)
            2 to 1.0f,   // ml (1 ml = 1 gram for simplicity)
            3 to 0.0f,   // pcs (no conversion)
            4 to 5.0f,   // c.à.c (1 c.à.c = 5 grams)
            5 to 15.0f   // c.à.s (1 c.à.s = 15 grams)
        )

        // Get the conversion factor for the given unitId, defaulting to 0 if not found
        val conversionFactor = unitToGrams[unitId] ?: 0.0f

        // Calculate the quantity in grams
        return quantity * conversionFactor
    }
}