package ch.heigvd.pdg_grocerypal.profil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R


class SpecialDietAdapter() :
    RecyclerView.Adapter<SpecialDietAdapter.ViewHolder>() {
    val specialDietList = listOf("vegan", "sans gluten")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val specialDiet: TextView = itemView.findViewById(R.id.itemDetails)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_igredient_recipe_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.specialDiet.text = specialDietList[position]
    }

    override fun getItemCount() = specialDietList.size
}