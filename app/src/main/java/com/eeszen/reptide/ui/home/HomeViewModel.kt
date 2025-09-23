package com.eeszen.reptide.ui.home

import androidx.lifecycle.ViewModel
import com.eeszen.reptide.data.repo.WorkoutRepo
import com.eeszen.reptide.data.model.Workout

class HomeViewModel(
    private val repo: WorkoutRepo = WorkoutRepo.getInstance()
) : ViewModel() {


    // Simulated stats (you can later calculate from history db)
    val totalWorkouts: Int = repo.getAllWorkouts().size
    val totalExercises: Int = repo.getAllWorkouts().sumOf { it.exercises.size }

    // Pretend the last workout is the last in the list
    val lastWorkout: Workout? = repo.getAllWorkouts().lastOrNull()
}
