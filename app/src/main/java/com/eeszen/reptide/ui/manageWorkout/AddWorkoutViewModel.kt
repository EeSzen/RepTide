package com.eeszen.reptide.ui.manageWorkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.WorkoutRepo
import com.eeszen.reptide.data.repo.WorkoutRepository
import com.eeszen.reptide.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AddWorkoutViewModel(
    private val repo: WorkoutRepository
) : ViewModel() {

    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    fun addWorkout(name:String,type:WorkoutType,exercises: List<Exercise>) {
        try {
            require(name.isNotBlank()) { "Name cannot be blank" }
            require(type.toString().isNotBlank()) { "Type cannot be blank" }
            require(exercises.isNotEmpty()) { "Exercises cannot be empty" }

            val workout = Workout(
                name = name,
                type = type,
                exercises = exercises
            )
            viewModelScope.launch(Dispatchers.IO) {
                repo.addWorkout(workout)
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch {
                _error.emit(e.message.toString())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val myRepository = (this[APPLICATION_KEY] as RepTideApp).workoutRepository
                AddWorkoutViewModel(
                    repo = myRepository,
                )
            }
        }
    }
}