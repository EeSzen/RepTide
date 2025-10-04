package com.eeszen.reptide.ui.manageWorkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.WorkoutRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditWorkoutViewModel(
    private val repo: WorkoutRepo = WorkoutRepo.getInstance()
) : ViewModel() {
    private val _workout = MutableStateFlow<Workout?>(null)
    val workout: StateFlow<Workout?> = _workout

    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    fun loadWorkout(id:Int){
        val clickedWorkout = repo.getWorkout(id)
        if (clickedWorkout != null){
            _workout.value = clickedWorkout
        }else{
            viewModelScope.launch {
                _error.emit("Workout with ID: $id not found")
            }
        }
    }

    fun editWorkout(name:String,type:WorkoutType,exercises:List<Exercise>){
        try{
            require(name.isNotBlank()) {"Name cannot be blank"}
            require(type.toString().isNotBlank()) {"Type cannot be blank"}
            require(exercises.isNotEmpty()) {"Choose at least one exercise"}

            val oldWorkout = _workout.value
            if (oldWorkout != null){
                val updated = oldWorkout.copy(
                    name = name,
                    type = type,
                    exercises = exercises
                )

                viewModelScope.launch {
                    repo.updateWorkout(updated)
                    _finish.emit(Unit)
                }
            }

        }catch (e: Exception){
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }

}