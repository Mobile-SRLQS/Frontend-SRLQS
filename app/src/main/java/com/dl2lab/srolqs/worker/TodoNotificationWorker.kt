package com.dl2lab.srolqs.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dl2lab.srolqs.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class TodoNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val namaKegiatan = inputData.getString("nama_kegiatan")
        val tenggat = inputData.getString("tenggat")

        sendNotification(
            "Ayo Selesaikan Kegiatanmu!",
            "Kegiatan '$namaKegiatan' mendekati tenggat waktu pada $tenggat. Silahkan selesaikan kegiatan tersebut untuk meningkatkan dimensi Time Management-mu."
        )
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "todo_reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Todo Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
    fun scheduleTodoNotificationscheduleTodoNotification(context: Context, namaKegiatan: String, tenggat: String, reminderWaktu: Long) {
        var delay = calculateDelay(tenggat, reminderWaktu)
        if (delay <= 0) {delay = 1}

        val workRequest = OneTimeWorkRequestBuilder<TodoNotificationWorker>()
            .setInputData(
                workDataOf(
                    "nama_kegiatan" to namaKegiatan,
                    "tenggat" to tenggat
                )
            )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun calculateDelay(tenggat: String, reminderWaktu: Long): Long {
        val tenggatMillis = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(tenggat)?.time ?: 0
        val currentMillis = System.currentTimeMillis()
        return tenggatMillis - (reminderWaktu * 60 * 1000) - currentMillis
    }


}
