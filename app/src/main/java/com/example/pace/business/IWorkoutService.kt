package com.example.pace.business

import com.example.pace.domain.Workout

interface IWorkoutService {
    fun createWorkout(userId: String, workout: Workout)
}