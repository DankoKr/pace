package com.example.pace.persistence

import com.example.pace.domain.Workout

interface IWorkoutRepository {
    suspend fun createWorkout(userId: String, workout: HashMap<String, Any>)
    suspend fun getWorkoutsForDate(userId: String, selectedDate: String): List<Workout>
    suspend fun deleteWorkout(userId: String, selectedDate: String, workoutId: String)
}