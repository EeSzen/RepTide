package com.eeszen.reptide.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.databinding.LayoutItemWorkoutBinding

// for date conversion
import java.sql.Date
import java.text.DateFormat

class WorkoutAdapter(
    private var workouts: List<Workout>,
    private val onClick: (Workout) -> Unit
): RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemWorkoutBinding.inflate(layoutInflater, parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun getItemCount() = workouts.size

    fun setWorkouts(items:List<Workout>){
        this.workouts = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = workouts[position]
        holder.bind(item)
    }

    inner class WorkoutViewHolder(
        private val binding : LayoutItemWorkoutBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(item:Workout){
            binding.run {
                tvWorkoutName.text = item.name
                tvWorkoutType.text = root.context.getString(
                    R.string.workout_type,
                    item.type
                )
                tvExerciseCount.text = root.context.getString(
                    R.string.workout_count,
                    item.exercises.size
                )

//                // Show timestamp as a date
//                val date = Date(item.timestamp)
//                val formattedDate = DateFormat.getDateInstance().format(date)
//                tvWorkoutDate.text = formattedDate

                cvWorkout.setOnClickListener {
                    onClick(item)
                }

            }
        }
    }
}