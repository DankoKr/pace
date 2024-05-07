package com.example.pace

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
// import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pace.business.IExerciseService
import com.example.pace.business.IAuthService
import com.example.pace.business.IWorkoutService
import com.example.pace.business.impl.AuthServiceImpl
import com.example.pace.business.impl.ExerciseServiceImpl
import com.example.pace.business.impl.WorkoutServiceImpl
import com.example.pace.domain.Workout
import com.example.pace.persistence.IWorkoutRepository
import com.example.pace.persistence.impl.WorkoutRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ManageWorkoutActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var workoutRepository: IWorkoutRepository
    private lateinit var workoutService: IWorkoutService
    private lateinit var authService: IAuthService
    private lateinit var selectedDate: String
    private lateinit var workout: Workout
    private lateinit var exerciseService: IExerciseService
    private lateinit var exerciseContainer: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_workout)

        firestore = FirebaseFirestore.getInstance()
        workoutRepository = WorkoutRepositoryImpl(firestore)
        workoutService = WorkoutServiceImpl(workoutRepository)
        authService = AuthServiceImpl(this)
        exerciseService = ExerciseServiceImpl()

        workout = (intent.getParcelableExtra("workout") as? Workout)!!
        selectedDate = intent.getStringExtra("selectedDate").toString()

        exerciseContainer = findViewById(R.id.exercisesContainer)
        exerciseService.displayExercises(exerciseContainer, workout.exercises!!)

        val addExerciseButton: Button = findViewById(R.id.addExerciseButton)
        addExerciseButton.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val exerciseView = inflater.inflate(R.layout.exercise_item, exerciseContainer,
                false
            )
            exerciseContainer.addView(exerciseView)
        }


//        val btnDeleteWorkout: Button = findViewById(R.id.btnDeleteWorkout)
//        btnDeleteWorkout.setOnClickListener {
//            deleteWorkout(workout, selectedDate)
//        }

        val btnGoBack: Button = findViewById(R.id.btnGoBack)
        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val btnSaveWorkout: Button = findViewById(R.id.btnSaveWorkout)
        btnSaveWorkout.setOnClickListener {
            editWorkout()
        }
    }
    private fun deleteWorkout(workout: Workout, selectedDate: String){
        val userId = authService.getUserId().toString()

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Delete workout '${workout.workoutName}' created on $selectedDate")
            .setTitle("Confirmation")
            .setPositiveButton("Confirm") { _, _ ->
                lifecycleScope.launch {
                    try {
                        workoutService.deleteWorkout(userId, selectedDate, workout.id.toString())
                    } catch (e: Exception) {
                        Toast.makeText(this@ManageWorkoutActivity, "Failed to delete workout: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun editWorkout() {
        val userId = authService.getUserId()
        val updatedExercises = exerciseService.saveExercises(exerciseContainer, R.id.exerciseName,
            R.id.repsField, R.id.kgField)

        workout.exercises = updatedExercises

        if (userId != null) {
            lifecycleScope.launch {
                try {
                    workoutService.editWorkout(userId, selectedDate, workout)
                    startActivity(Intent(this@ManageWorkoutActivity, MainActivity::class.java))
                } catch (e: Exception) {
                    Toast.makeText(this@ManageWorkoutActivity, "Failed to save changes: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}