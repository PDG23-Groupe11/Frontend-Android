package ch.heigvd.pdg_grocerypal.list

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.data.model.Ingredient

/**
 * Gestion des ingrédients lors de la tentative d'ajout d'un nouvel ingrédient à la liste
 */
class IngredientAdapter(private val context: Context, private val ingredientList: List<Ingredient>) :
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

        // Récupération et affichage des informations de l'ingrédient
        val ingredient = filteredIngredientList[position]
        holder.ingredientName.text = ingredient.name

        // Ouverture d'une fenêtre lors d'un appuie sur un ingrédient
        holder.ingredientName.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
            val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_add_ingredient, null)
            val editTextQuantity = dialogLayout.findViewById<EditText>(R.id.editTextQuantity)
            val spinnerUnit = dialogLayout.findViewById<Spinner>(R.id.spinnerUnit)

            // Récupération des unités existants dans la DB
            val dbHelper = GroceryPalDBHelper(context)
            val unitOptions = dbHelper.getAllUnits()
            val unitAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, unitOptions.map { it.name })
            spinnerUnit.adapter = unitAdapter

            alertDialogBuilder
                .setView(dialogLayout)
                .setTitle("Ajout de ${ingredient.name}")
                // Action lors que le bouton "Ajouter" est appuyé
                .setPositiveButton("Ajouter") { dialog, _ ->
                    val selectedUnitName = spinnerUnit.selectedItem.toString()
                    val selectedQuantity = editTextQuantity.text.toString().toIntOrNull() ?: 0

                    // Vérifie que la quantité indiquée est un positive
                    if (selectedQuantity >= 1) {
                        val selectedUnit = unitOptions.find { it.name == selectedUnitName }
                        // Vérifie que l'unité sélectionnée existe
                        if (selectedUnit != null) {
                            // Ajout de l'ingrédient à la liste
                            dbHelper.addOrUpdateShoppingListItem(context, ingredient.id, selectedUnit.id, selectedQuantity)
                            Toast.makeText(context, "Ajout de $selectedQuantity ${selectedUnit.name} ${ingredient.name}", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(context, "Unité invalide", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "La quantité doit être supérieure ou égale à 1", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                } // Action lorsque le bouton "Annuler" est appuyer
                .setNegativeButton("Annuler") { dialog, _ ->
                    dialog.dismiss()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
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
