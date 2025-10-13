package com.eeszen.reptide.ui.startWorkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.data.repo.WorkoutRepo
import com.eeszen.reptide.data.repo.WorkoutRepository
import com.eeszen.reptide.ui.workout.WorkoutViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StartWorkoutViewModel(
    private val repo: WorkoutRepository
) : ViewModel() {

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    var currentWorkoutLog: WorkoutLog? = null

    fun getWorkoutById(id:Int) : Workout? = repo.getWorkoutById(id)

    fun addWorkoutLog(workoutLog: WorkoutLog) {
        repo.addWorkoutLog(workoutLog)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as RepTideApp).workoutRepository
                StartWorkoutViewModel(repo = myRepository)
            }
        }
    }
}
