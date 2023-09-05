package ch.heigvd.pdg_grocerypal.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.Ingredient

class IngredientAdapter(private val ingredientList: List<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.ViewHolder>(), Filterable {

    private var filteredIngredientList: List<Ingredient> = ingredientList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.itemDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_little_grocery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = filteredIngredientList[position]
        holder.ingredientName.text = ingredient.name
    }

    override fun getItemCount() = filteredIngredientList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().toLowerCase().trim()
                val filteredList = if (query.isEmpty()) {
                    ingredientList
                } else {
                    ingredientList.filter { ingredient ->
                        ingredient.name.toLowerCase().contains(query)
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredIngredientList = results?.values as List<Ingredient>
                notifyDataSetChanged()
            }
        }
    }
}
