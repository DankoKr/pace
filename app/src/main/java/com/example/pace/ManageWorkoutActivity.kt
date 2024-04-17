package com.example.pace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pace.adapter.ExerciseAdapter
import com.example.pace.domain.Workout

class ManageWorkoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_workout)

        val workout = intent.getParcelableExtra("workout") as? Workout
        if (workout == null) {
            Toast.makeText(this, "Workout data is not available!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val workoutName = findViewById<TextView>(R.id.tvWorkoutName)
        val gymName = findViewById<TextView>(R.id.tvGymName)

        workoutName.text = "${workout.workoutName}"
        gymName.text = "${workout.gymName}"

        // Populate RecyclerView with exercises
        val recyclerView: RecyclerView = findViewById(R.id.rvWorkoutExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ExerciseAdapter(workout.exercises!!)

        val btnCreateWorkout: Button = findViewById(R.id.btnDeleteWorkout)
        btnCreateWorkout.setOnClickListener {
            deleteWorkout(workout)
        }

        val btnGoBack: Button = findViewById(R.id.btnBackToMainActivity)
        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    private fun deleteWorkout(workout: Workout){
        Log.d("WorkoutIdTest", workout.id.toString())
    }
}