package com.eeszen.reptide.ui.workout

import androidx.lifecycle.ViewModel
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.repo.WorkoutRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WorkoutViewModel(
    private val repo: WorkoutRepo = WorkoutRepo.getInstance()
) : ViewModel() {

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    init {
        getWorkouts()
    }

    fun getWorkouts() {
        _workouts.update { repo.getAllWorkouts() }
    }

    fun getWorkoutById(id:Int) : Workout? {
        return repo.getWorkout(id)
    }

    fun refresh() {
        getWorkouts()
    }

    fun deleteWorkout(workout: Workout) {
        repo.deleteWorkout(workout.id!!)
        refresh()
    }
}
