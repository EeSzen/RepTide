package com.eeszen.reptide.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.eeszen.reptide.data.dao.ExerciseDao
import com.eeszen.reptide.data.dao.WorkoutDao
import com.eeszen.reptide.data.local.Converters
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.logs.ExerciseLog
import com.eeszen.reptide.data.model.logs.SetLog
import com.eeszen.reptide.data.model.logs.WorkoutLog

@Database(
    entities = [
        Workout::class,
        Exercise::class,
        WorkoutLog::class,
        ExerciseLog::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RepTideDatabase : RoomDatabase(){
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        const val NAME = "reptide_database"
    }
}

