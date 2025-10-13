package com.eeszen.reptide.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.data.repo.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: WorkoutRepository
) : ViewModel() {

    private val _totalWeight = MutableStateFlow(0)
    val totalWeight: StateFlow<Int> = _totalWeight

    private val _highestWeight = MutableStateFlow(0)
    val highestWeight: StateFlow<Int> = _highestWeight

    private val _lastWorkoutType = MutableStateFlow<WorkoutType?>(null)
    val lastWorkoutType: StateFlow<WorkoutType?> = _lastWorkoutType

    private val _lastWorkoutName = MutableStateFlow<String?>(null)
    val lastWorkoutName: StateFlow<String?> = _lastWorkoutName

    private val _completedWorkouts = MutableStateFlow<List<WorkoutLog>>(emptyList())
    val completedWorkouts: StateFlow<List<WorkoutLog>> = _completedWorkouts

    fun refreshStats() {
        viewModelScope.launch {
            // Collect Flow from repository
            repo.getCompletedWorkoutLogs().collect { workouts ->

                _completedWorkouts.value = workouts

                // Total weight lifted
                _totalWeight.value = workouts.sumOf { workout ->
                    workout.exercises.sumOf { exercise ->
                        exercise.sets.sumOf { it.actualWeight?.toInt() ?: 0 }
                    }
                }

                _lastWorkoutName.value = workouts.lastOrNull()?.name ?: "NONE"

                // Highest single weight lifted
                _highestWeight.value = workouts.flatMap { workout ->
                    workout.exercises.flatMap { exercise ->
                        exercise.sets.mapNotNull { it.actualWeight?.toInt() }
                    }
                }.maxOrNull() ?: 0

                // Last workout type
                _lastWorkoutType.value = workouts.lastOrNull()?.type
            }
        }
    }

    fun weightEntries(): List<Float> {
        return _completedWorkouts.value.flatMap { workout ->
            workout.exercises.flatMap { exercise ->
                exercise.sets.map { it.actualWeight?.toFloat() ?: 0f }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val myRepository = (this[APPLICATION_KEY] as RepTideApp).workoutRepository
                HomeViewModel(
                    repo = myRepository,
                )
            }
        }
    }
}
