package ch.heigvd.pdg_grocerypal.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.data.model.Ingredient

/**
 * Fragment permettant l'ajout d'ingrédients à la liste
 */
class AddIngredientFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var ingredientAdapter: IngredientAdapter
    private var ingredientList = mutableListOf<Ingredient>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_ingredient, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        searchView = rootView.findViewById(R.id.search)


        // Récupère tous les ingrédients
        val dbHelper = GroceryPalDBHelper(requireContext())
        ingredientList = dbHelper.getAllIngredients()
        ingredientAdapter = IngredientAdapter(requireContext(), ingredientList)

        recyclerView.adapter = ingredientAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Filtrage des ingrédients
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                ingredientAdapter.filter.filter(newText)
                return true
            }
        })

        // Fermeture de la page
        val returnButton = rootView.findViewById<ImageView>(R.id.closeReturn)
        returnButton.setOnClickListener {
            val navController = Navigation.findNavController(rootView)
            navController.navigate(R.id.listFragment)
        }

        return rootView
    }
}
