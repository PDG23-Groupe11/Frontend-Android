package ch.heigvd.pdg_grocerypal.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity


class RecipeAdapterIngredients(private val ingredientList: List<Ingredient_Quantity>, private var currentQuantity: Int) :
    RecyclerView.Adapter<RecipeAdapterIngredients.RecipeViewHolder>(){

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientDetailsTv : TextView = itemView.findViewById(R.id.itemDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_igredient_recipe_list, parent, false)
        return  RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        val totalQuantity = ingredient.quantity * currentQuantity
// TODO add unit
        holder.ingredientDetailsTv.text = "$totalQuantity ${ingredient.name}"
    }


    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun updateCurrentQuantity(newQuantity: Int) {
        currentQuantity = newQuantity
    }

}