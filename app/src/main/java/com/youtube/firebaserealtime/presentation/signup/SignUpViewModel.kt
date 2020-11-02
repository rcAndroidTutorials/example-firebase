package com.youtube.firebaserealtime.presentation.signup

import androidx.annotation.IntegerRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.youtube.firebaserealtime.R
import com.youtube.firebaserealtime.extensions.SingleLiveEvent

class SignUpViewModel : ViewModel() {
    private val errorSLE = SingleLiveEvent<@IntegerRes Int>()
    private val successSLE = SingleLiveEvent<Boolean>()
    private var taskCompleted = false
    val errorLD: LiveData<Int> = errorSLE
    val successLD: LiveData<Boolean> = successSLE
    fun create(username: String, password: String, repeteadPassword: String) {
        // ======= Esta parte está mal en el video, pero creo que se entiende fácilmente la corección =======
        //(no me da la vida ni el tiempo volver a grabarlo jeje)
        if (username.isEmpty()){
            errorSLE.value = R.string.signup_error_username
            return
        }
        if (password.isEmpty() || password.isEmpty()) {
            errorSLE.value = R.string.signup_error_password
            return
        }
        if (password!=repeteadPassword) {
            errorSLE.value = R.string.signup_error_password_notequals
            return
        }
        // ==================================================================================================
        // A partir de aquí ya estaba bien grabado =P
        Firebase.auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (taskCompleted) return@addOnCompleteListener
                taskCompleted = true
                val newUser = Firebase.auth.currentUser
                if (task.isSuccessful && newUser!=null) {
                    newUser.sendEmailVerification().addOnCompleteListener { emailTask ->
                        successSLE.postValue(emailTask.isSuccessful)
                    }
                    Firebase.auth.signOut()
                } else {
                    errorSLE.value = R.string.signup_error
                }
        }
    }
}