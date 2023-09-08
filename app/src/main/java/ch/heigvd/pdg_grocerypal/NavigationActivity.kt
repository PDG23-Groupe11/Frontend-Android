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
import ch.heigvd.pdg_grocerypal.databinding.AppNavigationLayoutBinding

/**
 * L'activité de navigation principale de l'application.
 */
class NavigationActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var ingredientQuantityList: MutableList<Ingredient>
    private lateinit var dbHelper: GroceryPalDBHelper
    private lateinit var shoppingList: MutableList<In_Shopping_List>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AppNavigationLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialisation des listes
        ingredientQuantityList = mutableListOf()
        shoppingList = mutableListOf()

        // Initialisation de la base de données locale
        dbHelper = GroceryPalDBHelper(this)

        // Récupération des ingrédients depuis l'API
        ConnectionRecipeUtils.fetchIngredients(
            onSuccess = { ingredientsList ->
                ingredientQuantityList.addAll(ingredientsList)
                dbHelper.insertIngredients(ingredientsList) // Utilisation de dbHelper pour insérer les ingrédients
            },
            onError = { errorMessage ->
                ConnectionRecipeUtils.showError(errorMessage)
            }
        )

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", "")

        if (authToken != null) {
            // Récupération de la liste de courses depuis l'API
            ConnectionRecipeUtils.fetchSchoppingList(
                authToken,
                onSuccess = { shoppingItems ->
                    shoppingList.addAll(shoppingItems)
                    // Vérification des différences et affichage d'une boîte de dialogue de synchronisation
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

    /**
     * Vérifie s'il y a des différences entre la liste de courses locale et distante,
     * et affiche une boîte de dialogue de synchronisation si nécessaire.
     */
    private fun synchronizeData() {
        val remoteShoppingList = shoppingList // Remplacer ceci par les données distantes réelles
        val areDifferent = dbHelper.areShoppingListsDifferent(remoteShoppingList)

        if (areDifferent) {
            showSynchronizationDialog(remoteShoppingList)
        }
    }

    /**
     * Affiche une boîte de dialogue permettant à l'utilisateur de choisir la source de synchronisation.
     *
     * @param remoteShoppingList La liste de courses distante
     */
    private fun showSynchronizationDialog(remoteShoppingList: List<In_Shopping_List>) {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogLayout =
            LayoutInflater.from(this).inflate(R.layout.popupp_synchro_shoppinglist, null)

        val titleTextView = dialogLayout.findViewById<TextView>(R.id.titleTextView)
        val messageTextView = dialogLayout.findViewById<TextView>(R.id.messageTextView)
        val localButton = dialogLayout.findViewById<Button>(R.id.localButton)
        val distantButton = dialogLayout.findViewById<Button>(R.id.distantButton)

        titleTextView.text = "Synchronisation des données"
        messageTextView.text =
            "Les listes de courses locales et distantes sont différentes. Choisissez la source de synchronisation :"

        localButton.text = "Garder les\ndonnées locales"
        distantButton.text = "Charger les\ndonnées distantes"

        dialogBuilder.setView(dialogLayout)
        dialogBuilder.setCancelable(false)

        val dialog = dialogBuilder.create()

        // Gestion du clic sur le bouton "Garder les données locales"
        localButton.setOnClickListener {
            ConnectionRecipeUtils.postShoppingListWithAuthToken(this)
            dialog.dismiss() // Fermer la boîte de dialogue après le clic sur le bouton
        }

        // Gestion du clic sur le bouton "Charger les données distantes"
        distantButton.setOnClickListener {
            dbHelper.clearShoppingList()
            dbHelper.insertShoppingListItems(remoteShoppingList)
            dialog.dismiss() // Fermer la boîte de dialogue après le clic sur le bouton
        }

        dialog.show()
    }
}

