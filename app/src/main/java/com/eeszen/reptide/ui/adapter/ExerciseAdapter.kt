package com.eeszen.reptide.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.databinding.LayoutItemExerciseBinding

class ExerciseAdapter(
    private var exercises: List<Exercise>,
    private val onCheckedChange: ((Exercise, Boolean) -> Unit)? = null,
    private val onClick: ((Exercise) -> Unit)? = null
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

    fun getSelectedExercises(): List<Exercise> {
        return exercises.filter { it.isSelected }.map {
            it.copy(isSelected = false)
        }
    }

    inner class ExerciseViewHolder(
        private val binding: LayoutItemExerciseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: Exercise) {
            binding.run {
                tvExerciseName.text = exercise.name
                tvExerciseInfo.text =
                    if (exercise.duration != null && exercise.duration > 0) {
                        "Duration: ${exercise.duration / 60} min"
                    } else {
                        "Reps: ${exercise.reps} | Sets: ${exercise.sets}"
                    }
                cbSelect.isChecked = exercise.isSelected
                cbSelect.setOnCheckedChangeListener { _, isChecked ->
                    exercise.isSelected = isChecked
                    onCheckedChange?.let { it(exercise, isChecked) }
                }
                cvExercise.setOnClickListener {
                    onClick?.let { it(exercise) }
                }
            }
        }
    }
}
