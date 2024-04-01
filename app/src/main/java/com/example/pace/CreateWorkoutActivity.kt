package com.example.pace

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
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
    private lateinit var btnCreateWorkout: Button
    private lateinit var workoutService: IWorkoutService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        val addButton: Button = findViewById(R.id.addExerciseButton)
        val exercisesContainer: LinearLayout = findViewById(R.id.exercisesContainer)

        // Initialize Firebase Auth and Firestore instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize your repository and service
        val workoutRepository = WorkoutRepositoryImpl(firestore)
        workoutService = WorkoutServiceImpl(workoutRepository)

        addButton.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val exerciseView = inflater.inflate(R.layout.exercise_item, exercisesContainer,
                false
            )
            exercisesContainer.addView(exerciseView)
        }

        btnCreateWorkout = findViewById(R.id.btnCreateWorkout)
        btnCreateWorkout.setOnClickListener {
            saveWorkoutToFirebase()
        }
    }

    private fun saveWorkoutToFirebase() {
        val userId = auth.currentUser?.uid ?: return  // Get current user ID from Firebase Auth
        val workoutName = "User Input Workout Name"
        val gymName = "User Input Gym Name"
        val exercises = listOf(
            Exercise(name = "Squat", reps = 10, kg = 80.0),
            Exercise(name = "Bench Press", reps = 8, kg = 60.0)
        )

        val workout = Workout(workoutName, gymName, exercises)

        workoutService.createWorkout(userId, workout)
    }
}
