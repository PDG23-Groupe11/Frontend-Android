package ch.heigvd.pdg_grocerypal.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.databinding.FragmentDetailRecipeBinding
import androidx.navigation.fragment.findNavController
import ch.heigvd.pdg_grocerypal.data.model.NutritionalValue
import ch.heigvd.pdg_grocerypal.data.model.NutritionalValueType
import ch.heigvd.pdg_grocerypal.data.model.NutritionalValues
import kotlin.reflect.KProperty


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

        binding.PlusButtonRecipe.setOnClickListener {
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

        // Add an EditText for recipe preparation text
        val recipePreparationField = view.findViewById<TextView>(R.id.recipePreparationText)

        // Your recipe preparation text
        val recipePreparationText = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor 
            incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
            exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure 
            dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
            Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt 
            mollit anim id est laborum.
        """.trimIndent()

        // Set the text in the EditText
        recipePreparationField.setText(recipePreparationText)

        val nutritionalValues = NutritionalValues(
            fiber = NutritionalValue(NutritionalValueType.FIBER, "g", "5"),
            protein = NutritionalValue(NutritionalValueType.PROTEIN, "g", "10"),
            energy = NutritionalValue(NutritionalValueType.ENERGY, "kcal", "200"),
            carbs = NutritionalValue(NutritionalValueType.CARBS, "g", "30"),
            fat = NutritionalValue(NutritionalValueType.FAT, "g", "15")
        )

        // Initialize the nutritionalValueTextView
        val nutritionalValueTextView = view.findViewById<TextView>(R.id.nutritionalValueTextView)

        // Create a StringBuilder to build the text
        val nutritionalValueText = StringBuilder()

        // Iterate through the NutritionalValues and append each value to the StringBuilder
        appendNutritionalValue(nutritionalValues.fiber, nutritionalValueText)
        appendNutritionalValue(nutritionalValues.protein, nutritionalValueText)
        appendNutritionalValue(nutritionalValues.energy, nutritionalValueText)
        appendNutritionalValue(nutritionalValues.carbs, nutritionalValueText)
        appendNutritionalValue(nutritionalValues.fat, nutritionalValueText)

        // Set the formatted text in the nutritionalValueTextView
        nutritionalValueTextView.text = nutritionalValueText.toString()

        val returnButton = view.findViewById<ImageView>(R.id.blackArrowReturn)

        returnButton.setOnClickListener {
            // Navigate back to the previous fragment
            findNavController().navigateUp()
        }

        return view
    }

    private fun appendNutritionalValue(value: NutritionalValue, builder: StringBuilder) {
        val formattedValue = "${value.type.displayName} : ${value.quantity} ${value.unit}\n"
        builder.append(formattedValue)
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