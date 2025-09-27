package com.eeszen.reptide.data.repo

import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.model.logs.WorkoutLog

class WorkoutRepo private constructor(){
    val workouts : MutableMap<Int,Workout> = mutableMapOf()
    val workoutLogs: MutableMap<Int, WorkoutLog> = mutableMapOf()    // Workout Log

    var counter = 0
    var logCounter = 0    // Workout Log

    init {
        seedData()
    }


    // --- Workouts (template) --- //
    fun addWorkout(workout: Workout){
        counter++
        workouts[counter] = workout.copy(id=counter)
    }

    fun getWorkout(id:Int) = workouts[id]
    fun updateWorkout(workout: Workout){
        workouts[workout.id!!] = workout
    }

    fun getAllWorkouts() = workouts.values.toList()
    fun deleteWorkout(id:Int){
        workouts.remove(id)
    }
    // --- Workouts (template) --- //



    // --- Workout Logs (history) --- //
    fun addWorkoutLog(log: WorkoutLog) {
        logCounter++
        workoutLogs[logCounter] = log.copy(id = logCounter)
    }
    fun getWorkoutLog(id: Int) = workoutLogs[id]
    fun getAllWorkoutLogs() = workoutLogs.values.toList()
    fun getLastWorkoutLog(): WorkoutLog? = workoutLogs.values.lastOrNull()
    // --- Workout Logs (history) --- //


    companion object{
        private var instance: WorkoutRepo? = null

        fun getInstance():WorkoutRepo{
            if (instance == null){
                instance = WorkoutRepo()
            }
            return instance!!
        }
    }

    // fake data test //
    private fun seedData() {
        val workout1 = Workout(
            id = 0,
            name = "Push Day",
            type = WorkoutType.PUSH,
            exercises = listOf(
                Exercise(0, "Bench Press", 4, 8, WorkoutType.PUSH),
                Exercise(0, "Overhead Press", 3, 10, WorkoutType.PUSH),
                Exercise(0, "Triceps Pushdown", 3, 12, WorkoutType.PUSH),
                Exercise(0, "Lateral Raises", 3, 15, WorkoutType.PUSH)
            )
        )

        val workout2 = Workout(
            id = 0,
            name = "Pull Day",
            type = WorkoutType.PULL,
            exercises = listOf(
                Exercise(0, "Deadlift", 4, 6, WorkoutType.PULL),
                Exercise(0, "Pull Ups", 3, 8, WorkoutType.PULL),
                Exercise(0, "Barbell Row", 3, 10, WorkoutType.PULL),
                Exercise(0, "Bicep Curl", 3, 12, WorkoutType.PULL)
            )
        )

        val workout3 = Workout(
            id = 0,
            name = "ngfchtryh",
            type = WorkoutType.PULL,
            exercises = listOf(
                Exercise(0, "gg", 4, 6, WorkoutType.PULL),
                Exercise(0, "gg", 3, 8, WorkoutType.PULL),
                Exercise(0, "gg", 3, 10, WorkoutType.PULL),
                Exercise(0, "rrrr", 3, 12, WorkoutType.PULL),
                Exercise(0, "uuuuuu", 3, 12, WorkoutType.PULL)
            )
        )

        addWorkout(workout1)
        addWorkout(workout2)
        addWorkout(workout3)
    }
    // fake data test //
}