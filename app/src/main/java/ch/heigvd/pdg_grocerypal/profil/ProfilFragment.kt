package ch.heigvd.pdg_grocerypal.profil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.databinding.FragmentProfilBinding
import ch.heigvd.pdg_grocerypal.recipes.RecipeAdapterVertical

class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
    private lateinit var adapter: SpecialDietAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = SpecialDietAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        binding.minusButton.setOnClickListener {
            val currentQuantity = binding.quantityEditText.text.toString().toIntOrNull() ?: 0
            if (currentQuantity > 1) {
                binding.quantityEditText.setText((currentQuantity - 1).toString())
            }
        }

        binding.plusButton.setOnClickListener {
            val currentQuantity = binding.quantityEditText.text.toString().toIntOrNull() ?: 0
            binding.quantityEditText.setText((currentQuantity + 1).toString())
        }

        return view;
    }
}