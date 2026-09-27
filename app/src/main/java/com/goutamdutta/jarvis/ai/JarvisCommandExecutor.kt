package com.goutamdutta.jarvis.ai

import android.content.Context
import com.goutamdutta.jarvis.actions.ActionRouter

class JarvisCommandExecutor(context: Context) {
    private val router = ActionRouter(context)
    private val parser = JarvisIntentParser()

    fun execute(command: String): String = when (val intent = parser.parse(command)) {
        is JarvisIntent.OpenApp -> if (router.openApp(intent.name)) "Opening ${intent.name}." else "I couldn't find ${intent.name}."
        is JarvisIntent.GoogleSearch -> if (intent.query.isNotBlank() && router.googleSearch(intent.query)) "Searching Google for ${intent.query}." else "Tell me what to search for."
        is JarvisIntent.YouTubeSearch -> if (intent.query.isNotBlank() && router.youtubeSearch(intent.query)) "Searching YouTube for ${intent.query}." else "Tell me what to search for."
        is JarvisIntent.MapsSearch -> if (intent.query.isNotBlank() && router.mapsSearch(intent.query)) "Searching Maps for ${intent.query}." else "Tell me what place to search for."
        is JarvisIntent.Call -> if (router.callContact(intent.target)) "Starting a call to ${intent.target}." else "I couldn't resolve that phone number yet."
        is JarvisIntent.Navigate -> if (router.navigate(intent.destination)) "Starting navigation to ${intent.destination}." else "I couldn't start navigation."
        JarvisIntent.OpenSettings -> if (router.openSettings()) "Opening settings." else "I couldn't open settings."
        JarvisIntent.Home -> "HOME_ACTION"
        JarvisIntent.Back -> "BACK_ACTION"
        is JarvisIntent.Unknown -> "I heard: ${intent.text}. I don't have an action for that yet."
    }
}
