package ch.heigvd.pdg_grocerypal.recipes

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
import ch.heigvd.pdg_grocerypal.config.Configuration
import com.squareup.picasso.Picasso


class RecipeAdapterVertical(private val recipeList: List<RecipeCard>) :
    RecyclerView.Adapter<RecipeAdapterVertical.RecipeViewHolder>(){

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView : ImageView = itemView.findViewById(R.id.itemImage)
        val recipeNameTv : TextView = itemView.findViewById(R.id.itemTitle)
        val recipeDurationTv : TextView = itemView.findViewById(R.id.itemDuration)
        val clickableOverlay: CardView = itemView.findViewById(R.id.cardViewVertical)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vertical_recipe, parent, false)
        return  RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        val BASE_URL = Configuration.BaseURL
        val urlString = BASE_URL + "/static/recipeImages/" + recipe.id.toString()

        holder.recipeImageView.setImageResource(R.drawable.image_placeholder)
        holder.recipeNameTv.text = recipe.name
        holder.recipeDurationTv.text = recipe.prep_time + " min"


        val args = Bundle().apply {
            putParcelable("recipe", recipe)
            putInt("imagePlaceholder", R.drawable.image_placeholder)
        }


        val imgWidth = holder.recipeImageView.layoutParams.width
        val imgHeight = holder.recipeImageView.layoutParams.height


        Picasso.get()
            .load(urlString) // Replace imageUrl with the URL of the image
            .placeholder(R.drawable.image_placeholder) // Optional: Set a placeholder drawable while the image is loading
            .resize(imgWidth, imgHeight) // Optional: Resize the image to specific dimensions
            .centerCrop() // Optional: Crop the image to fit the ImageView dimensions
            .into(holder.recipeImageView) // Your ImageView





        val recipeDetailsFragment = RecipeDetailsFragment()
        recipeDetailsFragment.arguments = args

        holder.clickableOverlay.setOnClickListener {


            val navController = Navigation.findNavController(holder.itemView)
            navController.popBackStack()
            // set bottom navigation view manually to recipe
            navController.navigate(R.id.recipesFragment)
            navController.navigate(R.id.recipeDetailsFragment, args)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

}