package ch.heigvd.pdg_grocerypal.recipes

import android.os.Bundle
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
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
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

        val recipe = arguments?.getParcelable<RecipeCard>("recipe")

        val groceryList = arguments?.getParcelableArrayList<GroceryItem>("groceryList") ?: mutableListOf()

        val imagePlaceholderResId = arguments?.getInt("imagePlaceholder") ?: R.drawable.image_placeholder

        binding.recipeImage.setImageResource(imagePlaceholderResId)


        // Now, you can set the values to your views if the recipe is not null
        recipe?.let {
            binding.recipeTitle.text = it.recipeName
            binding.itemDuration.text = it.recipeDuration
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

        adapter = RecipeAdapterIngredients(groceryList, currentQuantity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        // TODO lier a la quantité profil
        binding.quantityEditText.setText("1")

        // Add an EditText for recipe preparation text
        val recipePreparationField = view.findViewById<TextView>(R.id.recipePreparationText)



//        // Set the text in the EditText
//        recipePreparationField.setText(recipePreparationText)

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