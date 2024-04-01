package com.example.pace

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.pace.business.IWorkoutService
import com.example.pace.business.impl.WorkoutServiceImpl
import com.example.pace.domain.Exercise
import com.example.pace.domain.Workout
import com.example.pace.persistence.impl.WorkoutRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateWorkoutActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var workoutService: IWorkoutService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        val addExerciseButton: Button = findViewById(R.id.addExerciseButton)
        val exercisesContainer: LinearLayout = findViewById(R.id.exercisesContainer)

        // Initialize Firebase Auth and Firestore instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize repository and service
        val workoutRepository = WorkoutRepositoryImpl(firestore)
        workoutService = WorkoutServiceImpl(workoutRepository)

        // Display the exercise fields
        addExerciseButton.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val exerciseView = inflater.inflate(R.layout.exercise_item, exercisesContainer,
                false
            )
            exercisesContainer.addView(exerciseView)
        }

        val btnCreateWorkout: Button = findViewById(R.id.btnCreateWorkout)
        btnCreateWorkout.setOnClickListener {
            saveWorkoutToFirebase()
        }
    }

    private fun saveWorkoutToFirebase() {
        // Get current user ID from Firebase Auth
        val userId = auth.currentUser?.uid ?: return

        val workoutName = findViewById<EditText>(R.id.workoutName).text.toString()
        val gymName = findViewById<EditText>(R.id.gymName).text.toString()

        val exercisesContainer = findViewById<LinearLayout>(R.id.exercisesContainer)
        val exercises = mutableListOf<Exercise>()

        val totalChildren = exercisesContainer.childCount
        for (i in 0 until totalChildren) {
            val exerciseLayout = exercisesContainer.getChildAt(i) as? LinearLayout
            if (exerciseLayout != null) {
                val nameEditText = exerciseLayout.findViewById<EditText>(R.id.exerciseName)
                val repsEditText = exerciseLayout.findViewById<EditText>(R.id.repsField)
                val weightEditText = exerciseLayout.findViewById<EditText>(R.id.kgField)

                val name = nameEditText?.text.toString()
                val reps = repsEditText?.text.toString().toIntOrNull() ?: 0
                val weight = weightEditText?.text.toString().toDoubleOrNull() ?: 0.0

                if (name.isNotBlank()) { // Not adding empty exercises
                    exercises.add(Exercise(name, reps, weight))
                }
            }
        }

        val workout = Workout(workoutName, gymName, exercises)
        workoutService.createWorkout(userId, workout)
    }
}
