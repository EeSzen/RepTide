package com.eeszen.reptide.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.logs.ExerciseLog
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.data.repo.WorkoutRepo
import com.eeszen.reptide.data.repo.WorkoutRepository
import com.eeszen.reptide.ui.workout.WorkoutViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repo : WorkoutRepository
) : ViewModel() {

    private val _history = MutableStateFlow<List<WorkoutLog>>(emptyList())
    val history: StateFlow<List<WorkoutLog>> = _history

    private val _exerciseHistory = MutableStateFlow<ExerciseLog?>(null)
    val exerciseHistory: StateFlow<ExerciseLog?> = _exerciseHistory

    init {
        viewModelScope.launch {
            repo.getCompletedWorkoutLogs().collect { list ->
                _history.value = list
            }
        }
    }

    fun getCompletedWorkouts(): List<WorkoutLog> {
        // returns the current cached value
        return _history.value
    }

    fun getHistoryById(id:Int) : WorkoutLog?{
        return repo.getWorkoutLogById(id)
    }

    fun getExerciseHistoryById(exerciseId: Int) {
        viewModelScope.launch {
            repo.getCompletedWorkoutLogs().collect { logs ->
                val exerciseLog = logs
                    .flatMap { it.exercises }
                    .find { it.id == exerciseId }
                _exerciseHistory.value = exerciseLog
            }
        }
    }

    fun deleteHistory(history:WorkoutLog){
        repo.deleteWorkoutLog(history)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as RepTideApp).workoutRepository
                HistoryViewModel(repo = myRepository)
            }
        }
    }
}
