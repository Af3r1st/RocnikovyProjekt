package com.levurda.fitnessproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.levurda.fitnessproject.fragments.DaysFragment
import com.levurda.fitnessproject.models.User
import com.levurda.fitnessproject.storage.UserStorage
import com.levurda.fitnessproject.utils.FragmentManager
import com.levurda.fitnessproject.utils.MainViewModel

class MainActivity : AppCompatActivity() {

    private var currentUser: User? = null
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (application as MyApp).viewModel
        Log.d("MainActivity", "onCreate spuštěn")

        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val username = intent.getStringExtra("username")
        Log.d("MainActivity", "Username z intentu: $username")

        if (!username.isNullOrBlank()) {
            val users = UserStorage.getUsers(this)
            currentUser = users.find { it.username == username }

            Log.d("MainActivity", "Načtený uživatel: ${currentUser?.username}, isAdmin: ${currentUser?.isAdmin}")

            viewModel.currentUsername = username
            viewModel.pref = getSharedPreferences("prefs", MODE_PRIVATE)
            // ⚠️ NEvoláme viewModel.loadDayList(), protože už bylo voláno v MyApp.kt
        } else {
            Log.e("MainActivity", "username z intentu byl null nebo prázdný!")
        }

        if (savedInstanceState == null) {
            FragmentManager.setFragment(DaysFragment.newInstance(), this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (currentUser?.isAdmin != true) {
            menu?.removeItem(R.id.menu_admin_panel)
            menu?.removeItem(R.id.menu_user_list)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_admin_panel -> {
                startActivity(Intent(this, AdminPanelActivity::class.java))
                true
            }
            R.id.menu_user_list -> {
                startActivity(Intent(this, UserListActivity::class.java))
                true
            }
            R.id.menu_logout -> {
                // ✅ Odhlásit uživatele
                val viewModel = (application as MyApp).viewModel
                viewModel.pref?.edit()?.remove("lastUser")?.apply()
                viewModel.currentUsername = null

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            R.id.menu_reset_all ->{
                // Vymazání tréninků
                AlertDialog.Builder(this)
                    .setTitle("Reset tréninků")
                    .setMessage("Opravdu chceš smazat veškerý pokrok?")
                    .setPositiveButton("Ano") { _, _ ->
                        val viewModel = (application as MyApp).viewModel
                        viewModel.dayList.clear()
                        viewModel.saveDayList()
                        FragmentManager.setFragment(DaysFragment.newInstance(), this)
                    }
                    .setNegativeButton("Zrušit", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}