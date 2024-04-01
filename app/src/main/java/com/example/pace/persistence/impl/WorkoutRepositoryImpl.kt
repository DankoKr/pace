package com.example.pace.persistence.impl

import com.example.pace.persistence.IWorkoutRepository
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutRepositoryImpl(private val firestore: FirebaseFirestore) : IWorkoutRepository {
    override fun createWorkout(userId: String, workout: HashMap<String, Any>) {
        // Reference to the user document in the 'users' collection
        val userDocRef = firestore.collection("users").document(userId)

        // Reference to the date-specific collection within the user document
        // Then, create a new document within this collection for the workout
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        userDocRef.collection(currentDate).add(workout)
    }

}