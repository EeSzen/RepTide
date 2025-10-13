package com.eeszen.reptide.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eeszen.reptide.data.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert
    fun addExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise")
    fun getAllExercises() : Flow<List<Exercise>>

    @Query("SELECT * FROM exercise WHERE id = :id")
    fun getExercise(id: Int) : Exercise?

    @Update
    fun updateExercise(exercise: Exercise)

    @Delete
    fun deleteExercise(exercise: Exercise)
}