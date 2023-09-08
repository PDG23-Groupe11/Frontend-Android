package ch.heigvd.pdg_grocerypal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.data.model.UserInfos
import ch.heigvd.pdg_grocerypal.databinding.ActivityMainBinding
import ch.heigvd.pdg_grocerypal.ui.login.LoginActivity

/**
 * L'activité principale de l'application.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtient les préférences partagées de l'application
        val sharedPreferencesApp = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        // Obtient le token d'authentification depuis les préférences partagées
        val authToken = sharedPreferencesApp.getString("auth_token", null)

        var usersInfo: UserInfos

        if (authToken != null) {

            binding.button.visibility = View.GONE

            // Récupère les informations de l'utilisateur depuis une API
            ConnectionRecipeUtils.fetchUserInfos(
                authToken,
                onSuccess = { retrievedInfos ->
                    usersInfo = retrievedInfos
                    val surname = usersInfo.name
                    Toast.makeText(this, "Bienvenue $surname !", Toast.LENGTH_SHORT).show()
                },
                onError = { errorMessage ->
                    Log.e("MainAcivity", "Erreur : $errorMessage")
                }
            )

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)

        } else {
            Log.e("MainActivity", "Aucun token trouvé")
        }

        // Bouton permettant de naviguer vers la page de login
        binding.button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
