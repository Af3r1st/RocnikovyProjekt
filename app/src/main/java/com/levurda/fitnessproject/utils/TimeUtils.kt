package com.levurda.fitnessproject.utils

import android.annotation.SuppressLint
import android.icu.util.Calendar
import java.text.SimpleDateFormat


object TimeUtils {
    @SuppressLint("SimpleDateFormat")
    val formatter = SimpleDateFormat("mm:ss")
    fun  getTime(time: Long): String{
        val cv = Calendar.getInstance()
        cv.timeInMillis = time
        return formatter.format(cv.time)
    }
}