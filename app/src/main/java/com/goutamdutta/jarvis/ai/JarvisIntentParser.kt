package com.goutamdutta.jarvis.ai

class JarvisIntentParser {
    fun parse(input: String): JarvisIntent {
        val text = input.trim()
        val lower = text.lowercase()
        return when {
            lower == "home" || lower == "go home" -> JarvisIntent.Home
            lower == "back" || lower == "go back" -> JarvisIntent.Back
            lower == "open settings" || lower == "settings" -> JarvisIntent.OpenSettings
            lower.startsWith("open ") -> JarvisIntent.OpenApp(text.substring(5).trim())
            lower.startsWith("google search ") -> JarvisIntent.GoogleSearch(text.substring(14).trim())
            lower.startsWith("search google for ") -> JarvisIntent.GoogleSearch(text.substring(18).trim())
            lower.startsWith("search youtube for ") -> JarvisIntent.YouTubeSearch(text.substring(20).trim())
            lower.startsWith("youtube search ") -> JarvisIntent.YouTubeSearch(text.substring(15).trim())
            lower.startsWith("search maps for ") -> JarvisIntent.MapsSearch(text.substring(17).trim())
            lower.startsWith("navigate to ") -> JarvisIntent.Navigate(text.substring(12).trim())
            lower.startsWith("call ") -> JarvisIntent.Call(text.substring(5).trim())
            else -> JarvisIntent.Unknown(text)
        }
    }
}
