package com.example.pace.business

import android.widget.LinearLayout
import com.example.pace.domain.Exercise

interface ExerciseService {
    fun createExercises(exercisesContainer: LinearLayout, exNameId: Int, repsFieldId: Int, kgFieldId: Int): List<Exercise>
}