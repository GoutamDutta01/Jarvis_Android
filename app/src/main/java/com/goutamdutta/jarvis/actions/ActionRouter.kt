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

    fun openApp(appName: String): Boolean {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(0)
        val target = packages.firstOrNull {
            pm.getApplicationLabel(it).toString().equals(appName, ignoreCase = true)
        } ?: packages.firstOrNull {
            pm.getApplicationLabel(it).toString().contains(appName, ignoreCase = true)
        } ?: return false
        val launch = pm.getLaunchIntentForPackage(target.packageName) ?: return false
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launch)
        return true
    }

    fun callContact(nameOrNumber: String): Boolean {
        val uri = if (nameOrNumber.matches(Regex("[+0-9 ()-]{5,}"))) {
            Uri.parse("tel:${Uri.encode(nameOrNumber)}")
        } else return false
        return runCatching {
            context.startActivity(Intent(Intent.ACTION_DIAL, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            true
        }.getOrDefault(false)
    }

    fun navigate(destination: String): Boolean = openUrl("google.navigation:q=${Uri.encode(destination)}")
}
