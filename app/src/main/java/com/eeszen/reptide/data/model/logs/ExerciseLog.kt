package com.eeszen.reptide.data.model.logs

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eeszen.reptide.data.model.WorkoutType

@Entity
data class ExerciseLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // room handles this id, makes a new id if its 0
    val exerciseId: Int? = null, // reference to Exercise (template)
    val name: String,
    val category: WorkoutType,
    val sets: MutableList<SetLog>,
    val duration: Long? = null,
    var isCompleted: Boolean = false
)
