package com.eeszen.reptide.ui.manageExercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AddExerciseViewModel(
    private val repo : ExerciseRepo = ExerciseRepo.getInstance()
) : ViewModel() {

    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    fun addExercise(name:String,category:WorkoutType,sets:Int,reps:Int,duration:Long? = null){
        try{
            require(name.isNotBlank()) {"Name cannot be blank"}
            require(category.toString().isNotBlank()) {"Category cannot be blank"}
            require(sets.toString().isNotBlank()) {"Sets cannot be empty"}

            val exercise = Exercise(
                name = name,
                category = category,
                sets = sets,
                reps = reps,
                duration = duration
            )

            viewModelScope.launch {
                repo.addExercise(exercise)
                _finish.emit(Unit)
            }

        }catch (e:Exception){
            viewModelScope.launch {
                _error.emit(e.message.toString())
            }
        }
    }
}