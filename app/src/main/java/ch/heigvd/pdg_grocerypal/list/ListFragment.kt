package ch.heigvd.pdg_grocerypal.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
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
    fun updateGroceryList(newGroceryList: MutableList<GroceryItem>) {
        groceryList.clear()
        groceryList.addAll(newGroceryList)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        val dbHelper = GroceryPalDBHelper(requireContext())
        groceryList = dbHelper.getAllShoppingListItems()

        updateEmptyListMessageVisibility()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroceryListAdapter(requireContext(), groceryList, parentFragmentManager)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)


        binding.deletePurchasedButton.setOnClickListener {
            val dbHelper = GroceryPalDBHelper(requireContext())
            dbHelper.deleteAllPurchasedItems()
            updateGroceryList(dbHelper.getAllShoppingListItems())
            view.post {
                adapter.notifyDataSetChanged()
            }
        }

    }
}
