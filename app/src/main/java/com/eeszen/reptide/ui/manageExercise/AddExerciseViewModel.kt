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
import com.eeszen.reptide.ui.manageWorkout.AddWorkoutViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AddExerciseViewModel(
    private val repo : ExerciseRepository
) : ViewModel() {

    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    fun addExercise(name:String,category:WorkoutType,sets:Int,reps:Int,duration:Long? = null){
        try{
            require(name.isNotBlank()) {"Name cannot be blank"}
            require(category.toString().isNotBlank()) {"Category cannot be blank"}

            val exercise = Exercise(
//                id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                name = name,
                category = category,
                sets = sets,
                reps = reps,
                duration = duration
            )

            viewModelScope.launch(Dispatchers.IO) {
                repo.addExercise(exercise)
                _finish.emit(Unit)
            }

        }catch (e:Exception){
            viewModelScope.launch {
                _error.emit(e.message.toString())
            }
        }

    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Get the dependency in your factory
                val myRepository = (this[APPLICATION_KEY] as RepTideApp).exerciseRepository
                AddExerciseViewModel(
                    repo = myRepository,
                )
            }
        }
    }
}