package com.example.pace

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var tvDatePicker: TextView
    private lateinit var btnDatePicker: Button
    private lateinit var btnAddWorkout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Authentication setup for the Name view
        mAuth = FirebaseAuth.getInstance()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



        val textView = findViewById<TextView>(R.id.tvProfileName)

        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            val userName = user.displayName
            textView.text = "Welcome, $userName"
        } else {
            // Handle the case where the user is not signed in
        }


        val signOutButton = findViewById<Button>(R.id.btnLogout)
        signOutButton.setOnClickListener {
            signOutAndStartSignInActivity()
        }


        //DatePicker
        tvDatePicker = findViewById(R.id.tvDate)
        btnDatePicker = findViewById(R.id.btnDatePicker)

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        btnDatePicker.setOnClickListener {
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnAddWorkout = findViewById(R.id.btnAddWorkout)

        btnAddWorkout.setOnClickListener {
            startActivity(Intent(this, CreateWorkoutActivity::class.java))
            finish()
        }
    }


    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Optional: Update UI or show a message to the user
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        tvDatePicker.text = sdf.format(myCalendar.time)
    }
}


