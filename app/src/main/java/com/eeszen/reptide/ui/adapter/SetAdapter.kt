package com.eeszen.reptide.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.logs.SetLog
import com.eeszen.reptide.databinding.LayoutSetItemExerciseBinding
import androidx.core.widget.addTextChangedListener


class SetAdapter(
    private var sets: List<SetLog>,
    private val onSetUpdated: ((SetLog, Int) -> Unit)? = null
) : RecyclerView.Adapter<SetAdapter.SetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val binding = LayoutSetItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SetViewHolder(binding)
    }

    override fun getItemCount() = sets.size

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.bind(sets[position], position)
    }

    inner class SetViewHolder(private val binding: LayoutSetItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(set: SetLog, index: Int) {
            binding.run {
                tvSetNumber.text = root.context.getString(R.string.exercise_sets , set.setNumber)

                // Fill planned reps/weight into the fields as defaults
                etReps.setText(set.actualReps?.toString() ?: set.plannedReps.toString())
                etWeight.setText(set.actualWeight?.toString() ?: set.plannedWeight.toString())

                cbComplete.isChecked = set.completed

                cbComplete.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Notify fragment that user wants to complete this set
                        onSetUpdated?.let { it(set.copy(completed = true), index) }
                        cbComplete.isChecked = false // temporarily uncheck until timer finishes
                    } else {
                        set.completed = false
                        set.completedAt = null
                        onSetUpdated?.let { it(set, index) }
                    }
                }

                etReps.addTextChangedListener { editable ->
                    val input = editable?.toString()?.toIntOrNull()
                    if (input != null) {
                        set.actualReps = input
                        onSetUpdated?.let { it(set, index) }
                    }
                }

                etWeight.addTextChangedListener { editable ->
                    val input = editable?.toString()?.toDoubleOrNull()
                    if (input != null) {
                        set.actualWeight = input
                        onSetUpdated?.let { it(set, index) }
                    }
                }
            }
        }
    }
}
