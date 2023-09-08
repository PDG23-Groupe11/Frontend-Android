package ch.heigvd.pdg_grocerypal.recipes

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.databinding.FragmentDetailRecipeBinding
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils.showError
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.config.Configuration
import com.squareup.picasso.Picasso

/**
 * Fragment qui affiche les détails d'une recette, y compris les ingrédients et les informations nutritionnelles.
 */
class RecipeDetailsFragment() : Fragment() {

    private lateinit var ingredientQuantityList: MutableList<Ingredient_Quantity>
    private lateinit var binding: FragmentDetailRecipeBinding
    private lateinit var adapter: RecipeAdapterIngredients
    val sharedPreferences = context?.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
    private var currentQuantity = sharedPreferences?.getInt("nbPerHome", 1)?:1


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

        val bundle = arguments

        if (bundle != null) {
            // Récupération de l'image de la recette depuis les arguments
            val recipePlaceHolderImage = bundle.getInt("imagePlaceholder")?: R.drawable.image_placeholder
            binding.recipeImage.setImageResource(recipePlaceHolderImage)

        }
        // Récupération de la recette actuelle depuis les arguments
        val recipe = arguments?.getParcelable<RecipeCard>("recipe")

        val BASE_URL = Configuration.BaseURL
        val urlString = BASE_URL + "/static/recipeImages/" + recipe?.id.toString()

        val placeHolder = arguments?.getInt("imagePlaceholder")?: R.drawable.image_placeholder


        binding.recipeImage.setImageResource(placeHolder)

        ingredientQuantityList = mutableListOf()

        val imgWidth = binding.recipeImage.layoutParams.width
        val imgHeight = binding.recipeImage.layoutParams.height

        // Récupération de l'image liée à la recette
        Picasso.get()
            .load(urlString)
            .placeholder(R.drawable.image_placeholder)
            .resize(imgWidth, imgHeight)
            .centerCrop()
            .into(binding.recipeImage)


        if (recipe != null) {
            val recipeIdString = recipe.id.toString()

            // Récupération des ingrédients de la recette depuis la base de données distante
            ConnectionRecipeUtils.fetchIngredientsForRecipe(
                recipeIdString,
                onSuccess = { ingredientsList ->
                    ingredientQuantityList.addAll(ingredientsList)

                    for (ingredient in ingredientsList) {

                        // Conversion de la quantité en grammes
                        val quantityInGrams = convertToGrams(ingredient.unitId, ingredient.quantity)

                        // Calcul de la contribution de cet ingrédient aux valeurs nutritionnelles totales
                        val contributionFactor = quantityInGrams / 100.0f

                        // Ajout de la contribution aux valeurs nutritionnelles totales
                        totalFiber += ingredient.fiber * contributionFactor
                        totalProtein += ingredient.protein * contributionFactor
                        totalEnergy += (ingredient.energy * contributionFactor).toInt()
                        totalCarb += ingredient.carb * contributionFactor
                        totalFat += ingredient.fat * contributionFactor

                    }

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
                adapter.updateCurrentQuantity(currentQuantity)
                adapter.notifyDataSetChanged()
            }
        }

        binding.PlusButtonRecipe.setOnClickListener {
            currentQuantity++
            binding.quantityEditText.setText(currentQuantity.toString())
            adapter.updateCurrentQuantity(currentQuantity )
            adapter.notifyDataSetChanged()
        }

        binding.AjouterAListeButton.setOnClickListener {
            showConfirmationDialog(requireActivity(),ingredientQuantityList)
        }


        adapter = RecipeAdapterIngredients(ingredientQuantityList, currentQuantity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.quantityEditText.setText(currentQuantity.toString())

        val returnButton = view.findViewById<ImageView>(R.id.blackArrowReturn)

        returnButton.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.returnToRecipeView)
        }

        return view
    }

    /**
     * Conversion de la quantité en grammes
     */
    private fun convertToGrams(unitId: Int, quantity: Int): Float {

        val unitToGrams = mapOf(
            1 to 1.0f,   // grams (1 gram = 1 gram)
            2 to 1.0f,   // ml (1 ml = 1 gram for simplicity)
            3 to 0.0f,   // pcs (no conversion)
            4 to 5.0f,   // c.à.c (1 c.à.c = 5 grams)
            5 to 15.0f   // c.à.s (1 c.à.s = 15 grams)
        )

        val conversionFactor = unitToGrams[unitId] ?: 0.0f

        return quantity * conversionFactor
    }

    /**
     * Méthode pour afficher et gérer une boîte de dialogue de confirmation lors d'ajout des ingrédients
     * à la liste d'achat
     */
    private fun showConfirmationDialog(context: Context, ingredients: List<Ingredient_Quantity>) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.popup_add_recipe_ingredients, null)
        val titleTextView = dialogLayout.findViewById<TextView>(R.id.titleTextView)
        val messageTextView = dialogLayout.findViewById<TextView>(R.id.messageTextView)
        val addButton = dialogLayout.findViewById<Button>(R.id.addButton)
        val cancelButton = dialogLayout.findViewById<Button>(R.id.cancelButton)
        val dbHelper = context?.let { GroceryPalDBHelper(it) }

        titleTextView.text = "Confirmer l'ajout d'ingrédient"
        messageTextView.text =
            "Si vous confirmez, tous les ingrédients de cette recette seront ajoutés à votre liste de courses."

        addButton.text = "Ajouter"

        cancelButton.text = "Annuler"

        alertDialogBuilder
            .setView(dialogLayout)
            .setCancelable(false)

        val alertDialog = alertDialogBuilder.create()

        addButton.setOnClickListener {
            dbHelper?.addOrUpdateShoppingListItems(context, ingredients)
            Toast.makeText(context, "Ingrédients ajoutés à la liste", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}