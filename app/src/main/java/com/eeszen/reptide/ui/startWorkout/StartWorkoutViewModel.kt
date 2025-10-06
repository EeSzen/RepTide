package com.eeszen.reptide.ui.startWorkout

import androidx.lifecycle.ViewModel
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.data.repo.WorkoutRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StartWorkoutViewModel(
    private val repo: WorkoutRepo = WorkoutRepo.getInstance()
) : ViewModel() {

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    var currentWorkoutLog: WorkoutLog? = null

    fun getWorkoutById(id:Int) : Workout? = repo.getWorkout(id)

}
