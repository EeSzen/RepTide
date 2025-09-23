package com.eeszen.reptide.data.repo

import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.WorkoutType

class ExerciseRepo private constructor() {
    private val exercises: MutableMap<Int, Exercise> = mutableMapOf()
    private var counter = 0

    init {
        seedData()
    }

    fun addExercise(exercise: Exercise) {
        counter++
        exercises[counter] = exercise.copy(id = counter)
    }

    fun getExercise(id: Int) = exercises[id]

    fun updateExercise(exercise: Exercise) {
        exercises[exercise.id!!] = exercise
    }

    fun getAllExercises() = exercises.values.toList()

    fun getExercisesByCategory(type: WorkoutType) =
        exercises.values.filter { it.category == type }

    fun getCompletedExercises() =
        exercises.values.filter { it.isCompleted }

    fun getPendingExercises() =
        exercises.values.filter { !it.isCompleted }

    fun deleteExercise(id: Int) {
        exercises.remove(id)
    }

    companion object {
        private var instance: ExerciseRepo? = null

        fun getInstance(): ExerciseRepo {
            if (instance == null) {
                instance = ExerciseRepo()
            }
            return instance!!
        }
    }

    // Fake data for testing
    private fun seedData() {
        addExercise(Exercise(0, "Bench Press", 4, 8, WorkoutType.PUSH))
        addExercise(Exercise(0, "Deadlift", 4, 6, WorkoutType.PULL))
        addExercise(Exercise(0, "Squat", 4, 10, WorkoutType.LEGS))
        addExercise(Exercise(0, "Plank", 3, 60, WorkoutType.CORE))
        addExercise(Exercise(0, "Running", 1, 20, WorkoutType.CARDIO))
    }

}
