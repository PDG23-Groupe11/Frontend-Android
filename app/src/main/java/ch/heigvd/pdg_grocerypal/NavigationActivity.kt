package ch.heigvd.pdg_grocerypal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ch.heigvd.pdg_grocerypal.databinding.AppNavigationLayoutBinding

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AppNavigationLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize NavController
        val navController = findNavController(R.id.nav_host_fragment)

        // Set up BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}



