package com.example.pace

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pace.business.IAuthService
import com.example.pace.business.IExerciseService
import com.example.pace.business.IWorkoutService
import com.example.pace.business.impl.AuthServiceImpl
import com.example.pace.business.impl.ExerciseServiceImpl
import com.example.pace.business.impl.WorkoutServiceImpl
import com.example.pace.domain.Workout
import com.example.pace.persistence.IWorkoutRepository
import com.example.pace.persistence.impl.WorkoutRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class CreateWorkoutActivity : AppCompatActivity() {

    private lateinit var authService: IAuthService
    private lateinit var firestore: FirebaseFirestore
    private lateinit var workoutRepository: IWorkoutRepository
    private lateinit var workoutService: IWorkoutService
    private lateinit var exerciseService: IExerciseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        val addExerciseButton: Button = findViewById(R.id.addExerciseButton)
        val exercisesContainer: LinearLayout = findViewById(R.id.exercisesContainer)

        // Initialize Firebase Auth and Firestore instances
        authService = AuthServiceImpl(this)
        firestore = FirebaseFirestore.getInstance()

        // Initialize repository and service
        workoutRepository = WorkoutRepositoryImpl(firestore)
        workoutService = WorkoutServiceImpl(workoutRepository)
        exerciseService = ExerciseServiceImpl()

        // Display the exercise fields
        addExerciseButton.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val exerciseView = inflater.inflate(R.layout.exercise_item, exercisesContainer, false)
            exercisesContainer.addView(exerciseView)
        }

        val btnCreateWorkout: Button = findViewById(R.id.btnCreateWorkout)
        btnCreateWorkout.setOnClickListener {
            lifecycleScope.launch {
                saveWorkoutToFirebase()
            }
        }

        val btnGoBack: Button = findViewById(R.id.btnGoBack)
        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun showRecurringDialog(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to create a recurring workout?")
                .setTitle("Recurring or not?")
                .setPositiveButton("Yes") { _, _ ->
                    continuation.resume(true)
                }
                .setNegativeButton("No") { _, _ ->
                    continuation.resume(false)
                }
                .setOnCancelListener {
                    continuation.resume(false)
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private suspend fun saveWorkoutToFirebase() {
        val userId = authService.getUserId()

        val workoutName = findViewById<EditText>(R.id.workoutName).text.toString()
        val gymName = findViewById<EditText>(R.id.gymName).text.toString()

        val exercisesContainer = findViewById<LinearLayout>(R.id.exercisesContainer)

        val exercises = exerciseService.saveExercises(exercisesContainer, R.id.exerciseName, R.id.repsField, R.id.kgField)

        val workout = Workout(id = null, workoutName, gymName, exercises)

        // Show the dialog and wait for the user response
        val isRecurring = showRecurringDialog()

        if (userId != null) {
            try {
                workoutService.createWorkout(userId, workout, isRecurring)
                startActivity(Intent(this@CreateWorkoutActivity, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@CreateWorkoutActivity, "Failed to create workout: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
