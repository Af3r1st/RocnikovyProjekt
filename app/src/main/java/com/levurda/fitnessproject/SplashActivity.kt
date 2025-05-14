package com.levurda.fitnessproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.levurda.fitnessproject.storage.UserStorage

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val lastUsername = prefs.getString("lastUser", null)
        val users = UserStorage.getUsers(this)

        val viewModel = (application as MyApp).viewModel
        viewModel.pref = prefs

        Handler(Looper.getMainLooper()).postDelayed({
            if (lastUsername != null && users.any { it.username == lastUsername }) {
                viewModel.currentUsername = lastUsername
                viewModel.loadDayList() // 🧠 musí být až po nastavení username

                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtra("username", lastUsername)
                })
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000)
    }
}
