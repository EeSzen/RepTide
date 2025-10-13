package com.eeszen.reptide

import android.app.Application
import androidx.room.Room
import com.eeszen.reptide.data.db.RepTideDatabase
import com.eeszen.reptide.data.repo.ExerciseRepo
import com.eeszen.reptide.data.repo.ExerciseRepository
import com.eeszen.reptide.data.repo.WorkoutRepo
import com.eeszen.reptide.data.repo.WorkoutRepository

class RepTideApp : Application() {

    // Lateinit properties for DB and repos
    lateinit var db: RepTideDatabase
    lateinit var workoutRepository: WorkoutRepository
    lateinit var exerciseRepository: ExerciseRepository

    override fun onCreate() {
        super.onCreate()

        // Initialize Room database
        db = Room.databaseBuilder(
            this,
            RepTideDatabase::class.java,
            RepTideDatabase.NAME
        ).build()

        // Initialize repositories
        workoutRepository = WorkoutRepository(db.workoutDao())
        exerciseRepository = ExerciseRepository(db.exerciseDao())
    }
}
