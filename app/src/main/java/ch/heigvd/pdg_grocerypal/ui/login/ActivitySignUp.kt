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
import ch.heigvd.pdg_grocerypal.R
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.data.model.UserData

class ActivitySignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signUpButton = findViewById<Button>(R.id.SignUp_Button)
        val nameEditText = findViewById<EditText>(R.id.nomEt)
        val surnameEditText = findViewById<EditText>(R.id.prenomEt)
        val emailEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val minusButton = findViewById<Button>(R.id.minusButton)
        val plusButton = findViewById<Button>(R.id.plusButton)
        val quantityEditText = findViewById<EditText>(R.id.quantityEditText)

        nameEditText.doAfterTextChanged { updateSignUpButtonState() }
        surnameEditText.doAfterTextChanged { updateSignUpButtonState() }
        emailEditText.doAfterTextChanged { updateSignUpButtonState() }
        passwordEditText.doAfterTextChanged { updateSignUpButtonState() }


        minusButton.setOnClickListener {
            val currentQuantity = quantityEditText.text.toString().toIntOrNull() ?: 0
            if (currentQuantity > 1) {
                quantityEditText.setText((currentQuantity - 1).toString())
            }
        }

        plusButton.setOnClickListener {
            val currentQuantity = quantityEditText.text.toString().toIntOrNull() ?: 0
            quantityEditText.setText((currentQuantity + 1).toString())
        }



        signUpButton.setOnClickListener {
            // Get user input from EditText fields
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val nbPerHome = quantityEditText.text.toString().toIntOrNull() ?: 0

            // Create a UserData object with the user's data
            val userData = UserData(name, surname, nbPerHome, email, password)

            // Call the createAccount function from your ConnectionRecipeUtils object
            ConnectionRecipeUtils.createAccount(userData,
                onSuccess = {  ->
                    // Handle successful sign-up

                    Log.d("ActivitySignUp", "SuccesfulCreation")
                    Toast.makeText(this, "Account succesfully created", Toast.LENGTH_SHORT).show()

                    // Save user data in SharedPreferences
                    val sharedPreferences = getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("name", userData.firstname)
                    editor.putString("surname", userData.name)
                    editor.putInt("nbPerHome", userData.nbPerHome)
                    editor.putString("email", userData.email)
                    editor.apply()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                },
                onError = {  ->
                    // Handle sign-up error
                    Toast.makeText(this, "didn't work", Toast.LENGTH_SHORT).show()
                }
            )
        }

    }

    private fun updateSignUpButtonState() {
        val nameEditText = findViewById<EditText>(R.id.nomEt)
        val surnameEditText = findViewById<EditText>(R.id.prenomEt)
        val emailEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val signUpButton = findViewById<Button>(R.id.SignUp_Button)

        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        // Enable the sign-up button if all fields are filled
        signUpButton.isEnabled = name.isNotBlank() && surname.isNotBlank() && email.isNotBlank() && password.isNotBlank()
    }
}


