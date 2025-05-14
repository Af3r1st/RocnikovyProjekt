package com.levurda.fitnessproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        Toast.makeText(this, "Seznam uživatelů – funkce se připravují", Toast.LENGTH_SHORT).show()
    }
}
