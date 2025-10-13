package com.eeszen.reptide.data.local

import androidx.room.TypeConverter
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.model.logs.ExerciseLog
import com.eeszen.reptide.data.model.logs.SetLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    // Converts list into JSON and back
    private val gson = Gson()

    // WorkoutType <-> String
    @TypeConverter
    fun fromWorkoutType(type: WorkoutType): String = type.name

    @TypeConverter
    fun toWorkoutType(value: String): WorkoutType = WorkoutType.valueOf(value)

    // Exercise List <-> JSON
    @TypeConverter
    fun fromExerciseList(list: List<Exercise>?): String =
        gson.toJson(list ?: emptyList<Exercise>())

    @TypeConverter
    fun toExerciseList(value: String): List<Exercise> {
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(value, type)
    }

    // ExerciseLog List <-> JSON
    @TypeConverter
    fun fromExerciseLogList(list: List<ExerciseLog>?): String =
        gson.toJson(list ?: emptyList<ExerciseLog>())

    @TypeConverter
    fun toExerciseLogList(value: String): List<ExerciseLog> {
        val type = object : TypeToken<List<ExerciseLog>>() {}.type
        return gson.fromJson(value, type)
    }

    // SetLog List <-> JSON
    @TypeConverter
    fun fromSetLogList(list: List<SetLog>?): String =
        gson.toJson(list ?: emptyList<SetLog>())

    @TypeConverter
    fun toSetLogList(value: String): List<SetLog> {
        val type = object : TypeToken<List<SetLog>>() {}.type
        return gson.fromJson(value, type)
    }
}
