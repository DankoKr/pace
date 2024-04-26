package com.example.pace

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
// import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pace.adapter.ExerciseAdapter
import com.example.pace.business.IAuthService
import com.example.pace.business.IWorkoutService
import com.example.pace.business.impl.AuthServiceImpl
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_workout)

        firestore = FirebaseFirestore.getInstance()
        workoutRepository = WorkoutRepositoryImpl(firestore)
        workoutService = WorkoutServiceImpl(workoutRepository)
        authService = AuthServiceImpl(this)

        workout = (intent.getParcelableExtra("workout") as? Workout)!!
        selectedDate = intent.getStringExtra("selectedDate").toString()

        val workoutName = findViewById<TextView>(R.id.edWorkoutName)
        val gymName = findViewById<TextView>(R.id.edGymName)

        workoutName.text = "${workout.workoutName}"
        gymName.text = "${workout.gymName}"

        // Populate RecyclerView with exercises
        val recyclerView: RecyclerView = findViewById(R.id.rvWorkoutExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ensure workout.exercises is not null and has elements
        if (workout.exercises != null && workout.exercises!!.isNotEmpty()) {
            recyclerView.adapter = ExerciseAdapter(workout.exercises!!)
        } else {
            Log.d("ExercisesWorkout", "No exercises found")
        }

        val btnDeleteWorkout: Button = findViewById(R.id.btnDeleteWorkout)
        btnDeleteWorkout.setOnClickListener {
            deleteWorkout(workout, selectedDate)
        }

        val btnGoBack: Button = findViewById(R.id.btnBackToMainActivity)
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
        // val userId = authService.getUserId()

//        val workoutName = findViewById<EditText>(R.id.edWorkoutName).text.toString()
//        val gymName = findViewById<EditText>(R.id.edGymName).text.toString()
//
//        val changedWorkout = Workout(id = workout.id, workoutName, gymName, exercises)
//        if (userId != null) {
//            lifecycleScope.launch {
//                try {
//                    workoutService.editWorkout(userId, selectedDate, changedWorkout)
//                } catch (e: Exception) {
//                    Toast.makeText(this@ManageWorkoutActivity, "Failed to save changes: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
    }
}