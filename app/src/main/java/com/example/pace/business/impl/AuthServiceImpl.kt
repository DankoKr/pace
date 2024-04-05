package com.example.pace.business.impl

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pace.MainActivity
import com.example.pace.R
import com.example.pace.SignInActivity
import com.example.pace.business.IAuthService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

private const val RC_SIGN_IN = 9001

class AuthServiceImpl(private val context: Context) : IAuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        val signInIntent = googleSignInClient.signInIntent
        (context as AppCompatActivity).startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(context, "Signed in as ${user?.displayName}",
                        Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as AppCompatActivity).finish()
                } else {
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun signOutAndStartSignInActivity() {
        this.auth.signOut()
        val intent = Intent(context, SignInActivity::class.java)
        context.startActivity(intent)
    }

    override fun getCurrentUserName(): String? {
        val user = this.auth.currentUser
        return user?.displayName
    }

    override fun getUserId(): String? {
        return auth.currentUser?.uid
    }
}