package com.eeszen.reptide.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val sets: Int,
    val reps: Int,
    val category: WorkoutType,
    val duration: Long? = null,
    var isCompleted: Boolean = false,
    var isSelected: Boolean = false // for add workout page
)