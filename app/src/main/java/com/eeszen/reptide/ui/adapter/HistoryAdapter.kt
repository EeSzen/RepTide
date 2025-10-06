package com.eeszen.reptide.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.databinding.LayoutItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private var workoutLogs: List<WorkoutLog>,
    private val onWorkoutClick: ((WorkoutLog) -> Unit)? = null
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = LayoutItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun getItemCount() = workoutLogs.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(workoutLogs[position])
    }

    inner class HistoryViewHolder(private val binding: LayoutItemHistoryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutLog) {
            binding.tvWorkoutName.text = workout.name
            binding.tvWorkoutType.text = workout.type.name

            val durationMillis = (workout.finishedAt ?: 0L) - workout.startedAt
            val durationMinutes = durationMillis / 60000
            binding.tvWorkoutDuration.text = "Duration: ${durationMinutes} min"

            binding.tvCompletionDate.text = formatDate(workout.finishedAt)

            binding.root.setOnClickListener {
                onWorkoutClick?.invoke(workout)
            }
        }
    }

    // date time convertor
    private fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return "Unknown date"
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
