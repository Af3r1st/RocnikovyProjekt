package com.levurda.fitnessproject.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.levurda.fitnessproject.MyApp
import com.levurda.fitnessproject.R
import com.levurda.fitnessproject.databinding.WaitingFragmentBinding
import com.levurda.fitnessproject.utils.FragmentManager
import com.levurda.fitnessproject.utils.MainViewModel
import com.levurda.fitnessproject.utils.TimeUtils

class WaitingFragment : Fragment() {

    private lateinit var binding: WaitingFragmentBinding
    private lateinit var viewModel: MainViewModel
    private var ab: ActionBar? = null
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WaitingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity().application as MyApp).viewModel

        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.waiting)

        startTimer()
    }

    private fun startTimer() {
        val totalTime = 3000L // 3 sekund
        binding.pBar.max = totalTime.toInt()

        timer = object : CountDownTimer(totalTime, 50) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = TimeUtils.getTime(millisUntilFinished)
                binding.pBar.progress = (totalTime - millisUntilFinished).toInt()
            }

            override fun onFinish() {
                FragmentManager.setFragment(
                    ExerciseFragment.newInstance(),
                    activity as AppCompatActivity
                )
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WaitingFragment()
    }
}
