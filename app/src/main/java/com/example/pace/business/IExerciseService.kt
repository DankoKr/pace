package com.example.pace.business

import android.widget.LinearLayout
import com.example.pace.domain.Exercise

interface IExerciseService {
    fun saveExercises(exercisesContainer: LinearLayout, exNameId: Int, repsFieldId: Int, kgFieldId: Int): List<Exercise>
    fun displayExercises(exercisesContainer: LinearLayout, exercises: List<Exercise>)
}