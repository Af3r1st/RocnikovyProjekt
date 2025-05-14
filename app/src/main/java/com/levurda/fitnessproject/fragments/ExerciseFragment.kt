package com.levurda.fitnessproject.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.levurda.fitnessproject.MyApp
import com.levurda.fitnessproject.R
import com.levurda.fitnessproject.adapters.ExerciseModel
import com.levurda.fitnessproject.databinding.ExerciseBinding
import com.levurda.fitnessproject.utils.FragmentManager
import com.levurda.fitnessproject.utils.MainViewModel
import com.levurda.fitnessproject.utils.TimeUtils
import pl.droidsonroids.gif.GifDrawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View.OnTouchListener

class ExerciseFragment : Fragment() {

    private lateinit var binding: ExerciseBinding
    private lateinit var model: MainViewModel
    private var exerciseCounter = 0
    private var timer: CountDownTimer? = null
    private var exList: ArrayList<ExerciseModel>? = null
    private var ab: ActionBar? = null
    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = (requireActivity().application as MyApp).viewModel
        ab = (activity as AppCompatActivity).supportActionBar
        val savedIndex = model.getPref(model.currentDay.toString()) ?: 0
        exerciseCounter = savedIndex

        model.mutableListExercise.observe(viewLifecycleOwner) {
            for (i in 0 until savedIndex) {
                if (i in it.indices) {
                    it[i].isDone = true
                }
            }
            exList = it
            nextExercise()
        }

        binding.bNext.setOnClickListener {
            nextExercise()
        }

        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            // Only one method with correct signature must exist
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 != null && e2 != null) {
                    val deltaX = e2.x - e1.x
                    val threshold = 150
                    val velocityThreshold = 150
                    if (Math.abs(deltaX) > threshold && Math.abs(velocityX) > velocityThreshold) {
                        navigateBackToList()
                        return true
                    }
                }
                return false
            }
        })

        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun nextExercise() {
        timer?.cancel()

        if (exerciseCounter < exList?.size ?: 0) {
            val ex = exList?.get(exerciseCounter++) ?: return
            ex.isDone = true
            model.savePref(model.currentDay.toString(), exerciseCounter)
            showExercise(ex)
            setExerciseType(ex)
            showNextExercise()
        } else {
            model.markDayCompleted(model.currentDay)
            model.savePref(model.currentDay.toString(), 0)

            FragmentManager.setFragment(
                DayFinishFragment.newInstance(),
                activity as AppCompatActivity
            )
        }
    }

    private fun showExercise(ex: ExerciseModel) = with(binding) {
        if (ex.image.endsWith(".gif", true)) {
            imMain.setImageDrawable(GifDrawable(root.context.assets, ex.image))
        } else {
            val input = root.context.assets.open(ex.image)
            imMain.setImageDrawable(Drawable.createFromStream(input, null))
        }

        tvName.text = ex.name
        ab?.title = "$exerciseCounter / ${exList?.size}"
    }

    private fun setExerciseType(ex: ExerciseModel) = with(binding) {
        if (ex.time.startsWith("x")) {
            tvTime.text = ex.time
            progressBar.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.VISIBLE
            startTimer(ex)
        }
    }

    private fun startTimer(ex: ExerciseModel) = with(binding) {
        val totalTimeMs = (ex.time.toIntOrNull() ?: 0) * 1000L
        progressBar.max = totalTimeMs.toInt()
        progressBar.progress = 0

        timer = object : CountDownTimer(totalTimeMs, 50) {
            override fun onTick(millisUntilFinished: Long) {
                tvTime.text = TimeUtils.getTime(millisUntilFinished)
                progressBar.progress = (totalTimeMs - millisUntilFinished).toInt()
            }

            override fun onFinish() {
                progressBar.progress = totalTimeMs.toInt()
                nextExercise()
            }
        }.start()
    }

    private fun showNextExercise() = with(binding) {
        if (exerciseCounter < exList?.size ?: 0) {
            val next = exList?.get(exerciseCounter)!!
            imNext.setImageDrawable(GifDrawable(root.context.assets, next.image))
            tvNextName.text = "${next.name}: ${
                if (next.time.startsWith("x")) next.time else TimeUtils.getTime(next.time.toLong() * 1000)
            }"
        } else {
            imNext.setImageDrawable(GifDrawable(root.context.assets, "congratulation.gif"))
            tvNextName.text = getString(R.string.done)
        }
    }

    private fun navigateBackToList() {
        timer?.cancel()
        model.savePref(model.currentDay.toString(), exerciseCounter - 1)
        FragmentManager.setFragment(
            ExerciseListFragment.newInstance(),
            activity as AppCompatActivity
        )
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("DEBUG_FLOW", "ExerciseFragment.onDetach volán")
        timer?.cancel()
        model.savePref(model.currentDay.toString(), exerciseCounter - 1)
        model.saveDayList()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciseFragment()
    }
}
