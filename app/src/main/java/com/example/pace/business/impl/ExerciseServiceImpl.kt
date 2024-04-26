package com.example.pace.business.impl

import android.widget.EditText
import android.widget.LinearLayout
import com.example.pace.R
import com.example.pace.business.ExerciseService
import com.example.pace.domain.Exercise

class ExerciseServiceImpl : ExerciseService {
    override fun createExercises(exercisesContainer: LinearLayout, exNameId: Int, repsFieldId: Int, kgFieldId: Int): List<Exercise> {
        val exercises = mutableListOf<Exercise>()

        val totalChildren = exercisesContainer.childCount
        for (i in 0 until totalChildren) {
            val exerciseLayout = exercisesContainer.getChildAt(i) as? LinearLayout
            if (exerciseLayout != null) {

                val nameEditText = exerciseLayout.findViewById<EditText>(exNameId)
                val repsEditText = exerciseLayout.findViewById<EditText>(repsFieldId)
                val weightEditText = exerciseLayout.findViewById<EditText>(kgFieldId)

                val name = nameEditText.text.toString()
                val reps = repsEditText.text.toString().toIntOrNull() ?: 0
                val weight = weightEditText.text.toString().toDoubleOrNull() ?: 0.0

                if (name.isNotBlank()) { // Not adding empty exercises
                    exercises.add(Exercise(name, reps, weight))
                }
            }
        }
        return exercises
    }
}