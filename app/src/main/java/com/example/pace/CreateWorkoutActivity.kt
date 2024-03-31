package com.example.pace

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateWorkoutActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var btnCreateWorkout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        // Initialize Firebase Auth and Firestore instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        btnCreateWorkout = findViewById(R.id.btnCreateWorkout)
        btnCreateWorkout.setOnClickListener {
            saveWorkoutToFirebase()
        }
    }

    private fun saveWorkoutToFirebase() {
        val userId = auth.currentUser?.uid ?: return  // Get current user ID from Firebase Auth

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val workout = hashMapOf(
            "workoutName" to "Morning Workout Test",
            "gymName" to "Example Gym",
            "exercises" to listOf(
                hashMapOf("name" to "Push-ups", "reps" to 15, "kg" to 0),
                hashMapOf("name" to "Sit-ups", "reps" to 20, "kg" to 10)
            )
        )

        // Reference to the user document in the 'users' collection
        val userDocRef = firestore.collection("users").document(userId)

        // Reference to the date-specific collection within the user document
        // Then, create a new document within this collection for the workout
        userDocRef.collection(currentDate).add(workout)
            .addOnSuccessListener { documentReference ->
                Log.d("TestFirebase", "Workout saved successfully with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TestFirebase", "Error saving workout", e)
            }
    }
}
