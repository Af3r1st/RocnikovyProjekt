package com.levurda.fitnessproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.levurda.fitnessproject.models.User
import com.levurda.fitnessproject.storage.UserStorage
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var submitButton: Button
    private lateinit var modeToggle: TextView
    private var isSignUpMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.emailInput)
        usernameField = findViewById(R.id.usernameInput)
        passwordField = findViewById(R.id.passwordInput)
        submitButton = findViewById(R.id.submitButton)
        modeToggle = findViewById(R.id.toggleMode)

        updateForm()

        modeToggle.setOnClickListener {
            isSignUpMode = !isSignUpMode
            updateForm()
        }

        submitButton.setOnClickListener {
            val email = emailField.text.toString()
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            val hashed = hashPassword(password)

            if (isSignUpMode) {
                if (email.isBlank() || username.isBlank() || password.isBlank()) {
                    showToast("Vyplň všechna pole")
                    return@setOnClickListener
                }

                val user = User(email, username, hashed)
                if (UserStorage.addUser(this, user)) {
                    showToast("Registrace úspěšná")
                    loginSuccess(username)
                } else {
                    showToast("Uživatelské jméno již existuje")
                }
            } else {
                val user = UserStorage.validateUser(this, username, hashed)
                if (user != null) {
                    showToast("Přihlášení úspěšné")
                    loginSuccess(user.username)
                } else {
                    showToast("Neplatné přihlašovací údaje")
                }
            }
        }
    }

    private fun updateForm() {
        emailField.visibility = if (isSignUpMode) EditText.VISIBLE else EditText.GONE
        modeToggle.text = if (isSignUpMode) "Máš účet? Přihlas se" else "Nemáš účet? Zaregistruj se"
        submitButton.text = if (isSignUpMode) "Zaregistrovat se" else "Přihlásit se"
    }


     private fun loginSuccess(username: String) {
        val viewModel = (application as MyApp).viewModel // ✅ správná globální instance

        viewModel.currentUsername = username
        viewModel.pref = getSharedPreferences("prefs", MODE_PRIVATE) // 🧠 klíčové!
        viewModel.loadDayList()

        viewModel.pref?.edit()?.putString("lastUser", username)?.apply()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }



    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
