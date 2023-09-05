package ch.heigvd.pdg_grocerypal.recipes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem


class RecipeAdapterHorizontal(private val recipeList: List<RecipeCard>) :
    RecyclerView.Adapter<RecipeAdapterHorizontal.RecipeViewHolder>(){

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView : ImageView = itemView.findViewById(R.id.itemImage)
        val recipeNameTv : TextView = itemView.findViewById(R.id.itemTitle)
        val recipeDurationTv : TextView = itemView.findViewById(R.id.itemDuration)
        val clickableOverlay : CardView = itemView.findViewById(R.id.cardView)
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


        holder.recipeImageView.setImageResource(R.drawable.image_placeholder)
        holder.recipeNameTv.text = recipe.name
        holder.recipeDurationTv.text = recipe.prep_time + " min"

        val args = Bundle().apply {
            putParcelable("recipe", recipe)
            putInt("imagePlaceholder", R.drawable.image_placeholder)
        }

        val recipeDetailsFragment = RecipeDetailsFragment()
        recipeDetailsFragment.arguments = args

        holder.clickableOverlay.setOnClickListener {


            val navController = Navigation.findNavController(holder.itemView)
            navController.popBackStack()
            navController.navigate(R.id.recipeDetailsFragment, args)
        }
    }
}
