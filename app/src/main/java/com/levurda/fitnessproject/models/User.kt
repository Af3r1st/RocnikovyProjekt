package com.levurda.fitnessproject.models

data class User(
    val email: String,
    val username: String,
    val passwordHash: String,
    val isAdmin: Boolean = false
)
