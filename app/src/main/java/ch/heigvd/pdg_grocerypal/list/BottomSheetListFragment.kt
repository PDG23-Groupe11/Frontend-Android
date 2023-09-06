package ch.heigvd.pdg_grocerypal.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentBottomSheetListBinding

/**
 * Fragment permettant la modification d'un ingrédient
 */
class BottomSheetListFragment(private val groceryList: MutableList<GroceryItem>, private val position: Int, private val adapter: GroceryListAdapter) : BottomSheetDialogFragment() {

    /**
     * Met à jour la liste de courses
     */
    fun updateGroceryList(newGroceryList: MutableList<GroceryItem>) {
        groceryList.clear()
        groceryList.addAll(newGroceryList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBottomSheetListBinding.inflate(inflater, container, false)
        val view = binding.root

        // Affiche les informations de l'ingrédient
        binding.ingredientName.text = groceryList[position].name
        binding.quantityEditText.setText(groceryList[position].quantity.toString())
        binding.titleTextView.text= groceryList[position].unit

        // Gestion du bouton moins
        binding.minusButton.setOnClickListener {
            val currentQuantity = binding.quantityEditText.text.toString().toIntOrNull() ?: 0
            if (currentQuantity > 1) {
                binding.quantityEditText.setText((currentQuantity - 1).toString())
            }
        }

        // Gestion du bouton plus
        binding.plusButton.setOnClickListener {
            val currentQuantity = binding.quantityEditText.text.toString().toIntOrNull() ?: 0
            binding.quantityEditText.setText((currentQuantity + 1).toString())
        }

        // Gestion du bouton de savegarde et mise à jour des informations sur la liste de courses
        binding.saveButton.setOnClickListener {
            val newQuantity = binding.quantityEditText.text.toString().toIntOrNull() ?: 0
            val dbHelper = GroceryPalDBHelper(requireContext())
            dbHelper.updateQuantityInShoppingList(groceryList[position].ingredientId, groceryList[position].unitId, newQuantity)
            updateGroceryList(dbHelper.getAllShoppingListItems())
            adapter.notifyItemChanged(position)
            dismiss()
        }

        // Gestion du bouton de suppression d'un ingrédient de la liste
        binding.deleteProductButton.setOnClickListener {
            val dbHelper = GroceryPalDBHelper(requireContext())
            dbHelper.deletePurchasedItem(groceryList[position].ingredientId, groceryList[position].unitId)
            updateGroceryList(dbHelper.getAllShoppingListItems())
            adapter.notifyItemRemoved(position)
            dismiss()
        }

        return view
    }
}