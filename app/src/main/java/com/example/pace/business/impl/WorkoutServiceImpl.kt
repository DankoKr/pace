package com.example.pace.business.impl

import com.example.pace.business.IWorkoutService
import com.example.pace.domain.Workout
import com.example.pace.persistence.IWorkoutRepository

class WorkoutServiceImpl(private val repository: IWorkoutRepository) : IWorkoutService {
    override fun createWorkout(userId: String, workout: Workout) {
        val workoutMap = hashMapOf(
            "workoutName" to (workout.workoutName ?: ""),
            "gymName" to (workout.gymName ?: ""),
            "exercises" to (workout.exercises?.map { exercise ->
                hashMapOf<String, Any>(
                    "name" to (exercise.name ?: ""),
                    "reps" to exercise.reps,
                    "kg" to exercise.kg
                )
            } ?: emptyList())
        )

        repository.createWorkout(userId, workoutMap)
    }
}