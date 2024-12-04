package com.dl2lab.srolqs.worker

import androidx.work.CoroutineWorker
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dl2lab.srolqs.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class QuestionnaireNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val classname = inputData.getString("class_name")
        val periodName = inputData.getString("period_name")
        val endDate = inputData.getString("end_date")
        var tenggat = formatTanggal(endDate ?: "")

        sendNotification(
            "Reminder Kuesioner untu kelas $classname",
            "Kuesioner periode '$periodName' akan berakhir pada $tenggat. Silakan isi sebelum tanggal pengisian berakhir."
        )
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "questionnaire_reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Questionnaire Reminder",
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

        notificationManager.notify(2, notification)
    }

    fun scheduleQuestionnaireNotification(context: Context, periodName: String, endDate: String) {
        val delay = calculateDelay(endDate, 3 * 24 * 60) // H-3

        val workRequest = OneTimeWorkRequestBuilder<QuestionnaireNotificationWorker>()
            .setInputData(
                workDataOf(
                    "period_name" to periodName,
                    "end_date" to endDate
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

    fun formatTanggal(tanggal: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return try {
            val date: Date = inputFormat.parse(tanggal) ?: return tanggal
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            tanggal
        }
    }

}
