package com.dl2lab.srolqs.ui.ViewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.data.repository.SecuredRepository
import com.dl2lab.srolqs.data.repository.UserRepository
import com.dl2lab.srolqs.di.UserInjection
import com.dl2lab.srolqs.ui.authentication.viewmodel.LoginViewModel
import com.dl2lab.srolqs.ui.authentication.viewmodel.RegisterViewModel
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel

class ViewModelFactory private constructor(
    private var securedRepository: SecuredRepository, private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository, securedRepository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(securedRepository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(securedRepository) as T
            }
            modelClass.isAssignableFrom(QuestionnaireViewModel::class.java) -> {
                QuestionnaireViewModel(securedRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                UserInjection.provideSecuredRepository(context),
                UserInjection.provideRepository(context)
            ).also { instance = it }
        }

    }
}