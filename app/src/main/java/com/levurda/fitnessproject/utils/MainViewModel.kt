package com.levurda.fitnessproject.utils

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.levurda.fitnessproject.adapters.DayModel
import com.levurda.fitnessproject.adapters.ExerciseModel

class MainViewModel : ViewModel() {

    val mutableListExercise = MutableLiveData<ArrayList<ExerciseModel>>()
    var pref: SharedPreferences? = null
    var currentUsername: String? = null
    var currentDay = 0
    var dayList: ArrayList<DayModel> = ArrayList()

    fun markDayCompleted(dayIndex: Int) {
        if (dayIndex in dayList.indices) {
            dayList[dayIndex].isDone = true
            Log.d("MainViewModel", "markDayCompleted: den $dayIndex označen jako hotový")
            saveDayList()
        } else {
            Log.e("MainViewModel", "markDayCompleted: index $dayIndex mimo rozsah")
        }
    }

    fun savePref(key: String, value: Int) {
        if (currentUsername == null) {
            Log.e("MainViewModel", "Nelze uložit pref bez uživatele")
            return
        }
        val editor = pref?.edit()
        val scopedKey = scopedKey(key)
        editor?.putInt(scopedKey, value)?.apply()
    }

    fun getExerciseCount(): Int {
        if (currentUsername == null) {
            Log.e("MainViewModel", "Nelze načíst exerciseCount bez uživatele")
            return 0
        }
        return pref?.getInt(scopedKey(currentDay.toString()), 0) ?: 0
    }

    fun getPref(key: String): Int? {
        if (currentUsername == null) {
            Log.e("MainViewModel", "Nelze načíst pref bez uživatele")
            return null
        }
        return pref?.getInt(scopedKey(key), 0)
    }

    fun saveDayList() {
        if (currentUsername == null) {
            Log.e("DEBUG_SAVE", "currentUsername je null → neukládám dayList!")
            return
        }
        val key = scopedKey("dayList")
        val json = Gson().toJson(dayList)
        Log.d("DEBUG_SAVE", "Ukládám do $key → $json")
        pref?.edit()?.putString(key, json)?.apply()
    }


    fun loadDayList() {
        if (currentUsername == null) {
            Log.e("DEBUG_LOAD", "currentUsername je null → nenačítám dayList!")
            return
        }
        val key = scopedKey("dayList")
        val json = pref?.getString(key, null)
        Log.d("DEBUG_LOAD", "Načítám z $key → $json")
        if (json != null) {
            val type = object : TypeToken<ArrayList<DayModel>>() {}.type
            dayList = Gson().fromJson(json, type)
        }
    }

    private fun scopedKey(key: String): String {
        val finalKey = "${currentUsername ?: "default"}_$key"
        Log.d("DEBUG_KEY", "Používám scopedKey: $finalKey")
        return finalKey
    }
}
