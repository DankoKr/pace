package com.example.pace.persistence.impl

import com.example.pace.domain.Workout
import com.example.pace.persistence.IWorkoutRepository
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class WorkoutRepositoryImpl(private val firestore: FirebaseFirestore) : IWorkoutRepository {
    override fun createWorkout(userId: String, workout: HashMap<String, Any>) {
        // Reference to the user document in the 'users' collection
        val userDocRef = firestore.collection("users").document(userId)

        // Reference to the date-specific collection within the user document
        // Then, create a new document within this collection for the workout
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        userDocRef.collection(currentDate).add(workout)
    }

    override suspend fun getWorkoutsForDate(userId: String, selectedDate: String): List<Workout> {
        return suspendCoroutine { continuation ->
            firestore.collection("users")
                .document(userId)
                .collection(selectedDate)
                .get()
                .addOnSuccessListener { documents ->
                    val workouts = mutableListOf<Workout>()
                    for (document in documents) {
                        val data = document.toObject(Workout::class.java)
                        workouts.add(data)
                    }
                    continuation.resume(workouts)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

}