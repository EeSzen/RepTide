package com.eeszen.reptide.ui.home

import androidx.lifecycle.ViewModel
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.repo.WorkoutRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val repo: WorkoutRepo = WorkoutRepo.getInstance()
) : ViewModel() {

    private val _totalWorkouts = MutableStateFlow(0)
    val totalWorkouts: StateFlow<Int> = _totalWorkouts

    private val _totalExercises = MutableStateFlow(0)
    val totalExercises: StateFlow<Int> = _totalExercises

    private val _lastWorkout = MutableStateFlow<Workout?>(null)
    val lastWorkout: StateFlow<Workout?> = _lastWorkout

    fun refreshStats() {
        val workouts = repo.getAllWorkouts()
        _totalWorkouts.value = workouts.size
        _totalExercises.value = workouts.sumOf { it.exercises.size }
        _lastWorkout.value = workouts.lastOrNull()
    }
}
