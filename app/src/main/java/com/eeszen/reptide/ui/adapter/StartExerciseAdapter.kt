package com.eeszen.reptide.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eeszen.reptide.data.model.logs.ExerciseLog
import com.eeszen.reptide.databinding.FragmentStartExercisePageBinding


// Adapter for ViewPager2
// One page for each exercise

class StartExerciseAdapter(
    private var exercises: List<ExerciseLog>,
    private val onExerciseUpdated: ((ExerciseLog, Int) -> Unit)? = null
) : RecyclerView.Adapter<StartExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = FragmentStartExercisePageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExerciseViewHolder(binding)
    }

    override fun getItemCount() = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position], position)
    }

    inner class ExerciseViewHolder(
        private val binding: FragmentStartExercisePageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: ExerciseLog, position: Int) {
            binding.tvExerciseName.text = exercise.name

            // Sets adapter
            binding.rvSets.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = SetAdapter(exercise.sets) { updatedSet, setIndex ->
                    // Replace the set in the list with the updated one
                    exercise.sets[setIndex] = updatedSet

                    // Recalculate completion
                    exercise.isCompleted = exercise.sets.all { it.completed }

                    // Notify parent (StartWorkoutFragment)
                    onExerciseUpdated?.invoke(exercise, position)
                }

            }
        }
    }
}
