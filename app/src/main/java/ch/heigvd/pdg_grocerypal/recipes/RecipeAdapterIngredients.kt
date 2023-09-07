package ch.heigvd.pdg_grocerypal.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity


class RecipeAdapterIngredients(private val ingredientQuantityList: List<Ingredient_Quantity>, private var currentQuantity: Int) :
    RecyclerView.Adapter<RecipeAdapterIngredients.RecipeViewHolder>(){

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientDetailsTv : TextView = itemView.findViewById(R.id.itemDetails)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_igredient_recipe_list, parent, false)
        return  RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val ingredient = ingredientQuantityList[position]
        val totalQuantity = ingredient.quantity * currentQuantity

        // Get the unit name based on the unitId using the dbHelper instance
        val dbHelper = GroceryPalDBHelper(holder.itemView.context)
        val unitName = dbHelper.getUnitName(ingredient.unitId)

        holder.ingredientDetailsTv.text = "$totalQuantity $unitName ${ingredient.name}"

    }


    override fun getItemCount(): Int {
        return ingredientQuantityList.size
    }

    fun updateCurrentQuantity(newQuantity: Int) {
        currentQuantity = newQuantity
    }

}