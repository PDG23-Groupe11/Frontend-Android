package ch.heigvd.pdg_grocerypal.list

import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import androidx.fragment.app.FragmentManager
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import com.google.android.material.bottomsheet.BottomSheetDialog

class GroceryListAdapter(private val context: Context, private val groceryList: MutableList<GroceryItem>, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<GroceryListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemDetails: TextView = itemView.findViewById(R.id.itemDetails)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grocery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val groceryItem = groceryList[position]
        holder.itemDetails.text = "${groceryItem.quantity} ${groceryItem.unit} ${groceryItem.name}"
        holder.checkBox.isChecked = groceryItem.isPurchased

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val dbHelper = GroceryPalDBHelper(context)
            dbHelper.updateItemPurchasedStatus(groceryItem.ingredientId, groceryItem.unitId, isChecked)
            updateGroceryList(dbHelper.getAllShoppingListItems())
        }

        holder.itemDetails.setOnClickListener {
            val bottomSheetListFragment = BottomSheetListFragment(groceryList, position, this@GroceryListAdapter)
            bottomSheetListFragment.show(fragmentManager, bottomSheetListFragment.tag)
        }
    }
    override fun getItemCount() = groceryList.size

    fun updateGroceryList(newGroceryList: MutableList<GroceryItem>) {
        groceryList.clear()
        groceryList.addAll(newGroceryList)
    }
}