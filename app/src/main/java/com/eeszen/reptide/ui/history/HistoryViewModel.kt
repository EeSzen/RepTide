package com.eeszen.reptide.ui.history

import androidx.lifecycle.ViewModel
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.logs.ExerciseLog
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.data.repo.WorkoutRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel(
    private val repo : WorkoutRepo = WorkoutRepo.getInstance()
) : ViewModel() {

    private val _history = MutableStateFlow<List<WorkoutLog>>(emptyList())
    val history: StateFlow<List<WorkoutLog>> = _history

    fun getCompletedWorkouts(): List<WorkoutLog> {
        return repo.getCompletedWorkoutLogs()
    }

    fun getHistoryById(id:Int) : WorkoutLog?{
        return repo.getWorkoutLog(id)
    }

    fun getExerciseHistoryById(exerciseId: Int): ExerciseLog? {
        // get all workouts that have been logged
        val allWorkoutLogs = repo.getCompletedWorkoutLogs()
        // flatten into all exercises and find the one with matching id
        return allWorkoutLogs
            .flatMap { it.exercises }
            .find { it.id == exerciseId }
    }
}
