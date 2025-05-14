package com.levurda.fitnessproject.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.levurda.fitnessproject.R
import com.levurda.fitnessproject.adapters.ExerciseModel
import com.levurda.fitnessproject.databinding.ExerciseBinding
import com.levurda.fitnessproject.utils.FragmentManager
import com.levurda.fitnessproject.utils.MainViewModel
import com.levurda.fitnessproject.utils.TimeUtils
import pl.droidsonroids.gif.GifDrawable

class ExerciseFragment : Fragment() {

    private lateinit var binding: ExerciseBinding
    private var exerciseCounter = 0
    private var exList: ArrayList<ExerciseModel>? = null
    private val model: MainViewModel by activityViewModels()
    private var timer: CountDownTimer? = null
    private var ab: ActionBar? = null
    private var currentDay = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentDay = model.currentDay
        val savedIndex = model.getPref(currentDay.toString())
        exerciseCounter = if (savedIndex != null && savedIndex >= 0) savedIndex else 0

        ab = (activity as AppCompatActivity).supportActionBar

        model.mutableListExercise.observe(viewLifecycleOwner) {
            exList = it
            nextExercise()
        }

        binding.bNext.setOnClickListener {
            nextExercise()
        }
    }

    private fun nextExercise() {
        timer?.cancel()

        if (exerciseCounter < exList?.size ?: 0) {
            val ex = exList?.get(exerciseCounter++) ?: return
            ex.isDone = true  // ✅ označíme cvik jako hotový
            model.savePref(currentDay.toString(), exerciseCounter)
            showExercise(ex)
            setExerciseType(ex)
            showNextExercise()
        } else {
            // ✅ označíme celý den jako hotový
            model.markDayCompleted(model.currentDay)
            model.savePref(currentDay.toString(), 0)  // reset pozice

            FragmentManager.setFragment(
                DayFinishFragment.newInstance(),
                activity as AppCompatActivity
            )
        }
    }

    private fun showExercise(exercise: ExerciseModel?) = with(binding) {
        exercise ?: return@with

        if (exercise.image.endsWith(".gif", ignoreCase = true)) {
            imMain.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
        } else {
            val inputStream = root.context.assets.open(exercise.image)
            val drawable = Drawable.createFromStream(inputStream, null)
            imMain.setImageDrawable(drawable)
        }
        tvName.text = exercise.name
        val title = "$exerciseCounter / ${exList?.size}"
        ab?.title = title
    }

    private fun setExerciseType(exercise: ExerciseModel) = with(binding) {
        if (exercise.time.startsWith("x", ignoreCase = true)) {
            tvTime.text = exercise.time
            progressBar.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.VISIBLE
            startTimer(exercise)
        }
    }

    private fun startTimer(exercise: ExerciseModel) = with(binding) {
        timer?.cancel()
        val totalTimeMs = (exercise.time.toIntOrNull() ?: 0) * 1000L
        progressBar.max = totalTimeMs.toInt()
        progressBar.progress = 0

        timer = object : CountDownTimer(totalTimeMs, 50) {
            override fun onTick(restTime: Long) {
                val elapsed = totalTimeMs - restTime
                tvTime.text = TimeUtils.getTime(restTime)
                progressBar.progress = elapsed.toInt()
            }

            override fun onFinish() {
                progressBar.progress = totalTimeMs.toInt()
                nextExercise()
            }
        }.start()
    }

    private fun showNextExercise() = with(binding) {
        if (exerciseCounter < exList?.size!!) {
            val ex = exList?.get(exerciseCounter) ?: return
            imNext.setImageDrawable(GifDrawable(root.context.assets, ex.image))
            setTimeTape(ex)
        } else {
            imNext.setImageDrawable(GifDrawable(root.context.assets, "congratulation.gif"))
            tvNextName.text = getString(R.string.done)
        }
    }

    private fun setTimeTape(ex: ExerciseModel) {
        binding.tvNextName.text = if (ex.time.startsWith("x")) {
            ex.time
        } else {
            "${ex.name}: ${TimeUtils.getTime(ex.time.toLong() * 1000)}"
        }
    }

    override fun onDetach() {
        super.onDetach()
        model.savePref(currentDay.toString(), exerciseCounter - 1)
        timer?.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciseFragment()
    }
}
