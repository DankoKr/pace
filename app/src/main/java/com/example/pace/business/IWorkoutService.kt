package com.example.pace.business

import com.example.pace.domain.Workout

interface IWorkoutService {
    suspend fun createWorkout(userId: String, workout: Workout, isRecurring: Boolean)
    suspend fun getWorkoutsForDate(userId: String, selectedDate: String): List<Workout>
    suspend fun deleteWorkout(userId: String, selectedDate: String, workoutId: String)
    suspend fun editWorkout(userId: String, selectedDate: String, workout: Workout)
}