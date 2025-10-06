package com.eeszen.reptide.ui.history

import androidx.lifecycle.ViewModel
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.data.repo.WorkoutRepo

class HistoryViewModel : ViewModel() {
    private val repo = WorkoutRepo.getInstance()

    fun getCompletedWorkouts(): List<WorkoutLog> {
        return repo.getCompletedWorkoutLogs()
    }
}
