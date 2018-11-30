package com.escodro.alkaa.ui.task.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.getNotificationManager
import com.escodro.alkaa.ui.TaskNotificationChannel
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

/**
 * [BroadcastReceiver] to be notified by the [android.app.AlarmManager].
 */
class TaskAlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val channel: TaskNotificationChannel by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive() - intent ${intent?.action}")

        when (intent?.action) {
            TaskAlarmManager.ALARM_ACTION -> onAlarm(intent, context)
            Intent.ACTION_BOOT_COMPLETED -> onBootCompleted()
        }
    }

    private fun onBootCompleted() {
        Timber.d("onBootCompleted")

        val worker = OneTimeWorkRequest.Builder(TaskAlarmWorker::class.java).build()
        WorkManager.getInstance().enqueue(worker)
    }

    private fun onAlarm(intent: Intent?, context: Context?) {
        val id = intent?.getLongExtra(TaskAlarmManager.EXTRA_TASK_ID, 0) ?: 0

        if (context == null || id == 0L) {
            return
        }

        val description = intent?.getStringExtra(TaskAlarmManager.EXTRA_TASK_TITLE)

        val builder = NotificationCompat.Builder(context, channel.getChannelId())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(description)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)

        Timber.d("Notifying task '$description'")
        context.getNotificationManager()?.notify(id.toInt(), builder.build())
    }
}