package com.dl2lab.srolqs.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dl2lab.srolqs.data.preference.user.User
import com.dl2lab.srolqs.data.repository.UserRepository
import kotlinx.coroutines.runBlocking

class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun getSession(): LiveData<User> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        runBlocking {
            userRepository.logout()
        }
    }

    fun saveSession(userModel: User) {
        runBlocking {
            userRepository.saveSession(userModel)
        }
    }


}