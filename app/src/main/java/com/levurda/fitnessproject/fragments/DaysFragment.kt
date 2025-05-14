package com.levurda.fitnessproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.levurda.fitnessproject.MyApp
import com.levurda.fitnessproject.R
import com.levurda.fitnessproject.adapters.DayModel
import com.levurda.fitnessproject.adapters.DaysAdapter
import com.levurda.fitnessproject.adapters.ExerciseModel
import com.levurda.fitnessproject.databinding.FragmentDaysBinding
import com.levurda.fitnessproject.utils.DialogManager
import com.levurda.fitnessproject.utils.FragmentManager
import com.levurda.fitnessproject.utils.MainViewModel

class DaysFragment : Fragment(), DaysAdapter.Listener {

    private lateinit var adapter: DaysAdapter
    private lateinit var binding: FragmentDaysBinding
    private var ab: ActionBar? = null

    // ✅ Používáme správný globální model z MyApp
    private lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = (requireActivity().application as MyApp).viewModel
        model.currentDay = 0
        initRcView()
    }

    private fun initRcView() = with(binding) {
        adapter = DaysAdapter(this@DaysFragment)
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.days)
        rcViewDays.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        rcViewDays.adapter = adapter

        if (!model.currentUsername.isNullOrEmpty()) {
            Log.d("DaysFragment", "currentUsername = ${model.currentUsername} → načítám dayList")
            adapter.submitList(fillDaysArray())
        } else {
            Log.e("DaysFragment", "currentUsername je null → NEVOLÁM fillDaysArray()")
        }
    }

    private fun fillDaysArray(): ArrayList<DayModel> {
        val exerciseArray = resources.getStringArray(R.array.day_exercise)

        model.loadDayList()

        if (model.dayList.isNotEmpty() && model.dayList.size == exerciseArray.size) {
            val daysDoneCounter = model.dayList.count { it.isDone }
            binding.pB.max = model.dayList.size
            updateRestDaysUI(model.dayList.size - daysDoneCounter, model.dayList.size)
            return ArrayList(model.dayList)
        }

        // Pokud je nový plán, nebo chybné uložené údaje
        model.dayList.clear()
        exerciseArray.forEachIndexed { index, exStr ->
            model.dayList.add(DayModel(exStr, index + 1, false))
        }
        model.saveDayList()

        val daysDoneCounter = model.dayList.count { it.isDone }
        binding.pB.max = model.dayList.size
        updateRestDaysUI(model.dayList.size - daysDoneCounter, model.dayList.size)

        return ArrayList(model.dayList)
    }

    private fun updateRestDaysUI(restDays: Int, days: Int) = with(binding) {
        val rDays = getString(R.string.rest) + " $restDays " + getString(R.string.days_left)
        tvRestDays.text = rDays
        pB.progress = days - restDays
    }

    private fun fillExerciseList(day: DayModel) {
        val tempList = ArrayList<ExerciseModel>()
        day.exercises.split(",").forEach {
            val exerciseList = resources.getStringArray(R.array.exercise)
            val exercise = exerciseList[it.toInt()]
            val exerciseArray = exercise.split("|")
            tempList.add(ExerciseModel(exerciseArray[0], exerciseArray[1], false, exerciseArray[2]))
        }
        model.mutableListExercise.value = tempList
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }

    override fun onClick(day: DayModel) {
        if (!day.isDone) {
            fillExerciseList(day)
            model.currentDay = day.dayNumber - 1
            FragmentManager.setFragment(
                ExerciseListFragment.newInstance(),
                activity as AppCompatActivity
            )
        } else {
            DialogManager.showDialog(
                activity as AppCompatActivity,
                R.string.reset_day_left_message,
                object : DialogManager.Listener {
                    override fun onClick() {
                        model.savePref((day.dayNumber - 1).toString(), 0)
                        fillExerciseList(day)
                        model.currentDay = day.dayNumber - 1
                        FragmentManager.setFragment(
                            ExerciseListFragment.newInstance(),
                            activity as AppCompatActivity
                        )
                    }
                }
            )
        }
    }
}
