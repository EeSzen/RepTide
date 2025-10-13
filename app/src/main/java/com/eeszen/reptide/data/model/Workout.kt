package com.eeszen.reptide.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val type: WorkoutType,
    val exercises: List<Exercise>,
    val timestamp: Long = System.currentTimeMillis(), // For history
    val duration: Long? = null // Optional: track workout time
)
