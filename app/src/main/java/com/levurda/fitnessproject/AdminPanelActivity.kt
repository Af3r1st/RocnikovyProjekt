package com.levurda.fitnessproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminPanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        Toast.makeText(this, "Admin panel – funkce zatím nejsou aktivní", Toast.LENGTH_SHORT).show()
    }
}
