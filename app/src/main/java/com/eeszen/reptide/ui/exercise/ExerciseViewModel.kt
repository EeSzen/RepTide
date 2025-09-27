package com.eeszen.reptide.ui.exercise

import androidx.lifecycle.ViewModel
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.repo.ExerciseRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ExerciseViewModel(
    private val repo: ExerciseRepo = ExerciseRepo.getInstance()
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    init {
        getExercises()
    }

    fun getExercises(){
        _exercises.update { repo.getAllExercises() }
    }

    fun getExerciseById(id:Int) : Exercise? {
        return repo.getExercise(id)
    }

    fun refresh() {
        getExercises()
    }

    fun deleteExercise(exercise: Exercise) {
        repo.deleteExercise(exercise.id!!)
        refresh()
    }

}