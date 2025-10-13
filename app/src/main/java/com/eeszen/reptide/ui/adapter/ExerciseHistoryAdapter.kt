package com.eeszen.reptide.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eeszen.reptide.data.model.logs.ExerciseLog
import com.eeszen.reptide.databinding.LayoutItemHistoryExerciseBinding

class ExerciseHistoryAdapter(
    private var exerciseLogs: List<ExerciseLog>,
    private val onExerciseClick: ((ExerciseLog) -> Unit)? = null
) : RecyclerView.Adapter<ExerciseHistoryAdapter.ExerciseHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHistoryViewHolder {
        val binding = LayoutItemHistoryExerciseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExerciseHistoryViewHolder(binding)
    }

    override fun getItemCount() = exerciseLogs.size

    override fun onBindViewHolder(holder: ExerciseHistoryViewHolder, position: Int) {
        holder.bind(exerciseLogs[position])
    }

    inner class ExerciseHistoryViewHolder(
        private val binding: LayoutItemHistoryExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: ExerciseLog) {
            binding.tvExerciseName.text = exercise.name
            binding.tvExerciseCategory.text = exercise.category.toString()

            binding.root.setOnClickListener {
                onExerciseClick?.let { it(exercise) }
            }
        }
    }
}
