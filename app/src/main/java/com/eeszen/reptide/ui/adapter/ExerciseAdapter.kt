package com.eeszen.reptide.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.databinding.LayoutItemExerciseBinding

class ExerciseAdapter(
    private var exercises: List<Exercise>
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemExerciseBinding.inflate(layoutInflater, parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun getItemCount() = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    fun setExercises(items: List<Exercise>) {
        exercises = items
        notifyDataSetChanged()
    }

    inner class ExerciseViewHolder(
        private val binding: LayoutItemExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            binding.tvExerciseName.text = exercise.name
            binding.tvExerciseInfo.text =
                "Reps: ${exercise.reps} | Sets: ${exercise.sets}"
        }
    }
}
