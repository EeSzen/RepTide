package com.eeszen.reptide.ui.home

import androidx.lifecycle.ViewModel
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.model.logs.SetLog
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.data.repo.WorkoutRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val repo: WorkoutRepo = WorkoutRepo.getInstance()
) : ViewModel() {

    private val _totalWeight = MutableStateFlow(0)
    val totalWeight: StateFlow<Int> = _totalWeight

    private val _highestWeight = MutableStateFlow(0)
    val highestWeight: StateFlow<Int> = _highestWeight

    private val _lastWorkoutType = MutableStateFlow<WorkoutType?>(null)
    val lastWorkoutType: StateFlow<WorkoutType?> = _lastWorkoutType

    fun refreshStats() {
        val workouts = repo.getCompletedWorkoutLogs()

        // Total weight lifted
        _totalWeight.value = workouts.sumOf { workout ->
            workout.exercises.sumOf { exercise ->
                exercise.sets.sumOf { it.actualWeight?.toInt() ?: 0 }
            }
        }

        // Highest single weight lifted
        _highestWeight.value = workouts.flatMap { workout ->
            workout.exercises.flatMap { exercise ->
                exercise.sets.mapNotNull { it.actualWeight?.toInt() }
            }
        }.maxOrNull() ?: 0

        // Last workout type
        _lastWorkoutType.value = workouts.lastOrNull()?.type
    }

    fun getAllCompleted(): List<WorkoutLog>{
        return repo.getCompletedWorkoutLogs().sortedBy { it.finishedAt }
    }

    fun weightEntries(): List<Float> {
        return repo.getCompletedWorkoutLogs().flatMap { workout ->
            workout.exercises.flatMap { exercise ->
                exercise.sets.map { it.actualWeight?.toFloat() ?: 0f }
            }
        }
    }


}

