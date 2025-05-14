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
        pref?.edit()?.putInt(key, value)?.apply()
    }

    fun getExerciseCount(): Int {
        return pref?.getInt(currentDay.toString(), 0) ?: 0
    }

    fun getPref(key: String): Int? {
        return pref?.getInt(key, 0)
    }

    fun saveDayList() {
        val editor = pref?.edit()
        val json = Gson().toJson(dayList)
        val key = "dayList_${currentUsername ?: "default"}"
        Log.d("MainViewModel", "Ukládám dayList pro $key: $json")
        editor?.putString(key, json)
        editor?.apply()
    }



    fun loadDayList() {
        val key = "dayList_${currentUsername ?: "default"}"
        val json = pref?.getString(key, null)
        Log.d("MainViewModel", "Načítám dayList pro $key: $json")
        if (json != null) {
            val type = object : TypeToken<ArrayList<DayModel>>() {}.type
            dayList = Gson().fromJson(json, type)
        }
    }


}
