package com.eeszen.reptide.data.model.logs

data class SetLog(
    val setNumber: Int,
    val plannedReps: Int,
    val plannedWeight: Double,
    var actualReps: Int? = null,
    var actualWeight: Double? = null,
    var completed: Boolean = false,
    var completedAt: Long? = null
)
