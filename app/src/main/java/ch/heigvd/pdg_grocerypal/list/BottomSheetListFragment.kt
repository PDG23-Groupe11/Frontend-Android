package ch.heigvd.pdg_grocerypal.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentBottomSheetListBinding


class BottomSheetListFragment(private val groceryList: List<GroceryItem>, private val position: Int, private val adapter: GroceryListAdapter) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBottomSheetListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.ingredientName.text = groceryList[position].name
        binding.quantityEditText.setText(groceryList[position].quantity)

        binding.minusButton.setOnClickListener {
            val currentQuantity = binding.quantityEditText.text.toString().toIntOrNull() ?: 0
            if (currentQuantity > 1) {
                binding.quantityEditText.setText((currentQuantity - 1).toString())
            }
        }

        binding.plusButton.setOnClickListener {
            val currentQuantity = binding.quantityEditText.text.toString().toIntOrNull() ?: 0
            binding.quantityEditText.setText((currentQuantity + 1).toString())
        }

        binding.saveButton.setOnClickListener {
            val newQuantity = binding.quantityEditText.text.toString()
            groceryList[position].quantity = newQuantity
            adapter.notifyDataSetChanged()
            dismiss()
        }

        binding.deleteProductButton.setOnClickListener {
            dismiss()
        }

        return view
    }
}