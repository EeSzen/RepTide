package com.eeszen.reptide.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.repo.WorkoutRepo
import com.eeszen.reptide.data.repo.WorkoutRepository
import com.eeszen.reptide.ui.exercise.ExerciseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val repo: WorkoutRepository
) : ViewModel() {

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    init {
        getWorkouts()
    }

    fun getWorkouts() {
        viewModelScope.launch {
            repo.getAllWorkouts().collect{ list ->
                _workouts.value = list
            }
        }
    }

    fun getWorkoutById(id:Int) : Workout? {
        return repo.getWorkoutById(id)
    }

    fun refresh() {
        getWorkouts()
    }

    fun deleteWorkout(workout: Workout) {
        repo.deleteWorkout(workout)
        refresh()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as RepTideApp).workoutRepository
                WorkoutViewModel(repo = myRepository)
            }
        }
    }
}
