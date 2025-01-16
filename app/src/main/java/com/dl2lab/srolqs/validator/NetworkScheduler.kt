package com.dl2lab.srolqs.validator

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log

class NetworkScheduler(private val context: Context, private val db: SQLiteDatabase) {

    fun scheduleNetworkTask(task: NetworkTask) {
        try {
            val values = ContentValues().apply {
                put("target_class", task.targetClass)
                put("user_id", task.userId)
                put("target_package", task.targetPackage)
                put("tag", task.tag)
                put("task_type", task.taskType)
                put("required_idleness_state", task.requiredIdlenessState)
                put("service_kind", task.serviceKind)
                put("source_version", task.sourceVersion)
                put("persistence_level", task.persistenceLevel)
                put("preferred_charging_state", task.preferredChargingState)
                put("required_network_type", task.requiredNetworkType)
                put("runtime", task.runtime)
                put("retry_strategy", task.retryStrategy)
                put("last_runtime", task.lastRuntime)
            }

            db.insertWithOnConflict("pending_ops", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        } catch (e: SQLiteException) {
        }
    }


}

data class NetworkTask(
    val targetClass: String,
    val userId: Int,
    val targetPackage: String,
    val tag: String,
    val taskType: Int,
    val requiredIdlenessState: Int,
    val serviceKind: Int,
    val sourceVersion: Int,
    val persistenceLevel: Int,
    val preferredChargingState: Int,
    val requiredNetworkType: Int,
    val runtime: Long,
    val retryStrategy: String,
    val lastRuntime: Long
)