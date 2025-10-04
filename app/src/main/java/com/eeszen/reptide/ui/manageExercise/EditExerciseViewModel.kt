package com.eeszen.reptide.ui.manageExercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditExerciseViewModel(
    private val repo: ExerciseRepo = ExerciseRepo.getInstance()
) : ViewModel() {

    private val _exercise = MutableStateFlow<Exercise?>(null)
    val exercise: StateFlow<Exercise?> = _exercise

    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    fun loadExercise(id:Int){
        val clickedExercise = repo.getExercise(id)
        if (clickedExercise != null){
            _exercise.value = clickedExercise
        }else{
            viewModelScope.launch {
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

                viewModelScope.launch {
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

}