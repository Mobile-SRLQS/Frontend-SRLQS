package com.dl2lab.srolqs.data.room.dao

// NotificationDao.kt
import androidx.lifecycle.LiveData
import androidx.room.*
import com.dl2lab.srolqs.data.room.entity.NotificationEntity

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): LiveData<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY timestamp DESC")
    fun getUnreadNotifications(): LiveData<List<NotificationEntity>>

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Long)
}
