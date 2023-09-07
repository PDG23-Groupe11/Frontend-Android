package ch.heigvd.pdg_grocerypal

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.data.model.In_Shopping_List
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.TokenResponse
import ch.heigvd.pdg_grocerypal.databinding.AppNavigationLayoutBinding

class NavigationActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var ingredientQuantityList: MutableList<Ingredient>
    private lateinit var dbHelper: GroceryPalDBHelper
    private lateinit var shoppingList: MutableList<In_Shopping_List>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AppNavigationLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ingredientQuantityList = mutableListOf()

        shoppingList = mutableListOf()

        dbHelper = GroceryPalDBHelper(this)

        ConnectionRecipeUtils.fetchIngredients(
            onSuccess = { ingredientsList ->
                ingredientQuantityList.addAll(ingredientsList)
                dbHelper.insertIngredients(ingredientsList) // Use dbHelper to insert ingredients
            },
            onError = { errorMessage ->
                ConnectionRecipeUtils.showError(errorMessage)
            }
        )

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", "")

        if (authToken != null) {
            val tokenResponse = TokenResponse(authToken)
            ConnectionRecipeUtils.fetchSchoppingList(
                token = tokenResponse,
                onSuccess = { shoppingItems ->
                    shoppingList.addAll(shoppingItems)
                    // Check for differences and show synchronization dialog
                    synchronizeData()
                },
                onError = { errorMessage ->
                    ConnectionRecipeUtils.showError(errorMessage)
                }
            )
        }


        // Initialisation du NavController
        navController = findNavController(R.id.nav_host_fragment)

        // Mise en place de la barre de navigation
        binding.bottomNavigationView.setupWithNavController(navController)
    }
    private fun synchronizeData() {
        val remoteShoppingList = shoppingList // Replace this with actual remote data
        val areDifferent = dbHelper.areShoppingListsDifferent(remoteShoppingList)

        if (areDifferent) {
            showSynchronizationDialog(remoteShoppingList)
        }
    }

    private fun showSynchronizationDialog(remoteShoppingList: List<In_Shopping_List>) {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.popupp_synchro_shoppinglist, null)

        val titleTextView = dialogLayout.findViewById<TextView>(R.id.titleTextView)
        val messageTextView = dialogLayout.findViewById<TextView>(R.id.messageTextView)
        val localButton = dialogLayout.findViewById<Button>(R.id.localButton)
        val distantButton = dialogLayout.findViewById<Button>(R.id.distantButton)

        titleTextView.text = "Synchronisation des données"
        messageTextView.text = "Les listes de courses locales et distantes sont différentes. Choisissez la source de synchronisation :"

        localButton.text = "Garder les\ndonnées locales"
        distantButton.text = "Charger les\ndonnées distantes"

        dialogBuilder.setView(dialogLayout)
        dialogBuilder.setCancelable(false)

        val dialog = dialogBuilder.create()

        localButton.setOnClickListener {
            synchronizeFromLocalToRemote()
            dialog.dismiss() // Dismiss the dialog after the button press
        }

        distantButton.setOnClickListener {
            synchronizeFromRemoteToLocal(remoteShoppingList)
            dialog.dismiss() // Dismiss the dialog after the button press
        }

        dialog.show()
    }

    private fun synchronizeFromLocalToRemote() {
        // Implement synchronization logic from local to remote
        // Update the remote database with local data
        // Handle errors appropriately
    }

    private fun synchronizeFromRemoteToLocal(remoteShoppingList: List<In_Shopping_List>) {
        dbHelper.clearShoppingList()
        dbHelper.insertShoppingListItems(remoteShoppingList)
    }
}



