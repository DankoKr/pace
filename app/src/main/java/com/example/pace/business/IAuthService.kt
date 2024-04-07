package com.example.pace.business

import android.content.Intent

interface IAuthService {
    fun signInWithGoogle()
    fun handleGoogleSignInResult(data: Intent?)
    fun firebaseAuthWithGoogle(idToken: String)
    fun signOutAndStartSignInActivity()
    fun getCurrentUserName(): String?
    fun getUserId(): String?
}