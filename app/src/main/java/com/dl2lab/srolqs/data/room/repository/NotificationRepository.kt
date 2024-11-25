package com.dl2lab.srolqs.data.room.repository

import com.dl2lab.srolqs.data.room.dao.NotificationDao
import com.dl2lab.srolqs.data.room.entity.NotificationEntity

class NotificationRepository(private val notificationDao: NotificationDao) {
    val allNotifications = notificationDao.getAllNotifications()
    val unreadNotifications = notificationDao.getUnreadNotifications()

    suspend fun insertNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    suspend fun markAsRead(notificationId: Long) {
        notificationDao.markAsRead(notificationId)
    }

    suspend fun deleteNotification(notification: NotificationEntity) {
        notificationDao.deleteNotification(notification)
    }

    suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
}
