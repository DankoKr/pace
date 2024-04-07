package com.example.pace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.pace.business.IAuthService
import com.example.pace.business.impl.AuthServiceImpl

class SignInActivity : AppCompatActivity() {
    private lateinit var authService: IAuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        authService = AuthServiceImpl(this)

        val signInButton = findViewById<Button>(R.id.btnSignIn)
        signInButton.setOnClickListener {
            authService.signInWithGoogle()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authService.handleGoogleSignInResult(data)
    }
}
