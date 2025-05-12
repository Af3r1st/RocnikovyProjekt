package com.levurda.fitnessproject.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.levurda.fitnessproject.R
import com.levurda.fitnessproject.databinding.ExerciseListItemBinding
import pl.droidsonroids.gif.GifDrawable

class ExerciseAdapter(var listener: Listener) :
    ListAdapter<ExerciseModel, ExerciseAdapter.ExerciseHolder>(MyComparator()) {

    class ExerciseHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ExerciseListItemBinding.bind(view)

        fun setData(exercise: ExerciseModel) = with(binding) {
            tvName.text = exercise.name
            tvCount.text = exercise.time

            try {
                when {
                    exercise.image.endsWith(".gif", ignoreCase = true) -> {
                        val gifDrawable = GifDrawable(itemView.context.assets, exercise.image)
                        imEx.setImageDrawable(gifDrawable)
                    }
                    exercise.image.endsWith(".jpg", ignoreCase = true) ||
                            exercise.image.endsWith(".png", ignoreCase = true) -> {
                        val inputStream = itemView.context.assets.open(exercise.image)
                        val drawable = Drawable.createFromStream(inputStream, null)
                        imEx.setImageDrawable(drawable)
                    }
                    else -> {
                        imEx.setImageResource(R.drawable.bg_picture)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                imEx.setImageResource(R.drawable.bg_picture)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_list_item, parent, false)
        return ExerciseHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class MyComparator : DiffUtil.ItemCallback<ExerciseModel>() {
        override fun areItemsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onClick(day: DayModel)
    }
}
