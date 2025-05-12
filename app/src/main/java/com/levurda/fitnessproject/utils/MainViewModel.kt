package com.levurda.fitnessproject.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.levurda.fitnessproject.adapters.ExerciseModel

class MainViewModel : ViewModel(){
    val mutableListExercise = MutableLiveData<ArrayList<ExerciseModel>>()

}