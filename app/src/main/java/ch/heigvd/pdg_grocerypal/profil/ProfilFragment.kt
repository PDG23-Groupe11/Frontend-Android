package ch.heigvd.pdg_grocerypal.profil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.pdg_grocerypal.MainActivity
import ch.heigvd.pdg_grocerypal.NavigationActivity
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.data.model.UserInfos
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

        var userInfos = retrieveLocalUserInfos(requireContext())
        var currentQuantity = 1

        if (userInfos != null) {
                currentQuantity = userInfos.nbPerHome
        }
        binding.quantityEditText.setText((currentQuantity).toString())


        binding.logoutButton.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("auth_token", null)  // Set the value to null
            editor.apply()

            val sharedPreferencesUser = requireActivity().getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
            val editorUser = sharedPreferencesUser.edit()
            editorUser.putString("name", null)
            editorUser.putString("surname", null)
            editorUser.putInt("nbPerHome", 0)
            editorUser.putString("email", null)
            editorUser.apply()

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }


        binding.minusButton.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity -= 1
                binding.quantityEditText.setText((currentQuantity).toString())

                if (userInfos != null) {
                    updateUserInfos(userInfos, currentQuantity, requireContext())
                }

            }
        }

        binding.plusButton.setOnClickListener {

            currentQuantity += 1
            binding.quantityEditText.setText((currentQuantity).toString())


            if (userInfos != null) {
                if (userInfos != null) {
                    updateUserInfos(userInfos, currentQuantity, requireContext())
                }
            }
        }
        return view;
    }
}

fun retrieveLocalUserInfos(context: Context): UserInfos? {
    val sharedPreferences = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)

    try {
        val firstname = sharedPreferences.getString("name", null)?: throw IllegalArgumentException("No firstname found")
        val surname = sharedPreferences.getString("surname", null)?: throw IllegalArgumentException("No surname found")
        val nbPerHome = sharedPreferences.getInt("nbPerHome", 0)
        if (nbPerHome < 1) {
            throw IllegalArgumentException("Illegal nbPerHome value")
        }
        val email = sharedPreferences.getString("email", null)?: throw IllegalArgumentException("No email found")

        return UserInfos(firstname, surname, nbPerHome, email)

    } catch (e: Exception) {
        Log.e("Exeption in retrieveLocalUserInfos", e.toString())
    }
    return null
}

fun updateUserInfos(userInfos: UserInfos, currentQuantity: Int, context: Context) {

    val sharedPreferencesUser = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferencesUser.edit()
    editor.putInt("nbPerHome", currentQuantity)
    editor.apply()

    userInfos.nbPerHome = currentQuantity
    val sharedPreferencesToken = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val authToken = sharedPreferencesToken?.getString("auth_token", null)


    if (authToken != null) {
        ConnectionRecipeUtils.postUserInfos(
            authToken,
            userInfos,
            onSuccess = {
            },
            onError = { errorMessage ->
                ConnectionRecipeUtils.showError(errorMessage)
            }
        )
    }
}




