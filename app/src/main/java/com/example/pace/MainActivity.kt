package com.example.pace

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pace.adapter.WorkoutAdapter
import com.example.pace.business.IAuthService
import com.example.pace.business.IWorkoutService
import com.example.pace.business.impl.AuthServiceImpl
import com.example.pace.business.impl.WorkoutServiceImpl
import com.example.pace.domain.Workout
import com.example.pace.persistence.IWorkoutRepository
import com.example.pace.persistence.impl.WorkoutRepositoryImpl
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var authService: IAuthService
    private lateinit var workoutRepository: IWorkoutRepository
    private lateinit var workoutService: IWorkoutService
    private lateinit var firestore: FirebaseFirestore
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestore = FirebaseFirestore.getInstance()
        authService = AuthServiceImpl(this)
        workoutRepository = WorkoutRepositoryImpl(firestore)
        workoutService = WorkoutServiceImpl(workoutRepository)

        val profileName = findViewById<TextView>(R.id.tvProfileName)
        val signOutButton = findViewById<Button>(R.id.btnLogout)
        val getWorkoutsForDateButton = findViewById<Button>(R.id.btnGetWorkoutsForDate)
        // Set up MaterialDatePicker
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvWorkouts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        profileName.text = "${authService.getCurrentUserName()}"

        // SignOut button
        signOutButton.setOnClickListener {
            authService.signOutAndStartSignInActivity()
        }

        // The Date button
        getWorkoutsForDateButton.setOnClickListener {
            picker.show(supportFragmentManager, picker.toString())
        }

        // Select a date and display it on the button
        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            getWorkoutsForDateButton.text = selectedDate
            fetchWorkouts(selectedDate!!)
        }

        // Go to CreateWorkoutActivity
        findViewById<Button>(R.id.btnAddWorkout).setOnClickListener {
            startActivity(Intent(this, CreateWorkoutActivity::class.java))
            finish()
        }
    }

    private fun fetchWorkouts(selectedDate: String) {
        val userId = authService.getUserId().toString()
        val recyclerView = findViewById<RecyclerView>(R.id.rvWorkouts)

        lifecycleScope.launch {
            try {
                val workouts = workoutService.getWorkoutsForDate(userId, selectedDate)
                if (workouts.isNotEmpty()) {
                    val adapter = WorkoutAdapter(workouts)
                    adapter.setOnItemClickListener(object : WorkoutAdapter.OnItemClickListener {
                        override fun onItemClick(workout: Workout) {
                            // Start ManageWorkoutActivity with selected workout data
                            val intent = Intent(this@MainActivity, ManageWorkoutActivity::class.java)
                            intent.putExtra("workout", workout)
                            intent.putExtra("selectedDate", selectedDate)
                            startActivity(intent)
                        }
                    })
                    recyclerView.adapter = adapter
                } else {
                    recyclerView.adapter = null
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("No Workouts")
                    builder.setMessage("There are no workouts available for $selectedDate.")
                    builder.setPositiveButton("OK", null)
                    val dialog = builder.create()
                    dialog.show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching workouts", e)
            }
        }
    }

}