package ch.heigvd.pdg_grocerypal.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem

class LittleListAdapter(private val groceryList: List<GroceryItem>) :
       RecyclerView.Adapter<LittleListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemDetails: TextView = itemView.findViewById(R.id.itemDetails)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_little_grocery, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val groceryItem = groceryList[position]
        holder.itemDetails.text = "${groceryItem.quantity} ${groceryItem.unit} ${groceryItem.name}"
    }

    override fun getItemCount() = groceryList.size
}