package com.eeszen.reptide.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.logs.SetLog
import com.eeszen.reptide.databinding.LayoutSetItemExerciseBinding

class SetHistoryAdapter(private val sets: List<SetLog>) :
    RecyclerView.Adapter<SetHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutSetItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(set: SetLog) {
            binding.run {
                tvSetNumber.text = root.context.getString(R.string.exercise_sets, set.setNumber)
                etReps.setText(set.actualReps?.toString() ?: set.plannedReps.toString())
                etWeight.setText(set.actualWeight?.toString() ?: set.plannedWeight.toString())
                cbComplete.isChecked = set.completed

                // Disable interactions
                etReps.isEnabled = false
                etWeight.isEnabled = false
                cbComplete.isEnabled = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutSetItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = sets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(sets[position])
}
