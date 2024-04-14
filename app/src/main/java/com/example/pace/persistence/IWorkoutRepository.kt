package com.example.pace.persistence

import com.example.pace.domain.Workout

interface IWorkoutRepository {
    fun createWorkout(userId: String, workout: HashMap<String, Any>)
    suspend fun getWorkoutsForDate(userId: String, selectedDate: String): List<Workout>
}