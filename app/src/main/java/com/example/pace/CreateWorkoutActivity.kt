package com.example.pace

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pace.classes.Exercise
import com.example.pace.classes.Workout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateWorkoutActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var btnCreateWorkout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        // Initialize Firebase Auth and Database instances once
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://pace-6659b-default-rtdb.europe-west1.firebasedatabase.app").reference

        btnCreateWorkout = findViewById(R.id.btnCreateWorkout)

        btnCreateWorkout.setOnClickListener {
            saveWorkoutToFirebase()
        }
    }

    fun saveWorkoutToFirebase() {
        val userId = auth.currentUser?.uid ?: return  // Use the auth instance

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val workout = Workout(
            workoutName = "Morning Workout Test",
            gymName = "Example Gym",
            exercises = listOf(
                Exercise("Push-ups", 15, 0.0),
                Exercise("Sit-ups", 20, 10.0)
            )
        )

        val userWorkoutsRef = database.child("Workouts").child(userId).child(currentDate) // Use the database instance

        val workoutKey = userWorkoutsRef.push().key ?: return

        userWorkoutsRef.child(workoutKey).setValue(workout)
            .addOnSuccessListener {
                Log.d("Firebase", "Workout saved successfully")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error saving workout", e)
            }
    }
}
