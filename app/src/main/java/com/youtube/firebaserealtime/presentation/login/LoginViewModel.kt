package com.youtube.firebaserealtime.presentation.login

import androidx.annotation.IntegerRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.youtube.firebaserealtime.R
import com.youtube.firebaserealtime.extensions.SingleLiveEvent

class LoginViewModel : ViewModel() {
    private val signUpSLE = SingleLiveEvent<Unit>()
    private val errorSLE = SingleLiveEvent<@IntegerRes Int>()
    private val successSLE = SingleLiveEvent<@IntegerRes Int>()
    val errorLD: LiveData<Int> = errorSLE
    val successLD: LiveData<Int> = successSLE
    val signUpLD: LiveData<Unit> = signUpSLE
    fun onSignUpPressed() {
        signUpSLE.call()
    }
    fun login(username: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(username, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = Firebase.auth.currentUser
                if (user!=null && user.isEmailVerified) {
                    successSLE.value = R.string.login_success
                } else {
                    errorSLE.value = R.string.login_email
                }
            } else {
                errorSLE.value = R.string.login_error
            }
        }
    }
}