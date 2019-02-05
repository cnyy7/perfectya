package com.escodro.alkaa.ui.task.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.escodro.alkaa.ui.task.alarm.worker.TaskNotifierWorker
import com.escodro.alkaa.ui.task.alarm.worker.TaskReschedulerWorker
import org.koin.standalone.KoinComponent
import timber.log.Timber

/**
 * [BroadcastReceiver] to be notified by the [android.app.AlarmManager].
 */
class TaskReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive() - intent ${intent?.action}")

        when (intent?.action) {
            ALARM_ACTION -> onAlarm(intent)
            Intent.ACTION_BOOT_COMPLETED -> onBootCompleted()
        }
    }

    private fun onBootCompleted() {
        Timber.d("onBootCompleted")

        val worker = OneTimeWorkRequest.Builder(TaskReschedulerWorker::class.java).build()
        WorkManager.getInstance().enqueue(worker)
    }

    private fun onAlarm(intent: Intent?) {
        Timber.d("onAlarm")

        val taskId = intent?.getLongExtra(EXTRA_TASK, 0) ?: return
        val data = Data.Builder()
        data.putLong(EXTRA_TASK, taskId)

        val worker = OneTimeWorkRequest.Builder(TaskNotifierWorker::class.java)
        worker.setInputData(data.build())

        WorkManager.getInstance().enqueue(worker.build())
    }

    companion object {

        const val EXTRA_TASK = "extra_task"

        const val ALARM_ACTION = "com.escodro.alkaa.SET_ALARM"
    }
}
