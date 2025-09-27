package com.eeszen.reptide.data.model.logs

import com.eeszen.reptide.data.model.WorkoutType

data class ExerciseLog(
    val id: Int? = null,
    val exerciseId: Int? = null, // reference to Exercise (template)
    val name: String,
    val category: WorkoutType,
    val sets: List<SetLog>,
    val duration: Long? = null
)
