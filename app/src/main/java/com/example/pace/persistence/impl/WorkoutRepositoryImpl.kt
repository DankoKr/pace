package com.example.pace.persistence.impl

import com.example.pace.domain.Workout
import com.example.pace.persistence.IWorkoutRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class WorkoutRepositoryImpl(private val firestore: FirebaseFirestore) : IWorkoutRepository {
    override suspend fun createWorkout(userId: String, workout: HashMap<String, Any>, isRecurring: Boolean) {
        try {
            // Reference to the user document in the 'users' collection
            val userDocRef = firestore.collection("users").document(userId)
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            userDocRef.collection(currentDate)
                .add(workout)
                .await()

            if (isRecurring){// Duplicate the workout to all same days in the month
                duplicateWorkoutToSameDaysInMonth(userId, workout)
            }
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

    override suspend fun deleteWorkout(userId: String, selectedDate: String, workoutId: String) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection(selectedDate)
                .document(workoutId)
                .delete()
                .await()
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun editWorkout(userId: String, selectedDate: String, workout: Workout) {
        val workoutUpdates = hashMapOf(
            "workoutName" to (workout.workoutName ?: ""),
            "gymName" to (workout.gymName ?: ""),
            "exercises" to (workout.exercises?.map { exercise ->
                hashMapOf<String, Any>(
                    "name" to (exercise.name ?: ""),
                    "reps" to exercise.reps,
                    "kg" to exercise.kg
                )
            } ?: emptyList())
        )
        try {
            firestore.collection("users")
                .document(userId)
                .collection(selectedDate)
                .document(workout.id.toString())
                .set(workoutUpdates)
                .await()
        } catch (exception: Exception) {
            throw exception
        }
    }

    override suspend fun duplicateWorkoutToSameDaysInMonth(userId: String, workout: HashMap<String, Any>) {
        val calendar = Calendar.getInstance()
        val today = calendar.time

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val datesToDuplicate = mutableListOf<String>()

        // Move to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // Find the first occurrence of today's day of the week in the current month
        while (calendar.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Collect all occurrences of today's day of the week in the current month
        while (calendar.get(Calendar.MONTH) == month) {
            val formattedDate = dateFormat.format(calendar.time)
            if (formattedDate != dateFormat.format(today)) {
                datesToDuplicate.add(formattedDate)
            }
            calendar.add(Calendar.DAY_OF_MONTH, 7)
        }

        val userDocRef = firestore.collection("users").document(userId)

        for (date in datesToDuplicate) {
            try {
                userDocRef.collection(date)
                    .add(workout)
                    .await()
            } catch (exception: Exception) {
                throw exception
            }
        }
    }
}