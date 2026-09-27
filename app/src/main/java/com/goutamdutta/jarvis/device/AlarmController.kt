package com.goutamdutta.jarvis.device

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock

class AlarmController(private val context: Context) {
    fun setAlarm(hour: Int, minute: Int, message: String = "Jarvis alarm"): Boolean = runCatching {
        context.startActivity(Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        true
    }.getOrDefault(false)
}
