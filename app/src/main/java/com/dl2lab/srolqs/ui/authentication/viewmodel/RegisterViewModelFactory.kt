package com.dl2lab.srolqs.ui.authentication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.data.repository.UserRepository
import com.dl2lab.srolqs.di.UserInjection

class RegisterViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RegisterViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): RegisterViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RegisterViewModelFactory::class.java) {
                    INSTANCE = RegisterViewModelFactory(UserInjection.provideRepository(context))
                }
            }
            return INSTANCE as RegisterViewModelFactory
        }
    }
}