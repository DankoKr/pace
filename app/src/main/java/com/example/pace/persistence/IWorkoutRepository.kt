package com.example.pace.persistence

interface IWorkoutRepository {
    fun createWorkout(userId: String, workout: HashMap<String, Any>)
}