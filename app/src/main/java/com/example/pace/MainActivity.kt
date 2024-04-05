package com.example.pace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.pace.business.IAuthService
import com.example.pace.business.impl.AuthServiceImpl

class MainActivity : AppCompatActivity() {
    private lateinit var authenticationManager: IAuthService
    // private lateinit var datePickerController: DatePickerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticationManager = AuthServiceImpl(this)
        // datePickerController = DatePickerController(this)

        val textView = findViewById<TextView>(R.id.tvProfileName)
        val signOutButton = findViewById<Button>(R.id.btnLogout)
        // val btnDatePicker = findViewById<Button>(R.id.btnDatePicker)

        textView.text = "${authenticationManager.getCurrentUserName()}"

        signOutButton.setOnClickListener {
            authenticationManager.signOutAndStartSignInActivity()
        }

        // datePickerController.setDatePickerButtonClickListener(btnDatePicker)

        findViewById<Button>(R.id.btnAddWorkout).setOnClickListener {
            startActivity(Intent(this, CreateWorkoutActivity::class.java))
            finish()
        }
    }
}