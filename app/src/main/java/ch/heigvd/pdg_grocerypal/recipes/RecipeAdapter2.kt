package ch.heigvd.pdg_grocerypal.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R


class RecipeAdapter2(private val recipeList: List<RecipeCard>) :
    RecyclerView.Adapter<RecipeAdapter2.RecipeViewHolder>(){

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView : ImageView = itemView.findViewById(R.id.itemImage)
        val recipeNameTv : TextView = itemView.findViewById(R.id.itemTitle)
        val recipeDurationTv : TextView = itemView.findViewById(R.id.itemDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vercital_recipe, parent, false)
        return  RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.recipeImageView.setImageResource(recipe.recipeImage)
        holder.recipeNameTv.text = recipe.recipeName
        holder.recipeDurationTv.text = recipe.recipeDuration
    }


    override fun getItemCount(): Int {
        return recipeList.size
    }

}