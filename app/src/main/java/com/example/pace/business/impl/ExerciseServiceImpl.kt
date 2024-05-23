package com.example.pace.business.impl

import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.pace.R
import com.example.pace.business.IExerciseService
import com.example.pace.domain.Exercise
import kotlin.Int


class ExerciseServiceImpl : IExerciseService {
    override fun saveExercises(exercisesContainer: LinearLayout, exNameId: Int, repsFieldId: Int, kgFieldId: Int): List<Exercise> {
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

    override fun displayExercises(exercisesContainer: LinearLayout, exercises: List<Exercise>) {
        exercisesContainer.removeAllViews()

        for (exercise in exercises) {
            val exerciseLayout = LayoutInflater.from(exercisesContainer.context)
                .inflate(R.layout.exercise_item, exercisesContainer, false) as LinearLayout

            val nameTextView = exerciseLayout.findViewById<TextView>(R.id.exerciseName)
            val repsTextView = exerciseLayout.findViewById<TextView>(R.id.repsField)
            val weightTextView = exerciseLayout.findViewById<TextView>(R.id.kgField)

            nameTextView.text = exercise.name
            repsTextView.text = exercise.reps.toString()
            weightTextView.text = exercise.kg.toString()

            exercisesContainer.addView(exerciseLayout)
        }
    }
}