package com.example.pace.persistence.impl

import com.example.pace.domain.Workout
import com.example.pace.persistence.IWorkoutRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class WorkoutRepositoryImpl(private val firestore: FirebaseFirestore) : IWorkoutRepository {
    override suspend fun createWorkout(userId: String, workout: HashMap<String, Any>) {
        try {
            // Reference to the user document in the 'users' collection
            val userDocRef = firestore.collection("users").document(userId)
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            userDocRef.collection(currentDate).add(workout).await()
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun getWorkoutsForDate(userId: String, selectedDate: String): List<Workout> {
        try {
            val documents = firestore.collection("users")
                .document(userId)
                .collection(selectedDate)
                .get()
                .await()

            return documents.map { document ->
                val workout = document.toObject(Workout::class.java)
                // Create a new Workout instance with the Firestore id
                workout.copy(id = document.id)
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

}