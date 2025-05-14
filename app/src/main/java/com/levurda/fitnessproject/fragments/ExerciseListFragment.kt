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
import com.levurda.fitnessproject.adapters.ExerciseAdapter
import com.levurda.fitnessproject.databinding.ExerciseListFragmentBinding
import com.levurda.fitnessproject.utils.FragmentManager
import com.levurda.fitnessproject.utils.MainViewModel

class ExerciseListFragment : Fragment() {

    private lateinit var binding: ExerciseListFragmentBinding
    private lateinit var model: MainViewModel
    private lateinit var adapter: ExerciseAdapter
    private var ab: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = (requireActivity().application as MyApp).viewModel

        init()

        model.mutableListExercise.observe(viewLifecycleOwner) {
            if (model.currentUsername != null) {
                val doneIndex = model.getExerciseCount()
                for (i in 0 until doneIndex) {
                    if (i in it.indices) {
                        it[i] = it[i].copy(isDone = true)
                    }
                }
                adapter.submitList(it)
            } else {
                Log.e("ExerciseListFragment", "currentUsername je null → přeskočeno getExerciseCount")
            }
        }
    }

    private fun init() = with(binding) {
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.exercises)
        adapter = ExerciseAdapter(object : ExerciseAdapter.Listener {
            override fun onClick(day: DayModel) {
                // Místo pro budoucí funkce
            }
        })
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter
        bStart.setOnClickListener {
            FragmentManager.setFragment(
                WaitingFragment.newInstance(),
                activity as AppCompatActivity
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciseListFragment()
    }
}
