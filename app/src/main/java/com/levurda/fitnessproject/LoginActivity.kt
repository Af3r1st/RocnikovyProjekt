package com.levurda.fitnessproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.levurda.fitnessproject.models.User
import com.levurda.fitnessproject.storage.UserStorage
import com.levurda.fitnessproject.utils.MainViewModel
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var modeToggle: TextView
    private lateinit var emailField: EditText
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var submitButton: Button
    private val viewModel: MainViewModel by lazy { MainViewModel() }
    private var isSignUpMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        modeToggle = findViewById(R.id.toggleMode)
        emailField = findViewById(R.id.emailInput)
        usernameField = findViewById(R.id.usernameInput)
        passwordField = findViewById(R.id.passwordInput)
        submitButton = findViewById(R.id.submitButton)

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

            Log.d("LoginActivity", "Klik na submit. Mód: ${if (isSignUpMode) "SignUp" else "SignIn"}")

            if (isSignUpMode) {
                if (email.isBlank() || username.isBlank() || password.isBlank()) {
                    showToast("Vyplň všechna pole")
                    return@setOnClickListener
                }

                val user = User(email, username, hashed)
                val success = UserStorage.addUser(this, user)

                if (success) {
                    Log.d("LoginActivity", "Registrace úspěšná pro: $username")
                    showToast("Registrace úspěšná")
                    loginSuccess(username)
                } else {
                    showToast("Uživatelské jméno již existuje")
                }
            } else {
                val user = UserStorage.validateUser(this, username, hashed)
                Log.d("LoginActivity", "Přihlášení: ${user?.username}, admin: ${user?.isAdmin}")
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
        viewModel.currentUsername = username
        viewModel.loadDayList()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
