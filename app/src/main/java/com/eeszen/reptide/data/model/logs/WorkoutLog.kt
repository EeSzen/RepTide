package com.eeszen.reptide.data.model.logs

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eeszen.reptide.data.model.WorkoutType

@Entity
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val workoutId: Int? = null, // reference to planned workout
    val name: String,
    val type: WorkoutType,
    val exercises: MutableList<ExerciseLog>,
    val startedAt: Long = System.currentTimeMillis(),
    var finishedAt: Long? = null,
    val duration: Long? = null,
    var isCompleted: Boolean = false
)
