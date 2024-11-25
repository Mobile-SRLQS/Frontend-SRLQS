package com.dl2lab.srolqs.ui.notifikasi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dl2lab.srolqs.data.room.entity.NotificationEntity
import com.dl2lab.srolqs.data.room.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {
    val allNotifications: LiveData<List<NotificationEntity>> = repository.allNotifications
    val unreadNotifications: LiveData<List<NotificationEntity>> = repository.unreadNotifications

    fun insertNotification(notification: NotificationEntity) = viewModelScope.launch {
        repository.insertNotification(notification)
    }

    fun markAsRead(notificationId: Long) = viewModelScope.launch {
        repository.markAsRead(notificationId)
    }

    fun deleteNotification(notification: NotificationEntity) = viewModelScope.launch {
        repository.deleteNotification(notification)
    }

    fun deleteAllNotifications() = viewModelScope.launch {
        repository.deleteAllNotifications()
    }
}
