package com.eeszen.reptide.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.repo.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val repo: ExerciseRepository
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    init {
        getExercises()
    }

    fun getExercises() {
        viewModelScope.launch {
            repo.getAllExercises().collect { list ->
                _exercises.value = list
            }
        }
    }

    fun getExerciseById(id: Int): Exercise? {
        return repo.getExercise(id)
    }

    fun refresh() {
        getExercises()
    }

    fun deleteExercise(exercise: Exercise) {
        repo.deleteExercise(exercise)
        refresh()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as RepTideApp).exerciseRepository
                ExerciseViewModel(repo = myRepository)
            }
        }
    }
}
