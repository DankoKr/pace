package com.example.pace.business.impl

import com.example.pace.business.IWorkoutService
import com.example.pace.domain.Workout
import com.example.pace.domain.Exercise
import com.example.pace.persistence.IWorkoutRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WorkoutServiceImplTest {

    @Mock
    private lateinit var repository: IWorkoutRepository

    private lateinit var workoutService: IWorkoutService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        workoutService = WorkoutServiceImpl(repository)
    }

    @Test
    fun `test createWorkout`() = runBlocking {
        // Given
        val userId = "user123"
        val exercises = listOf(Exercise("exercise1", 10, 20.0))
        val workout = Workout("workout1", "workout1","gym1", exercises)
        val expectedWorkoutMap = hashMapOf(
            "workoutName" to "workout1",
            "gymName" to "gym1",
            "exercises" to listOf(
                hashMapOf(
                    "name" to "exercise1",
                    "reps" to 10,
                    "kg" to 20.0
                )
            )
        )

        `when`(repository.createWorkout(userId, expectedWorkoutMap, false)).then {}

        // When
        workoutService.createWorkout(userId, workout, false)

        // Then
        // Verify that repository.createWorkout() was called with the expected arguments
        org.mockito.Mockito.verify(repository).createWorkout(userId, expectedWorkoutMap, false)
    }

    @Test
    fun `test getWorkoutsForDate`() = runBlocking {
        // Given
        val userId = "user123"
        val selectedDate = "2023-05-24"
        val workouts = listOf(Workout("w1", "workout1","gym1", listOf(Exercise("exercise1", 10, 20.0))))

        `when`(repository.getWorkoutsForDate(userId, selectedDate)).thenReturn(workouts)

        // When
        val result = workoutService.getWorkoutsForDate(userId, selectedDate)

        // Then
        // Verify that the returned list of workouts matches the expected value
        assertEquals(workouts, result)
    }

    @Test
    fun `test deleteWorkout`() = runBlocking {
        // Given
        val userId = "user123"
        val selectedDate = "2023-05-24"
        val workoutId = "workout123"

        `when`(repository.deleteWorkout(userId, selectedDate, workoutId)).then {}

        // When
        workoutService.deleteWorkout(userId, selectedDate, workoutId)

        // Then
        // Verify that repository.deleteWorkout() was called with the expected arguments
        org.mockito.Mockito.verify(repository).deleteWorkout(userId, selectedDate, workoutId)
    }

    @Test
    fun `test editWorkout`() = runBlocking {
        // Given
        val userId = "user123"
        val selectedDate = "2023-05-24"
        val workout = Workout("workout1", "workout1","gym1", listOf(Exercise("exercise1", 10, 20.0)))

        `when`(repository.editWorkout(userId, selectedDate, workout)).then {}

        // When
        workoutService.editWorkout(userId, selectedDate, workout)

        // Then
        // Verify that repository.editWorkout() was called with the expected arguments
        org.mockito.Mockito.verify(repository).editWorkout(userId, selectedDate, workout)
    }
}
