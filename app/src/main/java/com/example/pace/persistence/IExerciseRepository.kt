package com.example.pace.persistence

import com.example.pace.domain.Exercise

interface IExerciseRepository {
    fun createExercise(ex: Exercise)
}