package ch.heigvd.pdg_grocerypal


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.databinding.ActivityMainBinding
import ch.heigvd.pdg_grocerypal.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var ingredientQuantityList: MutableList<Ingredient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ingredientQuantityList = mutableListOf()


        ConnectionRecipeUtils.fetchIngredients(
            onSuccess = { ingredientsList ->
                ingredientQuantityList.addAll(ingredientsList)
                insertIngredientsIntoDatabase(ingredientsList)
            },
            onError = { errorMessage ->
                ConnectionRecipeUtils.showError(errorMessage)
            }
        )

        // Add a button click listener to open the LoginActivity
        binding.button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun insertIngredientsIntoDatabase(ingredientsList: List<Ingredient>) {
        val dbHelper = GroceryPalDBHelper(this)
        val db = dbHelper.writableDatabase

        for (ingredient in ingredientsList) {
            val values = ContentValues()
            values.put("Name", ingredient.name)
            values.put("Fiber", ingredient.fiber)
            values.put("Protein", ingredient.protein)
            values.put("Energy", ingredient.energy)
            values.put("Carbs", ingredient.carb)
            values.put("Fat", ingredient.fat)

            db.insert("Ingredient", null, values)
        }

        db.close()
    }


}

