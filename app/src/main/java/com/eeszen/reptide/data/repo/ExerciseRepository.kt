package com.eeszen.reptide.data.repo

import com.eeszen.reptide.data.dao.ExerciseDao
import com.eeszen.reptide.data.model.Exercise
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val dao: ExerciseDao) {

    fun addExercise(exercise: Exercise) = dao.addExercise(exercise)

    fun getAllExercises(): Flow<List<Exercise>> = dao.getAllExercises()

    fun getExercise(id: Int): Exercise? = dao.getExercise(id)

    fun updateExercise(exercise: Exercise) = dao.updateExercise(exercise)

    fun deleteExercise(exercise: Exercise) = dao.deleteExercise(exercise)
}
