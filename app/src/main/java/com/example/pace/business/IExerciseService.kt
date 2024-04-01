package com.example.pace.business

interface IExerciseService {
    fun createExercise(name: String, reps: Int, kg: Double)
}