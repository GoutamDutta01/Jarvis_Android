package com.goutamdutta.jarvis.ai

class JarvisIntentParser {
    fun parse(input: String): JarvisIntent {
        val text = input.trim()
        val lower = text.lowercase()
        return when {
            lower == "home" || lower == "go home" -> JarvisIntent.Home
            lower == "back" || lower == "go back" -> JarvisIntent.Back
            lower == "take a photo" || lower == "take photo" || lower == "open camera" -> JarvisIntent.TakePhoto
            lower == "record video" || lower == "start recording" -> JarvisIntent.RecordVideo
            lower == "play music" || lower == "play" || lower == "pause" || lower == "pause music" -> JarvisIntent.PlayPause
            lower == "next song" || lower == "next track" || lower == "skip song" -> JarvisIntent.NextTrack
            lower == "previous song" || lower == "previous track" || lower == "last song" -> JarvisIntent.PreviousTrack
            lower == "open settings" || lower == "settings" -> JarvisIntent.OpenSettings
            lower.startsWith("set an alarm for ") -> parseAlarm(text.substring(18).trim())
            lower.startsWith("set alarm for ") -> parseAlarm(text.substring(15).trim())
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

    private fun parseAlarm(value: String): JarvisIntent {
        val match = Regex("(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm)?(?:\\s+(.+))?", RegexOption.IGNORE_CASE).matchEntire(value)
            ?: return JarvisIntent.Unknown("set alarm $value")
        var hour = match.groupValues[1].toInt()
        val minute = match.groupValues[2].ifBlank { "0" }.toInt()
        val meridiem = match.groupValues[3].lowercase()
        if (meridiem == "pm" && hour < 12) hour += 12
        if (meridiem == "am" && hour == 12) hour = 0
        if (hour !in 0..23 || minute !in 0..59) return JarvisIntent.Unknown("set alarm $value")
        return JarvisIntent.SetAlarm(hour, minute, match.groupValues[4].ifBlank { "Jarvis alarm" })
    }
}
