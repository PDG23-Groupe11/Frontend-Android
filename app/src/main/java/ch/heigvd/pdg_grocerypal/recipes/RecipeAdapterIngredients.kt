package ch.heigvd.pdg_grocerypal.recipes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.ui.login.LoginActivity



class RecipeAdapterIngredients(private val groceryList: List<GroceryItem>) :
    RecyclerView.Adapter<RecipeAdapterIngredients.RecipeViewHolder>(){

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientNameTv : TextView = itemView.findViewById(R.id.itemDetails)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_igredient_recipe_list, parent, false)
        return  RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val groceryItem = groceryList[position]
        holder.ingredientNameTv.text = groceryItem.name
    }


    override fun getItemCount(): Int {
        return groceryList.size
    }

}