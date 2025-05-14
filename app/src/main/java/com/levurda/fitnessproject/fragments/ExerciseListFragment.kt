package com.levurda.fitnessproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.levurda.fitnessproject.R
import com.levurda.fitnessproject.adapters.DayModel
import com.levurda.fitnessproject.adapters.DaysAdapter
import com.levurda.fitnessproject.adapters.ExerciseAdapter
import com.levurda.fitnessproject.databinding.ExerciseListFragmentBinding
import com.levurda.fitnessproject.databinding.FragmentDaysBinding
import com.levurda.fitnessproject.utils.FragmentManager
import com.levurda.fitnessproject.utils.MainViewModel


class ExerciseListFragment : Fragment() {

    private lateinit var binding: ExerciseListFragmentBinding
    private var ab: ActionBar? =  null
    private val model: MainViewModel by activityViewModels()
    private lateinit var adapter: ExerciseAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseListFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        model.mutableListExercise.observe(viewLifecycleOwner) {
            val doneIndex = model.getExerciseCount()
            for (i in 0 until doneIndex) {
                it[i] = it[i].copy(isDone = true)
            }
            adapter.submitList(it)
        }

    }

    private fun init() = with(binding) {
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.exercises)
        adapter = ExerciseAdapter(object : ExerciseAdapter.Listener {
            override fun onClick(day: DayModel) {
                // Tady zatím nic – můžeš později doplnit akci
            }
        })
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter
        bStart.setOnClickListener{
            FragmentManager.setFragment(WaitingFragment.newInstance(),activity as AppCompatActivity)
        }
    }




    companion object {

        @JvmStatic
        fun newInstance() = ExerciseListFragment()
    }
}