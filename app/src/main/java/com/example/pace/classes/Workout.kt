package com.example.pace.classes

data class Workout (
    val workoutName: String? = null,
    val gymName: String? = null,
    val exercises: List<Exercise>? = null
)