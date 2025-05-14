package com.levurda.fitnessproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.currentDay = 0
        initRcView()

       /* requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.reset -> {
                        DialogManager.showDialog(
                            activity as AppCompatActivity,
                            R.string.reset_days_left_message,
                            object : DialogManager.Listener {
                                override fun onClick() {
                                    model.pref?.edit()?.clear()?.apply()
                                    adapter.submitList(fillDaysArray())
                                }
                            }
                        )
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)*/
    }

    private fun initRcView() = with(binding) {
        adapter = DaysAdapter(this@DaysFragment)
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.days)
        rcViewDays.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        rcViewDays.adapter = adapter
        adapter.submitList(fillDaysArray())
    }

    private fun fillDaysArray(): ArrayList<DayModel> {
        val exerciseArray = resources.getStringArray(R.array.day_exercise)

        if (model.dayList.isEmpty() || model.dayList.size != exerciseArray.size) {
            model.dayList.clear()
            exerciseArray.forEachIndexed { index, exStr ->
                model.dayList.add(DayModel(exStr, index + 1, false))
            }
            model.saveDayList()
        }

        // UI
        val daysDoneCounter = model.dayList.count { it.isDone }
        binding.pB.max = model.dayList.size
        updateRestDaysUI(model.dayList.size - daysDoneCounter, model.dayList.size)

        return ArrayList(model.dayList) // Kopie, abys neměnil originál, pokud bys ho někde přepsal
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
            model.currentDay = day.dayNumber - 1 // 🔧 OPRAVA - denNumber je 1-based
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
                        model.savePref((day.dayNumber - 1).toString(), 0) // 🔧 OPRAVA
                        fillExerciseList(day)
                        model.currentDay = day.dayNumber - 1 // 🔧 OPRAVA
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
