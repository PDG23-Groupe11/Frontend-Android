package ch.heigvd.pdg_grocerypal


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.pdg_grocerypal.databinding.ActivityMainBinding
import ch.heigvd.pdg_grocerypal.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferencesApp = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferencesApp.getString("auth_token", null)

        if (authToken != null) {
            // After successful login and API request, navigate to the main activity
            binding.button.visibility = View.GONE

            val sharedPreferencesUser = getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
            val surname = sharedPreferencesUser.getString("surname", "")

            // Display a welcome toast message
            Toast.makeText(this, "Welcome $surname!", Toast.LENGTH_SHORT).show()

            // Delay for 2 seconds before moving to NavigationActivity
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }

        // Add a button click listener to open the LoginActivity
        binding.button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

