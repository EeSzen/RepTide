package com.eeszen.reptide.data.model.logs

import com.eeszen.reptide.data.model.WorkoutType

data class WorkoutLog(
    val id: Int? = null,
    val workoutId: Int? = null, // reference to planned workout
    val name: String,
    val type: WorkoutType,
    val exercises: List<ExerciseLog>,
    val startedAt: Long = System.currentTimeMillis(),
    var finishedAt: Long? = null,
    val duration: Long? = null,
    var isCompleted: Boolean = false
)
