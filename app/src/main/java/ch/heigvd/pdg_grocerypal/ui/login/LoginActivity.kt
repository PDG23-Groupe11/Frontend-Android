package ch.heigvd.pdg_grocerypal.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import ch.heigvd.pdg_grocerypal.NavigationActivity
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.SQLite_localDB.GroceryPalDBHelper
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.data.model.Credentials
import ch.heigvd.pdg_grocerypal.data.model.In_Shopping_List
import ch.heigvd.pdg_grocerypal.data.model.TokenResponse

class LoginActivity : AppCompatActivity() {

    private lateinit var shoppingList: MutableList<In_Shopping_List>
    private lateinit var dbHelper: GroceryPalDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInButton = findViewById<Button>(R.id.login)
        val signUpButton = findViewById<Button>(R.id.sign_up)
        val emailEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        var credentials: Credentials
        shoppingList = mutableListOf()

        dbHelper = GroceryPalDBHelper(this)

        // Initially, disable the "Login" button
        signInButton.isEnabled = false

        // Add text change listeners to the email and password fields
        emailEditText.doAfterTextChanged { updateLoginButtonState() }
        passwordEditText.doAfterTextChanged { updateLoginButtonState() }

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            credentials = Credentials(email, password)

            // Check if email and password are not blank
            if (email.isNotBlank() && password.isNotBlank()) {
                // Attempt to log in
                ConnectionRecipeUtils.login(
                    credentials,
                    onSuccess = { tokenResponse ->
                        // Login successful, token is already saved in SharedPreferences

                        val authToken = tokenResponse.token
                        Log.d("AuthToken", "Received token: $authToken")

                        val tokenResponse = TokenResponse(authToken)

                        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("auth_token", tokenResponse.token)
                        editor.apply()


                        // After successful login and API request, navigate to the main activity
                        val intent = Intent(this, NavigationActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onError = { errorMessage ->
                        // Handle login error (e.g., display error message)
                        Log.e("Login", "Login error: $errorMessage")
                    }
                )
            } else {
                // Handle case where email or password is blank (e.g., show an error message)
                Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show()
                Log.e("Login", "Email and password are required")
            }
        }



        signUpButton.setOnClickListener {
            // Navigate to the SignUpActivity
            val intent = Intent(this, ActivitySignUp::class.java)
            startActivity(intent)
        }
    }
    private fun updateLoginButtonState() {
        val emailEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val signInButton = findViewById<Button>(R.id.login)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        // Enable the "Login" button if both email and password fields are not blank
        signInButton.isEnabled = email.isNotBlank() && password.isNotBlank()
    }


}
