package ch.heigvd.pdg_grocerypal.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var groceryList: MutableList<GroceryItem>
    private lateinit var adapter: GroceryListAdapter

    private fun updateEmptyListMessageVisibility() {
        val emptyListMessage = binding.emptyListMessage

        if (groceryList.isEmpty()) {
            emptyListMessage.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            emptyListMessage.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        groceryList = mutableListOf(
            GroceryItem("Farine", "g", "100"),
            GroceryItem("Lait", "l", "4"),
            GroceryItem("Oeuf", "pcs", "6"),
            GroceryItem("Chocolat noir", "g", "200"),
            GroceryItem("Chocolat au lait", "g", "200"),
            )

        adapter = GroceryListAdapter(groceryList, parentFragmentManager)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        updateEmptyListMessageVisibility()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deletePurchasedButton.setOnClickListener {
            val iterator = groceryList.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (item.isPurchased) {
                    iterator.remove()
                }
            }
            updateEmptyListMessageVisibility()
            view.post {
                adapter.notifyDataSetChanged()
            }
        }

    }
}
