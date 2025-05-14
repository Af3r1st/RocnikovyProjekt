package com.levurda.fitnessproject.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.levurda.fitnessproject.models.User
import java.io.File

object UserStorage {
    private const val FILE_NAME = "users.json"
    private val gson = Gson()

    fun getUsers(context: Context): MutableList<User> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            return mutableListOf()
        }

        val json = file.readText()
        val type = object : TypeToken<MutableList<User>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun saveUsers(context: Context, users: List<User>) {
        val file = File(context.filesDir, FILE_NAME)
        val json = gson.toJson(users)
        file.writeText(json)
    }

    fun addUser(context: Context, user: User): Boolean {
        val users = getUsers(context)

        // kontrola duplicity uživatelského jména
        if (users.any { it.username == user.username }) return false

        // Pokud je první uživatel v systému → automaticky admin
        val isAdmin = users.isEmpty()
        val userWithRole = user.copy(isAdmin = isAdmin)

        users.add(userWithRole)
        saveUsers(context, users)
        return true
    }


    fun validateUser(context: Context, username: String, passwordHash: String): User? {
        val users = getUsers(context)
        return users.find { it.username == username && it.passwordHash == passwordHash }
    }
}
