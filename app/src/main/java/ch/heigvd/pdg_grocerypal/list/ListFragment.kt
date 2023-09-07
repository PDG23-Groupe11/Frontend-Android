package ch.heigvd.pdg_grocerypal.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentListBinding

/**
 * Fragment qui affiche la liste de course
 */
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var groceryList: MutableList<GroceryItem>
    private lateinit var adapter: GroceryListAdapter

    /**
     * Affichage d'un message si la liste est vide
     */
    private fun updateEmptyListMessageVisibility() {
        val emptyListMessage = binding.emptyListMessage

        if (groceryList.isEmpty()) {
            emptyListMessage.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            emptyListMessage.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    /**
     * Met à jour la liste de couses
     */
    fun updateGroceryList(newGroceryList: MutableList<GroceryItem>) {
        groceryList.clear()
        groceryList.addAll(newGroceryList)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        // Récpère la liste des ingrédients
        val dbHelper = GroceryPalDBHelper(requireContext())
        groceryList = dbHelper.getAllShoppingListItems()

        // Navigation vers le fragment AddIngredientFragment
        val addIngredientButton = view.findViewById<Button>(R.id.addIngredientButton)
        addIngredientButton.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.popBackStack()
            navController.navigate(R.id.addIngredientFragment)
        }

        updateEmptyListMessageVisibility()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroceryListAdapter(requireContext(), groceryList, parentFragmentManager)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Button qui permet de supprimer tout les ingrédients déjà achetés
        binding.deletePurchasedButton.setOnClickListener {
            val dbHelper = GroceryPalDBHelper(requireContext())
            dbHelper.deleteAllPurchasedItems(requireActivity())
            updateGroceryList(dbHelper.getAllShoppingListItems())
            view.post {
                adapter.notifyDataSetChanged()
            }
        }

    }
}
