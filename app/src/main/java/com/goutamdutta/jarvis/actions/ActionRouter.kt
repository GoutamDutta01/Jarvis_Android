package com.goutamdutta.jarvis.actions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.provider.Settings

class ActionRouter(private val context: Context) {
    fun openUrl(url: String): Boolean = runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        true
    }.getOrDefault(false)

    fun googleSearch(query: String): Boolean = openUrl("https://www.google.com/search?q=${Uri.encode(query)}")
    fun youtubeSearch(query: String): Boolean = openUrl("https://www.youtube.com/results?search_query=${Uri.encode(query)}")
    fun mapsSearch(query: String): Boolean = openUrl("geo:0,0?q=${Uri.encode(query)}")
    fun openSettings(): Boolean = runCatching {
        context.startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); true
    }.getOrDefault(false)

    fun setAlarm(hour: Int, minute: Int, message: String): Boolean = runCatching {
        context.startActivity(Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }); true
    }.getOrDefault(false)
}
