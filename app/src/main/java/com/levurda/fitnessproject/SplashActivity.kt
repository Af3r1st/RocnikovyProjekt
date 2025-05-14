package com.levurda.fitnessproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.levurda.fitnessproject.utils.MainViewModel

class SplashActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // ✅ Inicializace pref a načtení uloženého seznamu
        model.pref = getSharedPreferences("my_prefs", MODE_PRIVATE)
        model.loadDayList() // 👈 tohle bylo potřeba přidat

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
