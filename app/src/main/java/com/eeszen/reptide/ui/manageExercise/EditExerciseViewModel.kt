package com.eeszen.reptide.ui.manageExercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepo
import com.eeszen.reptide.data.repo.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditExerciseViewModel(
    private val repo: ExerciseRepository
) : ViewModel() {

    private val _exercise = MutableStateFlow<Exercise?>(null)
    val exercise: StateFlow<Exercise?> = _exercise

    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    fun loadExercise(id:Int){
        viewModelScope.launch (Dispatchers.IO) {
            val clickedExercise = repo.getExercise(id)
            if (clickedExercise != null) {
                _exercise.value = clickedExercise
            } else {
                _error.emit("Exercise with ID: $id is not found")
            }
        }
    }

    fun editExercise(name: String, sets: Int, reps: Int, category: WorkoutType) {
        try {
            require(name.isNotBlank()) { "Name cannot be blank" }
            require(category.toString().isNotBlank()) { "Category cannot be blank" }

            val oldExercise = _exercise.value
            if (oldExercise != null) {
                val updated = oldExercise.copy(
                    name = name,
                    category = category,
                    sets = sets,
                    reps = reps
                )

                viewModelScope.launch(Dispatchers.IO) {
                    repo.updateExercise(updated)
                    _finish.emit(Unit)
                }
            } else {
                viewModelScope.launch {
                    _error.emit("No exercise loaded for editing")
                }
            }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val myRepository = (this[APPLICATION_KEY] as RepTideApp).exerciseRepository
                EditExerciseViewModel(
                    repo = myRepository,
                )
            }
        }
    }
}