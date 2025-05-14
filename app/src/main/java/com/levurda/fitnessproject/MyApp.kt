package com.levurda.fitnessproject

import android.app.Application
import android.content.SharedPreferences
import com.levurda.fitnessproject.utils.MainViewModel

class MyApp : Application() {
    lateinit var viewModel: MainViewModel
        private set

    override fun onCreate() {
        super.onCreate()

        viewModel = MainViewModel()  // Inicializace musí být zde, ne mimo konstruktor

        val prefs: SharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        val lastUser = prefs.getString("lastUser", null)

        viewModel.pref = prefs

        if (!lastUser.isNullOrBlank()) {
            viewModel.currentUsername = lastUser
            viewModel.loadDayList()
        }
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }

    init {
        instance = this
    }
}
