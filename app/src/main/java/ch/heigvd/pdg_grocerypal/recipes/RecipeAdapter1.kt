package ch.heigvd.pdg_grocerypal.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R


class RecipeAdapter1(private val recipeList: List<RecipeCard>) :
    RecyclerView.Adapter<RecipeAdapter1.RecipeViewHolder>(){

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView : ImageView = itemView.findViewById(R.id.itemImage)
        val recipeNameTv : TextView = itemView.findViewById(R.id.itemTitle)
        val recipeDurationTv : TextView = itemView.findViewById(R.id.itemDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horizontal_recipes, parent, false)
        return  RecipeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.recipeImageView.setImageResource(recipe.recipeImage)
        holder.recipeNameTv.text = recipe.recipeName
        holder.recipeDurationTv.text = recipe.recipeDuration
    }

}
//    RecyclerView.Adapter<YourAdapterName.RecipeViewHolder>() {
//
//    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_horizontal, parent, false)
//        return RecipeViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
//        val recipe = recipeList[position]
//
//        // Bind data to the view holder's views
//        holder.itemView.itemImage.setImageResource(recipe.recipeImage)
//        holder.itemView.itemTitle.text = recipe.recipeName
//        holder.itemView.itemDuration.text = recipe.recipeDuration
//    }
//
//    override fun getItemCount(): Int {
//        return recipeList.size
//    }
//}
