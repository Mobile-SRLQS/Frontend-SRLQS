package com.dl2lab.srolqs.utils

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationHelper(private val context: Context) {

    fun scheduleNotification(
        title: String,
        message: String,
        deadlineTime: Long,
        reminderOffset: Long
    ) {
        // Cek permission untuk Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        // Hitung waktu notifikasi (deadline - offset)
        val notificationTime = deadlineTime - reminderOffset

        // Jika waktu notifikasi sudah lewat, tidak perlu dijadwalkan
        if (notificationTime <= System.currentTimeMillis()) {
            Log.d("NotificationHelper", "Notification time has passed")
            return
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
//            putExtra("channel_id", channelId)
            putExtra("deadline_time", deadlineTime)
        }

        // Buat unique ID berdasarkan deadline dan title
        val uniqueId = (deadlineTime.toString() + title).hashCode()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime,
                    pendingIntent
                )
            }

            // Log untuk debugging
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            Log.d("NotificationHelper", """
                Notification scheduled:
                Title: $title
                Message: $message
                Notification Time: ${formatter.format(Date(notificationTime))}
                Deadline Time: ${formatter.format(Date(deadlineTime))}
                Reminder Offset: ${reminderOffset / (60 * 1000)} minutes
            """.trimIndent())

            // Optional: Simpan informasi notifikasi ke SharedPreferences atau Database
            saveScheduledNotification(
                uniqueId,
                title,
                message,
                notificationTime,
                deadlineTime
            )

        } catch (e: SecurityException) {
            Log.e("NotificationHelper", "Error scheduling notification", e)
        }
    }

    // Fungsi helper untuk menyimpan informasi notifikasi
    private fun saveScheduledNotification(
        id: Int,
        title: String,
        message: String,
        notificationTime: Long,
        deadlineTime: Long
    ) {
        val prefs = context.getSharedPreferences("notifications", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("notification_$id", """
                {
                    "title": "$title",
                    "message": "$message",
                    "notification_time": $notificationTime,
                    "deadline_time": $deadlineTime
                }
            """.trimIndent())
            apply()
        }
    }

    // Fungsi untuk membatalkan notifikasi yang dijadwalkan
    fun cancelScheduledNotification(id: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        // Hapus dari SharedPreferences
        val prefs = context.getSharedPreferences("notifications", Context.MODE_PRIVATE)
        prefs.edit().remove("notification_$id").apply()
    }

    // Fungsi untuk mengecek apakah notifikasi sudah dijadwalkan
    fun isNotificationScheduled(id: Int): Boolean {
        val intent = Intent(context, NotificationReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) != null
    }

    // Fungsi untuk mendapatkan semua notifikasi yang dijadwalkan
    fun getScheduledNotifications(): List<ScheduledNotification> {
        val prefs = context.getSharedPreferences("notifications", Context.MODE_PRIVATE)
        val notifications = mutableListOf<ScheduledNotification>()

        prefs.all.forEach { (key, value) ->
            if (key.startsWith("notification_")) {
                try {
                    val json = JSONObject(value as String)
                    notifications.add(
                        ScheduledNotification(
                            id = key.removePrefix("notification_").toInt(),
                            title = json.getString("title"),
                            message = json.getString("message"),
                            notificationTime = json.getLong("notification_time"),
                            deadlineTime = json.getLong("deadline_time")
                        )
                    )
                } catch (e: Exception) {
                    Log.e("NotificationHelper", "Error parsing notification data", e)
                }
            }
        }

        return notifications
    }
}

// Data class untuk menyimpan informasi notifikasi
data class ScheduledNotification(
    val id: Int,
    val title: String,
    val message: String,
    val notificationTime: Long,
    val deadlineTime: Long
)
