package com.eeszen.reptide.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.logs.WorkoutLog
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    // Workout
    @Insert
    fun addWorkout(workout: Workout)

    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workout WHERE id = :id")
    fun getWorkoutById(id: Int): Workout?

    @Delete
    fun deleteWorkout(workout: Workout)

    @Update
    fun updateWorkout(workout: Workout)

    // WorkoutLog
    @Insert
    fun addWorkoutLog(workoutLog: WorkoutLog)

    @Query("SELECT * FROM workoutlog")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workoutlog WHERE id = :id")
    fun getWorkoutLogById(id: Int): WorkoutLog?

    @Update
    fun updateWorkoutLog(workoutLog: WorkoutLog)

    @Delete
    fun deleteWorkoutLog(workoutLog: WorkoutLog)
}
