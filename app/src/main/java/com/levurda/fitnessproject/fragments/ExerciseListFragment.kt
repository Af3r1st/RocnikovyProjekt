package com.levurda.fitnessproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.levurda.fitnessproject.R
import com.levurda.fitnessproject.adapters.DayModel
import com.levurda.fitnessproject.adapters.DaysAdapter
import com.levurda.fitnessproject.databinding.ExerciseListFragmentBinding
import com.levurda.fitnessproject.databinding.FragmentDaysBinding


class ExerciseListFragment : Fragment() {

    private lateinit var binding: ExerciseListFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseListFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    companion object {

        @JvmStatic
        fun newInstance() = ExerciseListFragment()
    }
}