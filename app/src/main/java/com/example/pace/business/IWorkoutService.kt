package com.example.pace.business

import com.example.pace.domain.Workout

interface IWorkoutService {
    fun createWorkout(userId: String, workout: Workout)
    suspend fun getWorkoutsForDate(userId: String, selectedDate: String): List<Workout>
}