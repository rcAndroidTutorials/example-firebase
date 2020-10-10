package com.youtube.firebaserealtime.presentation.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal class CatsVMFactory() : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = CatsViewModel() as T
}
