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

/**
 * Adaptateur personnalisé pour afficher une liste de recettes horizontalement dans un RecyclerView.
 */
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
        // Url pour la récupération des images
        val BASE_URL = Configuration.BaseURL
        val urlString = BASE_URL + "/static/recipeImages/" + recipe.id.toString()


        holder.recipeImageView.setImageResource(R.drawable.image_placeholder)
        holder.recipeNameTv.text = recipe.name
        holder.recipeDurationTv.text = recipe.prep_time + " min"

        val args = Bundle().apply {
            putParcelable("recipe", recipe)
            putInt("imagePlaceholder", R.drawable.image_placeholder)
        }

        val recipeDetailsFragment = RecipeDetailsFragment()
        recipeDetailsFragment.arguments = args

        val imgWidth = holder.recipeImageView.layoutParams.width
        val imgHeight = holder.recipeImageView.layoutParams.height

        // Tente de récupérer l'image de la recette sur la back-end si existante
        Picasso.get()
            .load(urlString)
            .placeholder(R.drawable.image_placeholder)
            .resize(imgWidth, imgHeight)
            .centerCrop()
            .into(holder.recipeImageView)

        holder.clickableOverlay.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            navController.popBackStack()
            navController.navigate(R.id.recipeDetailsFragment, args)
        }
    }
}
