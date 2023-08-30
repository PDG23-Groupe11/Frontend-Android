package ch.heigvd.pdg_grocerypal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import ch.heigvd.pdg_grocerypal.databinding.ActivityMainBinding
import ch.heigvd.pdg_grocerypal.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Your existing code for the main activity

        // Add a button click listener to open the LoginActivity
        binding.button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}