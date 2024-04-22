package com.example.pace.business

import com.example.pace.domain.Workout

interface IWorkoutService {
    suspend fun createWorkout(userId: String, workout: Workout)
    suspend fun getWorkoutsForDate(userId: String, selectedDate: String): List<Workout>
    suspend fun deleteWorkout(userId: String, selectedDate: String, workoutId: String)
}