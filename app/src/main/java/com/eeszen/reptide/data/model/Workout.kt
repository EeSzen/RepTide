package com.eeszen.reptide.data.model

data class Workout(
    val id: Int? = null,
    val name: String,
    val type: WorkoutType,
    val exercises: List<Exercise>,
    val timestamp: Long = System.currentTimeMillis(), // For history
    val duration: Long? = null // Optional: track workout time
)
