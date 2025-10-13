package com.eeszen.reptide.data.repo

import com.eeszen.reptide.data.dao.WorkoutDao
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.logs.WorkoutLog
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val dao: WorkoutDao) {

    // Workout
    fun addWorkout(workout: Workout) = dao.addWorkout(workout)
    fun getAllWorkouts(): Flow<List<Workout>> = dao.getAllWorkouts()
    fun getWorkoutById(id: Int): Workout? = dao.getWorkoutById(id)
    fun updateWorkout(workout: Workout) = dao.updateWorkout(workout)
    fun deleteWorkout(workout: Workout) = dao.deleteWorkout(workout)

    // WorkoutLog
    fun addWorkoutLog(workoutLog: WorkoutLog) = dao.addWorkoutLog(workoutLog)
    fun getCompletedWorkoutLogs(): Flow<List<WorkoutLog>> = dao.getAllWorkoutLogs()
    fun getWorkoutLogById(id: Int): WorkoutLog? = dao.getWorkoutLogById(id)
    fun updateWorkoutLog(workoutLog: WorkoutLog) = dao.updateWorkoutLog(workoutLog)
    fun deleteWorkoutLog(workoutLog: WorkoutLog) = dao.deleteWorkoutLog(workoutLog)
}
