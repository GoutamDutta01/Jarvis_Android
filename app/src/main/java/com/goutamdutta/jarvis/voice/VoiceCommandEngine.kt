package com.goutamdutta.jarvis.voice

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.goutamdutta.jarvis.actions.ActionRouter

class VoiceCommandEngine(private val context: Context) {
    private val router = ActionRouter(context)

    fun execute(rawCommand: String): String {
        val command = rawCommand.trim()
        val lower = command.lowercase()

        return when {
            lower == "go home" || lower == "home" -> {
                context.sendBroadcast(Intent("com.goutamdutta.jarvis.ACTION_HOME"))
                "Going home."
            }
            lower == "go back" || lower == "back" -> "Going back."
            lower.startsWith("google search ") -> {
                val q = command.substringAfter("google search ", "").trim()
                if (q.isNotEmpty() && router.googleSearch(q)) "Searching Google for $q." else "I need a search query."
            }
            lower.startsWith("search youtube for ") -> {
                val q = command.substringAfter("search youtube for ", "").trim()
                if (q.isNotEmpty() && router.youtubeSearch(q)) "Searching YouTube for $q." else "I need a YouTube search query."
            }
            lower.startsWith("search maps for ") -> {
                val q = command.substringAfter("search maps for ", "").trim()
                if (q.isNotEmpty() && router.mapsSearch(q)) "Searching Maps for $q." else "I need a Maps search query."
            }
            lower == "open settings" -> if (router.openSettings()) "Opening settings." else "I couldn't open settings."
            lower.startsWith("open ") -> {
                val target = command.substringAfter("open ").trim()
                if (router.openApp(target)) "Opening $target." else "I couldn't find an app named $target."
            }
            lower.startsWith("call ") -> {
                val target = command.substringAfter("call ").trim()
                if (router.callContact(target)) "Calling $target." else "I couldn't start a call to $target."
            }
            lower.startsWith("navigate to ") -> {
                val destination = command.substringAfter("navigate to ").trim()
                if (router.navigate(destination)) "Starting navigation to $destination." else "I couldn't open navigation."
            }
            else -> "I heard: $command. That command is not implemented yet."
        }
    }
}
