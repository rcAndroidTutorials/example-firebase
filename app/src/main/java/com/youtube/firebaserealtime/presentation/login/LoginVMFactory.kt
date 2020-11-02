package com.youtube.firebaserealtime.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal class LoginVMFactory() : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = LoginViewModel() as T
}
