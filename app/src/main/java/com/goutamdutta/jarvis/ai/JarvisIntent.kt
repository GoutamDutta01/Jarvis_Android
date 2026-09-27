package com.goutamdutta.jarvis.ai

sealed class JarvisIntent {
    data class OpenApp(val name: String) : JarvisIntent()
    data class GoogleSearch(val query: String) : JarvisIntent()
    data class YouTubeSearch(val query: String) : JarvisIntent()
    data class MapsSearch(val query: String) : JarvisIntent()
    data class Call(val target: String) : JarvisIntent()
    data class Navigate(val destination: String) : JarvisIntent()
    data class SetAlarm(val hour: Int, val minute: Int, val label: String) : JarvisIntent()
    data object TakePhoto : JarvisIntent()
    data object RecordVideo : JarvisIntent()
    data object PlayPause : JarvisIntent()
    data object NextTrack : JarvisIntent()
    data object PreviousTrack : JarvisIntent()
    data object OpenSettings : JarvisIntent()
    data object Home : JarvisIntent()
    data object Back : JarvisIntent()
    data class Unknown(val text: String) : JarvisIntent()
}
